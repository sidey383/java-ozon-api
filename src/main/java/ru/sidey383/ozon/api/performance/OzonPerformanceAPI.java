package ru.sidey383.ozon.api.performance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.container.ItemList;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.exception.OzonExceptionFactory;
import ru.sidey383.ozon.api.performance.exception.OzonAPINoBodyException;
import ru.sidey383.ozon.api.performance.objects.answer.StatisticAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.StatisticResponse;
import ru.sidey383.ozon.api.performance.objects.answer.statistic.ReportInfo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class OzonPerformanceAPI {

    private final Logger logger = LoggerFactory.getLogger(OzonPerformanceAPI.class);

    private static final int STATUS_UPDATE_TIME = 3000;

    private final Object asyncRequestBlock = new Object();

    public static final String API_URL = "https://performance.ozon.ru";

    private final ObjectMapper mapper;

    private final OkHttpClient client;

    private final PerformanceAuth performanceAuth;

    private final AsyncRequestRunner asyncRequestRunner;

    public OzonPerformanceAPI(String clientId, String clientSecret) {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.client = new OkHttpClient();
        this.performanceAuth = new PerformanceAuth(client, clientId, clientSecret);
        this.asyncRequestRunner = new AsyncRequestRunner(client, performanceAuth);
    }

    public <A> A runRequest(PerformanceAPIRequest<A> request) throws OzonApiException, IOException {
        return request.runRequest(client, performanceAuth);
    }

    public CompletableFuture<StatisticResponse> runAsyncRequest(AsyncPerformanceAPIRequest request) {
        return asyncRequestRunner.addRequest(request);
    }

    public void stop() {
        asyncRequestRunner.stop();
    }

    public String getCampaignObjects(String campaignID) throws OzonApiException, IOException {
        Request request = new Request.Builder()
                .url(API_URL + "/api/client/campaign/" + campaignID + "  ")
                .header(performanceAuth.getAuthorizeHeaderName(), performanceAuth.getAuthorizationHeaderValue())
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw OzonExceptionFactory.onWrongCode(response);
            }
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().toString(), response.code());
            return response.body().string();
        }
    }

    public byte[] getClientStatisticsExpense(String[] campaignIDs, LocalDate from, LocalDate to) throws OzonApiException, IOException {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(API_URL + "/api/client/statistics/expense")).newBuilder();
        if (campaignIDs != null)
            for (String cmp : campaignIDs) {
                urlBuilder.addQueryParameter("campaignIds", cmp);
            }
        if (from != null)
            urlBuilder.addQueryParameter("dateFrom", from.toString());
        if (to != null)
            urlBuilder.addQueryParameter("dateTo", to.toString());

        Request request = new Request.Builder()
                .header(performanceAuth.getAuthorizeHeaderName(), performanceAuth.getAuthorizationHeaderValue())
                .url(urlBuilder.build().url()).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw OzonExceptionFactory.onWrongCode(response);
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().url().toString(), response.code());
            return response.body().bytes();
        }
    }

    public byte[] getClientStatisticsCampaignProduct(String[] campaignIDs, LocalDate from, LocalDate to) throws OzonApiException, IOException {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(API_URL + "/api/client/statistics/campaign/product")).newBuilder();
        if (campaignIDs != null)
            for (String cmp : campaignIDs) {
                urlBuilder.addQueryParameter("campaignIds", cmp);
            }
        if (from != null)
            urlBuilder.addQueryParameter("dateFrom", from.toString());
        if (to != null)
            urlBuilder.addQueryParameter("dateTo", to.toString());

        Request request = new Request.Builder()
                .header(performanceAuth.getAuthorizeHeaderName(), performanceAuth.getAuthorizationHeaderValue())
                .url(urlBuilder.build().url()).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw OzonExceptionFactory.onWrongCode(response);
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().url().toString(), response.code());
            return response.body().bytes();
        }
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/ListReports">"/api/client/statistics/list"</>
     **/
    public ItemList<ReportInfo> getClientStatisticsList(long page, long pageSize) throws OzonApiException, IOException {
        HttpUrl httpUtl = HttpUrl.parse(API_URL + "/api/client/statistics/list");
        if (httpUtl == null)
            throw new IllegalStateException("Can't parse http url " + API_URL + "/api/client/statistics/list");
        HttpUrl url = httpUtl.newBuilder()
                .addQueryParameter("page", Long.toString(page))
                .addQueryParameter("pageSize", Long.toString(pageSize))
                .build();
        return getResponse(new Request.Builder().url(url.url()).get(), new TypeReference<>() {
        });
    }

    private <T> T getResponse(Request.Builder requestBuilder, Class<T> type) throws OzonApiException, IOException {
        requestBuilder.header(performanceAuth.getAuthorizeHeaderName(), performanceAuth.getAuthorizationHeaderValue());
        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw OzonExceptionFactory.onWrongCode(response);
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().url().toString(), response.code());
            return mapper.readValue(response.body().string(), type);
        }
    }

    private <T> T getResponse(Request.Builder requestBuilder, TypeReference<T> type) throws OzonApiException, IOException {
        requestBuilder.header(performanceAuth.getAuthorizeHeaderName(), performanceAuth.getAuthorizationHeaderValue());
        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw OzonExceptionFactory.onWrongCode(response);
            if (response.body() == null)
                throw new OzonAPINoBodyException(request.url().toString(), response.code());
            return mapper.readValue(response.body().string(), type);
        }
    }

}
