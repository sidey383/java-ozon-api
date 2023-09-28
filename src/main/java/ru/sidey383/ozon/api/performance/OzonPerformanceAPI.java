package ru.sidey383.ozon.api.performance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.AnswerList;
import ru.sidey383.ozon.api.ItemList;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.exception.OzonExceptionFactory;
import ru.sidey383.ozon.api.exception.OzonUnauthorizedException;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;
import ru.sidey383.ozon.api.performance.exception.OzonAPINoBodyException;
import ru.sidey383.ozon.api.performance.objects.answer.StatisticAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.TokenAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaignAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaignState;
import ru.sidey383.ozon.api.performance.objects.answer.statistic.ReportInfo;
import ru.sidey383.ozon.api.performance.objects.answer.statistic.StatisticStatus;
import ru.sidey383.ozon.api.performance.objects.request.StatisticRequest;
import ru.sidey383.ozon.api.performance.objects.request.TokenRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SynchronousQueue;

public class OzonPerformanceAPI {

    private final Logger logger = LoggerFactory.getLogger(OzonPerformanceAPI.class);

    private static final int STATUS_UPDATE_TIME = 2500;

    private static final int MAX_WAIT_TIME = 1000 * 60 * 15;

    public static final String API_URL = "https://performance.ozon.ru";

    private final ObjectMapper mapper;

    private final OkHttpClient client = new OkHttpClient();

    private final String clientId;

    private final String clientSecret;

    private final Thread reportReader;

    private final SynchronousQueue<StatisticRequestFuture> statisticQueue = new SynchronousQueue<>();

    public OzonPerformanceAPI(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        reportReader = new Thread(this::statisticReader);
        reportReader.start();
    }

    public void stop() {
        reportReader.interrupt();
    }

    public record StatisticRequestFuture(StatisticRequest request, CompletableFuture<StatisticResponse> future) {}

    public record StatisticResponse(Exception exception, StatisticStatus status, byte[] data) {}

