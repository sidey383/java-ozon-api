package ru.sidey383.ozon.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import ru.sidey383.ozon.api.exception.OzonExceptionFactory;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Map;

public abstract class BaseRequest {

    protected static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    protected byte @NotNull [] executeRequest(OkHttpClient client, Request request) throws OzonWrongCodeException, IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw OzonExceptionFactory.onWrongCode(response);
            assert response.body() != null;
            return response.body().bytes();
        }
    }
}
