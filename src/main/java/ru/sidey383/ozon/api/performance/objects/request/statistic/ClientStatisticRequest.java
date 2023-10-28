package ru.sidey383.ozon.api.performance.objects.request.statistic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.exception.OzonExceptionFactory;
import ru.sidey383.ozon.api.performance.AsyncPerformanceAPIRequest;
import ru.sidey383.ozon.api.performance.PerformanceAuth;
import ru.sidey383.ozon.api.performance.exception.OzonAPINoBodyException;
import ru.sidey383.ozon.api.performance.objects.answer.StatisticAnswer;

import java.io.IOException;

public class ClientStatisticRequest extends AsyncPerformanceAPIRequest {

    protected static final String url = "https://performance.ozon.ru/api/client";

    protected static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private final StatisticRequestData requestObject;

    private final String subPath;

    ClientStatisticRequest(StatisticRequestData object, String subPath) {
        this.requestObject = object;
        this.subPath = subPath;
    }

    @Override
    public StatisticAnswer runRequest(OkHttpClient client, PerformanceAuth auth) throws OzonApiException, IOException {
        String json = mapper.writeValueAsString(requestObject);
        Request request = new Request.Builder()
                .url(url + (subPath.isEmpty() ? "" : "/") + subPath)
                .post(RequestBody
                        .create(
                                json,
                                MediaType.parse("application/json")
                        ))
                .header(auth.getAuthorizeHeaderName(), auth.getAuthorizationHeaderValue())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw OzonExceptionFactory.onWrongCode(response);
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().url().toString(), response.code());
            return mapper.readValue(response.body().string(), new TypeReference<>() {});
        }
    }

}
