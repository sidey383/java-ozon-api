package ru.sidey383.ozon.api.seller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import ru.sidey383.ozon.api.BaseRequest;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;

import java.io.IOException;

/**
 * Объект запроса используемый в {@link OzonSellerAPI}
 **/
public abstract class JsonSellerAPIRequest<A> extends BaseRequest {

    private static final String API_URL = "https://api-seller.ozon.ru";

    private static final ObjectMapper mapper;

    static  {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @NotNull
    @JsonIgnore
    protected abstract String getURL();

    @NotNull
    @JsonIgnore
    public abstract TypeReference<A> getTypeReference();

    protected A makeRequest(OkHttpClient client, String clientId, String apiKey) throws OzonWrongCodeException, IOException {
        return getRequestData(client, clientId, apiKey, this);
    }

    protected A getRequestData(OkHttpClient client, String clientId, String apiKey, Object requestJSONBody) throws OzonWrongCodeException, IOException {
        String json = mapper.writeValueAsString(requestJSONBody);
        Request apiRequest =  new Request.Builder()
                .addHeader("Client-Id", clientId)
                .addHeader("Api-Key", apiKey)
                .url(API_URL + getURL())
                .post(RequestBody
                        .create(
                                json,
                                MediaType.parse("application/json")
                        )).build();
        return mapper.readValue(executeRequest(client, apiRequest), getTypeReference());
    }

    public abstract String toString();

}
