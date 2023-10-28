package ru.sidey383.ozon.api.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import okhttp3.*;
import ru.sidey383.ozon.api.exception.OzonExceptionFactory;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;
import ru.sidey383.ozon.api.performance.exception.TokenUpdateError;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class PerformanceAuth {

    private final ObjectMapper mapper = new ObjectMapper();

    private final OkHttpClient client;

    @Getter
    private final String clientId;

    private final String clientSecret;

    private TokenAnswer tokenAnswer;

    private Instant updateTime;

    public PerformanceAuth(OkHttpClient client, String clientId, String clientSecret) {
        this.client = client;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
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
       return ChronoUnit.SECONDS.between(
               updateTime,
               Instant.now(Clock.systemUTC())
       ) > (tokenAnswer.expires_in - 10);
    }

    private record TokenAnswer(String access_token, Long expires_in, String token_type) {}

    private synchronized void updateToken() throws IOException, OzonWrongCodeException {
        String json = String.format("{\"client_id\":\"%s\",\"client_secret\":\"%s\", \"grant_type\":\"client_credentials\"}", clientId, clientSecret);
        Request request = new Request.Builder()
                .url("https://performance.ozon.ru/api/client/token")
                .post(RequestBody
                        .create(
                                json,
                                MediaType.parse("application/json")
                        )).build();
        this.updateTime = Instant.now(Clock.systemUTC());
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw OzonExceptionFactory.onWrongCode(response);
            }
            assert response.body() != null;
            tokenAnswer = mapper.readValue(response.body().string(), TokenAnswer.class);
        }
    }

}
