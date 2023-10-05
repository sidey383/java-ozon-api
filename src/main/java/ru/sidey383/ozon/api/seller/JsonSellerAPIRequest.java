package ru.sidey383.ozon.api.seller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import ru.sidey383.ozon.api.exception.OzonExceptionFactory;
import ru.sidey383.ozon.api.exception.OzonRequestLimitException;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;

import java.io.IOException;

/**
 * Объект запроса используемый в {@link OzonSellerAPI}
 **/
public abstract class JsonSellerAPIRequest<A> {

    private static final String API_URL = "https://api-seller.ozon.ru";

    private static final ObjectMapper mapper;

    static  {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @NotNull
    @JsonIgnore
    protected abstract String getPath();

    @NotNull
    @JsonIgnore
    protected abstract Logger getLogger();

    @NotNull
    @JsonIgnore
    public abstract TypeReference<A> getTypeReference();

    final A makeRequest(OkHttpClient client, Request.Builder preparedRequest) throws OzonWrongCodeException, IOException {
        return getRequestData(client, preparedRequest, this);
    }

    private A getRequestData(OkHttpClient client, Request.Builder preparedRequest, Object requestJSONBody) throws OzonWrongCodeException, IOException {
        String json = mapper.writeValueAsString(requestJSONBody);
        Request apiRequest = preparedRequest
                .url(API_URL + getPath())
                .post(RequestBody
                        .create(
                                json,
                                MediaType.parse("application/json")
                        )).build();
        try (Response response = client.newCall(apiRequest).execute()) {
            if (!response.isSuccessful()) {
                OzonWrongCodeException e = OzonExceptionFactory.onWrongCode(response);
                if (e instanceof OzonRequestLimitException) {
                    getLogger().info("Request limit, response: {}", response);
                    try {
                        Thread.sleep(20000);
                        return getRequestData(client, preparedRequest, requestJSONBody);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    throw e;
                }
            }
            assert response.body() != null;
            return mapper.readValue(response.body().string(), getTypeReference());
        }
    }

    public abstract String toString();

}
