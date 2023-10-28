package ru.sidey383.ozon.api.performance.objects.request.statistic.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import ru.sidey383.ozon.api.BaseRequest;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.performance.PerformanceAPIRequest;
import ru.sidey383.ozon.api.performance.PerformanceAuth;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public abstract class ClientStatisticRequest extends BaseRequest implements PerformanceAPIRequest<byte[]> {

    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public abstract String getType();

    protected abstract Collection<ParamPair> queryParameters();

    protected record ParamPair(String key, String value) {
    }

    @Override
    public byte[] runRequest(OkHttpClient client, PerformanceAuth auth) throws OzonApiException, IOException {
        HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse("https://performance.ozon.ru/api/client/statistics/campaign/" + getType())).newBuilder();
        queryParameters().forEach((p) -> url.addQueryParameter(p.key(), p.value()));
        Request apiRequest =  new Request.Builder()
                .addHeader(auth.getAuthorizeHeaderName(), auth.getAuthorizationHeaderValue())
                .url(url.build().url())
                .get()
                .build();
        return  executeRequest(client, apiRequest);
    }

}
