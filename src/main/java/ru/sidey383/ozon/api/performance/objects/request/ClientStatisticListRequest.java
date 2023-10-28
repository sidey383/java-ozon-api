package ru.sidey383.ozon.api.performance.objects.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.sidey383.ozon.api.container.ItemList;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.exception.OzonExceptionFactory;
import ru.sidey383.ozon.api.performance.PerformanceAPIRequest;
import ru.sidey383.ozon.api.performance.PerformanceAuth;
import ru.sidey383.ozon.api.performance.objects.answer.statistic.ReportInfo;

import java.io.IOException;
import java.util.Objects;

/**
 * <a href="https://docs.ozon.ru/api/performance/#operation/ListReports">"/api/client/statistics/list"</>
 **/
public class ClientStatisticListRequest implements PerformanceAPIRequest<ItemList<ReportInfo>> {

    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private final Long page;

    private final Long pageSize;

    public ClientStatisticListRequest(Long page, Long pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    @Override
    public ItemList<ReportInfo> runRequest(OkHttpClient client, PerformanceAuth auth) throws OzonApiException, IOException {
        HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse("https://performance.ozon.ru/api/client/statistics/list")).newBuilder();
        url.addQueryParameter("page", Long.toString(page));
        url.addQueryParameter("pageSize", Long.toString(pageSize));
        Request apiRequest =  new Request.Builder()
                .addHeader(auth.getAuthorizeHeaderName(), auth.getAuthorizationHeaderValue())
                .url(url.build().url())
                .get()
                .build();
        try (Response response = client.newCall(apiRequest).execute()) {
            if (!response.isSuccessful())
                throw OzonExceptionFactory.onWrongCode(response);
            assert response.body() != null;
            return mapper.readValue(response.body().bytes(), new TypeReference<>() {});
        }
    }
}
