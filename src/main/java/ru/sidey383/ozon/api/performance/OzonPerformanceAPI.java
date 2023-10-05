package ru.sidey383.ozon.api.performance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.container.AnswerList;
import ru.sidey383.ozon.api.container.ItemList;
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
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class OzonPerformanceAPI {

    private final Logger logger = LoggerFactory.getLogger(OzonPerformanceAPI.class);

    private static final int STATUS_UPDATE_TIME = 3000;

    private final Object asyncRequestBlock = new Object();

    public static final String API_URL = "https://performance.ozon.ru";

    private final ObjectMapper mapper;

    private final OkHttpClient client = new OkHttpClient();

    private final String clientId;

    private final String clientSecret;

    private final List<WorkingThread> workingThreads = Collections.synchronizedList(new ArrayList<>());

    public OzonPerformanceAPI(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void stop() {
        workingThreads.forEach(Thread::interrupt);
    }

    public record StatisticResponse(Throwable exception, StatisticStatus status, byte[] data) {
    }

    private class WorkingThread extends Thread {

        private final StatisticRequest request;

        private final CompletableFuture<StatisticResponse> future;

        private WorkingThread(StatisticRequest request) {
            this.request = request;
            this.future = new CompletableFuture<>();
        }

        @Override
        @SuppressWarnings("BusyWait")
        public void run() {
            workingThreads.add(this);
            StatisticAnswer answer = null;
            StatisticStatus status = null;
            try {
                synchronized (asyncRequestBlock) {
                    answer = getClientStatistics(request);
                    logger.debug("Create statistic request for " + clientId + ": " + answer);
                    do {
                        status = getClientStatisticStatus(answer.uuid());
                        logger.debug("Receive statistic request status for " + clientId + ": " + status);
                        if (!status.state().isTerminate()) {
                            Thread.sleep(STATUS_UPDATE_TIME);
                        }
                    } while (!status.state().isTerminate());
                }
                byte[] data = getClientStatisticsReport(status);
                logger.debug("Complete statistic request status for " + clientId + ": " + status);
                future.complete(new StatisticResponse(null, status, data));
            } catch (InterruptedException e) {
                this.interrupt();
                logger.warn("Statistic request interrupted for " + clientId + ": " + answer + " " + status);
                future.complete(new StatisticResponse(e, status, null));
            } catch (Throwable th) {
                logger.error("Can't complete statistic request for " + clientId + ": " + answer + " " + status);
                future.complete(new StatisticResponse(th, status, null));
            } finally {
                workingThreads.remove(this);
            }
        }

        public CompletableFuture<StatisticResponse> getFuture() {
            return future;
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
            return Duration.between(updateTime, Instant.now()).getSeconds() > (expireIn - 5);
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
                throw OzonExceptionFactory.onWrongCode(response);
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
     **/
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

    public String getCampaignObjects(String campaignID) throws OzonApiException, IOException {
        if (isTokenExpire())
            updateToken();
        Request request = new Request.Builder()
                .header(token.getParamName(), token.getParamContent())
                .url(API_URL + "/api/client/campaign/" + campaignID + "/objects")
                .header(token.getParamName(), token.getParamContent())
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                OzonWrongCodeException exception = OzonExceptionFactory.onWrongCode(response);
                if (exception instanceof OzonUnauthorizedException) {
                    updateToken();
                    return getCampaignObjects(campaignID);
                } else {
                    throw exception;
                }
            }
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().toString(), response.code());
            return response.body().string();
        }
    }

    public byte[] getClientStatisticsExpense(String[] campaignIDs, LocalDate from, LocalDate to) throws OzonApiException, IOException {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(API_URL + "/api/client/statistics/expense")).newBuilder();
        if (campaignIDs != null)
            for (String cmp : campaignIDs) {
                urlBuilder.addQueryParameter("campaignIds", cmp);
            }
        if (from != null)
            urlBuilder.addQueryParameter("dateFrom", from.toString());
        if (to != null)
            urlBuilder.addQueryParameter("dateTo", to.toString());
        if (isTokenExpire())
            updateToken();

        Request request = new Request.Builder()
                .header(token.getParamName(), token.getParamContent())
                .url(urlBuilder.build().url()).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                OzonWrongCodeException exception = OzonExceptionFactory.onWrongCode(response);
                if (exception instanceof OzonUnauthorizedException) {
                    updateToken();
                    return getClientStatisticsExpense(campaignIDs, from, to);
                } else {
                    throw exception;
                }
            }
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().url().toString(), response.code());
            return response.body().bytes();
        }
    }

    public byte[] getClientStatisticsCampaignProduct(String[] campaignIDs, LocalDate from, LocalDate to) throws OzonApiException, IOException {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(API_URL + "/api/client/statistics/campaign/product")).newBuilder();
        if (campaignIDs != null)
            for (String cmp : campaignIDs) {
                urlBuilder.addQueryParameter("campaignIds", cmp);
            }
        if (from != null)
            urlBuilder.addQueryParameter("dateFrom", from.toString());
        if (to != null)
            urlBuilder.addQueryParameter("dateTo", to.toString());
        if (isTokenExpire())
            updateToken();

        Request request = new Request.Builder()
                .header(token.getParamName(), token.getParamContent())
                .url(urlBuilder.build().url()).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                OzonWrongCodeException exception = OzonExceptionFactory.onWrongCode(response);
                if (exception instanceof OzonUnauthorizedException) {
                    updateToken();
                    return getClientStatisticsExpense(campaignIDs, from, to);
                } else {
                    throw exception;
                }
            }
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().url().toString(), response.code());
            return response.body().bytes();
        }
    }


    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/SubmitRequest">"/api/client/statistics"</>
     * <a href="https://docs.ozon.ru/api/performance/#operation/StatisticsCheck">"/api/client/statistics/{UUID}"</>
     * <a href="https://docs.ozon.ru/api/performance/#operation/DownloadStatistics">"/api/client/statistics/report"</>
     **/
    public CompletableFuture<StatisticResponse> getClientStatisticsFuture(StatisticRequest request) {
        WorkingThread thread = new WorkingThread(request);
        thread.start();
        return thread.getFuture();
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/StatisticsCheck">"/api/client/statistics/{UUID}"</>
     **/
    public StatisticStatus getClientStatisticStatus(UUID uuid) throws OzonApiException, IOException {
        Request.Builder request = new Request.Builder()
                .url(API_URL + "/api/client/statistics/" + uuid.toString());
        return getResponse(request, StatisticStatus.class);
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/AttributionSubmitRequest">"/api/client/statistics/attribution"</>
     **/
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
     **/
    public ItemList<ReportInfo> getClientStatisticsList(long page, long pageSize) throws OzonApiException, IOException {
        HttpUrl httpUtl = HttpUrl.parse(API_URL + "/api/client/statistics/list");
        if (httpUtl == null)
            throw new IllegalStateException("Can't parse http url " + API_URL + "/api/client/statistics/list");
        HttpUrl url = httpUtl.newBuilder()
                .addQueryParameter("page", Long.toString(page))
                .addQueryParameter("pageSize", Long.toString(pageSize))
                .build();
        return getResponse(new Request.Builder().url(url.url()).get(), new TypeReference<>() {
        });
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/DownloadStatistics">"/api/client/statistics/report"</>
     **/
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
