package ru.sidey383.ozon.api.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.exception.OzonExceptionFactory;
import ru.sidey383.ozon.api.performance.exception.OzonAPINoBodyException;
import ru.sidey383.ozon.api.performance.objects.answer.StatisticAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.StatisticResponse;
import ru.sidey383.ozon.api.performance.objects.answer.statistic.StatisticStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AsyncRequestRunner {

    private static final Logger logger = LoggerFactory.getLogger(AsyncRequestRunner.class);

    public static final String API_URL = "https://performance.ozon.ru";

    private final ObjectMapper mapper;

    private final OkHttpClient client;

    private final int updateTime;

    private final List<WorkingThread> workingThreads = Collections.synchronizedList(new ArrayList<>());

    private final PerformanceAuth performanceAuth;

    protected AsyncRequestRunner(OkHttpClient client, PerformanceAuth performanceAuth) {
        this(client, performanceAuth, 3000);
    }

    protected AsyncRequestRunner(OkHttpClient client, PerformanceAuth performanceAuth, int updateTime) {
        this.client = client;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.performanceAuth = performanceAuth;
        this.updateTime = updateTime;
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/SubmitRequest">"/api/client/statistics"</>
     * <a href="https://docs.ozon.ru/api/performance/#operation/StatisticsCheck">"/api/client/statistics/{UUID}"</>
     * <a href="https://docs.ozon.ru/api/performance/#operation/DownloadStatistics">"/api/client/statistics/report"</>
     **/
    public CompletableFuture<StatisticResponse> addRequest(AsyncPerformanceAPIRequest initiator) {
        WorkingThread thread = new WorkingThread(initiator);
        thread.start();
        return thread.getFuture();
    }

    public void stop() {
        workingThreads.forEach(Thread::interrupt);
    }

    private class WorkingThread extends Thread {

        private final AsyncPerformanceAPIRequest  initiator;

        private final CompletableFuture<StatisticResponse> future;

        private WorkingThread(AsyncPerformanceAPIRequest  initiator) {
            this.initiator = initiator;
            this.future = new CompletableFuture<>();
        }

        @Override
        @SuppressWarnings("BusyWait")
        public void run() {
            workingThreads.add(this);
            StatisticAnswer answer = null;
            StatisticStatus status = null;
            try {
                synchronized (this) {
                    answer = initiator.runRequest(client, performanceAuth);
                    logger.debug("Create statistic request for " + performanceAuth.getClientId() + ": " + answer);
                    do {
                        status = getClientStatisticStatus(answer.uuid());
                        logger.debug("Receive statistic request status for " + performanceAuth.getClientId() + ": " + status);
                        if (!status.state().isTerminate()) {
                            Thread.sleep(updateTime);
                        }
                    } while (!status.state().isTerminate());
                }
                byte[] data = getClientStatisticsReport(status);
                logger.debug("Complete statistic request status for " + performanceAuth.getClientId() + ": " + status);
                future.complete(new StatisticResponse(null, status, data));
            } catch (InterruptedException e) {
                this.interrupt();
                logger.warn("Statistic request interrupted for " + performanceAuth.getClientId() + ": " + answer + " " + status);
                future.complete(new StatisticResponse(e, status, null));
            } catch (Throwable th) {
                logger.error("Can't complete statistic request for " + performanceAuth.getClientId() + ": " + answer + " " + status, th);
                future.complete(new StatisticResponse(th, status, null));
            } finally {
                workingThreads.remove(this);
            }
        }

        public CompletableFuture<StatisticResponse> getFuture() {
            return future;
        }

    }

    private StatisticStatus getClientStatisticStatus(UUID uuid) throws OzonApiException, IOException {
        Request.Builder request = new Request.Builder()
                .url(API_URL + "/api/client/statistics/" + uuid.toString());
        return getResponse(request, StatisticStatus.class);
    }

    /**
     * <a href="https://docs.ozon.ru/api/performance/#operation/DownloadStatistics">"/api/client/statistics/report"</>
     **/
    private byte[] getClientStatisticsReport(StatisticStatus status) throws OzonApiException, IOException {
        Request request = new Request.Builder()
                .url(API_URL + status.link())
                .header(performanceAuth.getAuthorizeHeaderName(), performanceAuth.getAuthorizationHeaderValue())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw OzonExceptionFactory.onWrongCode(response);
        if (response.body() == null)
            throw new OzonAPINoBodyException(request.url().toString(), response.code());
        return response.body().bytes();
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

}