    @SuppressWarnings("BusyWait")
    private void statisticReader() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                StatisticRequestFuture request = statisticQueue.take();
                logger.debug("Start statistic request fot " + clientId + ": " + request);
                StatisticAnswer answer;
                try {
                    answer = getClientStatistics(request.request());
                    logger.debug("Create statistic request fot " + clientId + ": " + answer);
                } catch (OzonApiException | IOException e) {
                    request.future().complete(new StatisticResponse(e, null, null));
                    continue;
                }
                StatisticStatus status = null;
                do {
                    try {
                        status = getClientStatisticStatus(answer.uuid());
                        logger.debug("Receive statistic request status fot " + clientId + ": " + status);
                        if (!status.state().isTerminate()) {
                            Thread.sleep(STATUS_UPDATE_TIME);
                        }
                    } catch (OzonApiException | IOException e) {
                        request.future.complete(new StatisticResponse(e, status, null));
                        break;
                    }
                } while (!status.state().isTerminate());
                try {
                    request.future.complete(new StatisticResponse(null, status, getClientStatisticsReport(status)));
                } catch (OzonApiException | IOException e) {
                    request.future.complete(new StatisticResponse(e, status, null));
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


    private record Token(String token, long expireIn, Instant updateTime) {
        public String getParamContent() {
            return "Bearer " + token;
        }

        public String getParamName() {
            return "Authorization";
        }

        public boolean isExpire() {
            return Duration.between(updateTime, Instant.now()).getSeconds() > expireIn;
        }
    }

    private Token token;

    @Nullable
    public String getToken() {
        if (isTokenExpire()) {
            return null;
        }
        return token.token();
    }

    public void updateToken() throws IOException, OzonWrongCodeException {
        Instant updateTime = Instant.now();
        String json = mapper.writeValueAsString(new TokenRequest(clientId, clientSecret, "client_credentials"));
        Request request = new Request.Builder()
                .url(API_URL + "/api/client/token")
                .post(RequestBody
                        .create(
                                json,
                                MediaType.parse("application/json")
                        )).build();
        final TokenAnswer answer;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw  OzonExceptionFactory.onWrongCode(response);
            }
            assert response.body() != null;
            answer = mapper.readValue(response.body().string(), TokenAnswer.class);
        }
        synchronized (this) {
            token = new Token(answer.access_token(), answer.expires_in(), updateTime);
        }
    }

    public boolean isTokenExpire() {
        return token == null || token.isExpire();
    }

    /**
     * <a href="https://performance.ozon.ru:443/api/client/campaign">/api/client/campaign</a>
     */
    public AnswerList<CampaignAnswer> getClientCampaigns(@Nullable String[] campaignId, @Nullable String type, @Nullable CampaignState state) throws OzonApiException, IOException {
        HttpUrl httpUtl = HttpUrl.parse(API_URL + "/api/client/campaign");
        if (httpUtl == null)
            throw new IllegalStateException("Can't parse http url " + API_URL + "/api/client/campaign");
        HttpUrl.Builder urlBuilder = httpUtl.newBuilder();
        if (campaignId != null && campaignId.length > 0 && campaignId[0] != null)
            urlBuilder.addQueryParameter("campaignIds", campaignId[0]);
        if (type != null)
            urlBuilder.addQueryParameter("advObjectType", type);
        if (state != null)
            urlBuilder.addQueryParameter("state", state.name());
        Request.Builder request = new Request.Builder()
                .url(urlBuilder.build().url())
                .get();
        return getResponse(request, new TypeReference<>() {
        });
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/SubmitRequest">"/api/client/statistics"</>
     * **/
    protected StatisticAnswer getClientStatistics(StatisticRequest request) throws OzonApiException, IOException {
        String json = mapper.writeValueAsString(request);
        Request.Builder builder = new Request.Builder()
                .url(API_URL + "/api/client/statistics")
                .post(RequestBody
                        .create(
                                json,
                                MediaType.parse("application/json")
                        ));
        return getResponse(builder, StatisticAnswer.class);
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/SubmitRequest">"/api/client/statistics"</>
     * <a href="https://docs.ozon.ru/api/performance/#operation/StatisticsCheck">"/api/client/statistics/{UUID}"</>
     * <a href="https://docs.ozon.ru/api/performance/#operation/DownloadStatistics">"/api/client/statistics/report"</>
     * **/
    public StatisticRequestFuture getClientStatisticsFuture(StatisticRequest request){
        StatisticRequestFuture future = new StatisticRequestFuture(request, new CompletableFuture<>());
        new Thread(() -> {
            try {
                statisticQueue.put(future);
            } catch (InterruptedException e) {
                future.future().complete(new StatisticResponse(e, null, null));
            }
        }).start();
        return future;
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/StatisticsCheck">"/api/client/statistics/{UUID}"</>
     * **/
    public StatisticStatus getClientStatisticStatus(UUID uuid) throws OzonApiException, IOException {
        Request.Builder request = new Request.Builder()
                .url(API_URL + "/api/client/statistics/" + uuid.toString());
        return getResponse(request, StatisticStatus.class);
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/AttributionSubmitRequest">"/api/client/statistics/attribution"</>
     * **/
    public StatisticAnswer getClientStatisticsAttribution(StatisticRequest statisticAnswer) throws OzonApiException, IOException {
        String json = mapper.writeValueAsString(statisticAnswer);
        Request.Builder request = new Request.Builder()
                .url(API_URL + "/api/client/statistics/attribution")
                .post(RequestBody
                        .create(
                                json,
                                MediaType.parse("application/json")
                        ));
        return getResponse(request, StatisticAnswer.class);
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/ListReports">"/api/client/statistics/list"</>
     * **/
    public ItemList<ReportInfo> getClientStatisticsList(long page, long pageSize) throws OzonApiException, IOException {
        HttpUrl httpUtl = HttpUrl.parse(API_URL + "/api/client/statistics/list");
        if (httpUtl == null)
            throw new IllegalStateException("Can't parse http url " + API_URL + "/api/client/statistics/list");
        HttpUrl url = httpUtl.newBuilder()
                .addQueryParameter("page", Long.toString(page))
                .addQueryParameter("pageSize", Long.toString(pageSize))
                .build();
        return getResponse(new Request.Builder().url(url.url()).get(), new TypeReference<>() {});
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/DownloadStatistics">"/api/client/statistics/report"</>
     * **/
    protected byte[] getClientStatisticsReport(StatisticStatus status) throws OzonApiException, IOException {
        if (isTokenExpire())
            updateToken();
        Request request = new Request.Builder()
                .url(API_URL + status.link())
                .addHeader(token.getParamName(), token.getParamContent())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            OzonWrongCodeException exception = OzonExceptionFactory.onWrongCode(response);
            if (exception instanceof OzonUnauthorizedException) {
                updateToken();
                return getClientStatisticsReport(status);
            } else {
                throw exception;
            }
        }
        if (response.body() == null)
            throw new OzonAPINoBodyException(request.url().toString(), response.code());
        return response.body().bytes();
    }

    private <T> T getResponse(Request.Builder requestBuilder, Class<T> type) throws OzonApiException, IOException {
        if (isTokenExpire())
            updateToken();
        requestBuilder.header(token.getParamName(), token.getParamContent());
        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                OzonWrongCodeException exception = OzonExceptionFactory.onWrongCode(response);
                if (exception instanceof OzonUnauthorizedException) {
                    updateToken();
                    return getResponse(requestBuilder, type);
                } else {
                    throw exception;
                }
            }
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().url().toString(), response.code());
            return mapper.readValue(response.body().string(), type);
        }
    }

    private <T> T getResponse(Request.Builder requestBuilder, TypeReference<T> type) throws OzonApiException, IOException {
        if (isTokenExpire())
            updateToken();
        requestBuilder.addHeader(token.getParamName(), token.getParamContent());
        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                OzonWrongCodeException exception = OzonExceptionFactory.onWrongCode(response);
                if (exception instanceof OzonUnauthorizedException) {
                    updateToken();
                    return getResponse(requestBuilder, type);
                } else {
                    throw exception;
                }
            }
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().toString(), response.code());
            return mapper.readValue(response.body().string(), type);
        }
    }

}
