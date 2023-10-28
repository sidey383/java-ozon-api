package ru.sidey383.ozon.api.seller;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;

import java.io.IOException;


public class OzonSellerAPI {

    private final String clientID;

    private final String apiKey;

    private final OkHttpClient client;

    public OzonSellerAPI(String clientID, String apiKey) {
        this.apiKey = apiKey;
        this.clientID = clientID;
        client = new OkHttpClient();
    }

    @NotNull
    public <A> A runRequest(JsonSellerAPIRequest<A> request) throws OzonWrongCodeException, IOException {
        return request.makeRequest(client, clientID, apiKey);
    }

}
