package ru.sidey383.ozon.api.performance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.AnswerList;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;
import ru.sidey383.ozon.api.performance.objects.answer.TokenAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaningAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaningState;
import ru.sidey383.ozon.api.performance.objects.request.TokenRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class PerformanceAPI {

    private final Logger logger = LoggerFactory.getLogger(PerformanceAPI.class);

    public static final String API_URL = "https://performance.ozon.ru";

    private final ObjectMapper mapper;

    private final OkHttpClient client = new OkHttpClient();

    private final String clientId;

    private final String clientSecret;

    public PerformanceAPI(String clientId, String clientSecret) {
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
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new OzonWrongCodeException(response);
            }
            assert response.body() != null;
            TokenAnswer answer = mapper.readValue(response.body().string(), TokenAnswer.class);
            synchronized (this) {
                token = new Token(answer.access_token(), answer.expires_in(), updateTime);
            }
        }
    }

    public boolean isTokenExpire() {
        return token == null || token.isExpire();
    }

    /**
     * <a href="https://performance.ozon.ru:443/api/client/campaign">/api/client/campaign</a>
     */
    public AnswerList<CampaningAnswer> getClientCampanings(@Nullable String[] campaignId, @Nullable String type, @Nullable CampaningState state) throws OzonWrongCodeException, IOException {
        if (isTokenExpire())
            updateToken();
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
        request.addHeader(token.getParamName(), token.getParamContent());
        return getResponse(request.build(), new TypeReference<>() {});
    }

    private <T> T getResponse(Request request, Class<T> type) throws OzonWrongCodeException, IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new OzonWrongCodeException(response);
            }
            assert response.body() != null;
            return mapper.readValue(response.body().string(), type);
        }
    }

    private <T> T getResponse(Request request, TypeReference<T> type) throws OzonWrongCodeException, IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new OzonWrongCodeException(response);
            }
            assert response.body() != null;
            return mapper.readValue(response.body().string(), type);
        }
    }

}
