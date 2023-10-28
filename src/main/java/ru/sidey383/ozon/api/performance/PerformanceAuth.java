package ru.sidey383.ozon.api.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import ru.sidey383.ozon.api.exception.OzonExceptionFactory;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;
import ru.sidey383.ozon.api.performance.exception.TokenUpdateError;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class PerformanceAuth {

    private final ObjectMapper mapper = new ObjectMapper();

    private final OkHttpClient client;

    private final String clientId;

    private final String clientSecret;

    private TokenAnswer tokenAnswer;

    private Instant updateTime;

    public PerformanceAuth(OkHttpClient client, String clientId, String clientSecret) {
        this.client = client;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getKey() throws TokenUpdateError {
        updateIfExpire();
        return tokenAnswer.access_token;
    }

    public String getAuthorizeHeaderName() {
        return "Authorization";
    }

    public String getAuthorizationHeaderValue() {
        return "Bearer " + getKey();
    }

    public synchronized void updateIfExpire() throws TokenUpdateError {
        if (isExpire()) {
            try {
                updateToken();
            } catch (IOException | OzonWrongCodeException e) {
                throw new TokenUpdateError(e);
            }
        }
    }

    public synchronized boolean isExpire() {
        if (tokenAnswer == null)
            return true;
       return Duration.between(
               updateTime,
               Instant.now(Clock.systemUTC())
       ).getSeconds() > (tokenAnswer.expires_in - 10);
    }

    private record TokenAnswer(String access_token, Long expires_in, String token_type) {}

    private synchronized void updateToken() throws IOException, OzonWrongCodeException {
        Instant updateTime = Instant.now();
        String json = String.format("\"client_id\":%s,\"client_secret\":\"%s\", \"grant_type\":\"client_credentials\"", clientId, clientSecret);
        Request request = new Request.Builder()
                .url("https://performance.ozon.ru/api/client/token")
                .post(RequestBody
                        .create(
                                json,
                                MediaType.parse("application/json")
                        )).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw OzonExceptionFactory.onWrongCode(response);
            }
            assert response.body() != null;
            tokenAnswer = mapper.readValue(response.body().string(), TokenAnswer.class);
        }
    }

}
