package ru.sidey383.ozon.api.performance;

import okhttp3.*;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.performance.objects.answer.StatisticResponse;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class OzonPerformanceAPI {

    private final OkHttpClient client;

    private final PerformanceAuth performanceAuth;

    private final AsyncRequestRunner asyncRequestRunner;

    public OzonPerformanceAPI(String clientId, String clientSecret) {
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

}
