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
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;
import ru.sidey383.ozon.api.performance.objects.answer.StatisticAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.TokenAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaningAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaningState;
import ru.sidey383.ozon.api.performance.objects.answer.statistic.ReportInfo;
import ru.sidey383.ozon.api.performance.objects.answer.statistic.StatisticStatus;
import ru.sidey383.ozon.api.performance.objects.request.StatisticRequest;
import ru.sidey383.ozon.api.performance.objects.request.TokenRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class OzonPerformanceAPI {

    private final Logger logger = LoggerFactory.getLogger(OzonPerformanceAPI.class);

    public static final String API_URL = "https://performance.ozon.ru";

    private final ObjectMapper mapper;

    private final OkHttpClient client = new OkHttpClient();

    private final String clientId;

    private final String clientSecret;

    public OzonPerformanceAPI(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
                throw new OzonWrongCodeException(response);
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
    public AnswerList<CampaningAnswer> getClientCampanings(@Nullable String[] campaignId, @Nullable String type, @Nullable CampaningState state) throws OzonWrongCodeException, IOException {
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
    public StatisticAnswer getClientStatistics(StatisticRequest request) throws OzonWrongCodeException, IOException {
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
     * <a href="https://docs.ozon.ru/api/performance/#operation/StatisticsCheck">"/api/client/statistics/{UUID}"</>
     * **/
    public StatisticStatus getClientStatisticStatus(UUID uuid) throws OzonWrongCodeException, IOException {
        Request.Builder request = new Request.Builder()
                .url(API_URL + "/api/client/statistics/" + uuid.toString());
        return getResponse(request, StatisticStatus.class);
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/AttributionSubmitRequest">"/api/client/statistics/attribution"</>
     * **/
    public StatisticAnswer getClientStatisticsAttribution(StatisticRequest statisticAnswer) throws OzonWrongCodeException, IOException {
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
    public ItemList<ReportInfo> getClientStatisticsList(long page, long pageSize) throws OzonWrongCodeException, IOException {
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
    public Response getClientStatisticsReport(StatisticStatus status) throws OzonWrongCodeException, IOException {
        if (isTokenExpire())
            updateToken();
        Request request = new Request.Builder()
                .url(API_URL + status.link())
                .addHeader(token.getParamName(), token.getParamContent())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new OzonWrongCodeException(response);
        }
        return response;
    }

    private <T> T getResponse(Request.Builder request, Class<T> type) throws OzonWrongCodeException, IOException {
        if (isTokenExpire())
            updateToken();
        request.addHeader(token.getParamName(), token.getParamContent());
        try (Response response = client.newCall(request.build()).execute()) {
            if (!response.isSuccessful()) {
                throw new OzonWrongCodeException(response);
            }
            assert response.body() != null;
            return mapper.readValue(response.body().string(), type);
        }
    }

    private <T> T getResponse(Request.Builder request, TypeReference<T> type) throws OzonWrongCodeException, IOException {
        if (isTokenExpire())
            updateToken();
        request.addHeader(token.getParamName(), token.getParamContent());
        try (Response response = client.newCall(request.build()).execute()) {
            if (!response.isSuccessful()) {
                throw new OzonWrongCodeException(response);
            }
            assert response.body() != null;
            return mapper.readValue(response.body().string(), type);
        }
    }

}
