package ru.sidey383.ozon.api.performance;

import okhttp3.OkHttpClient;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.performance.objects.answer.StatisticAnswer;

import java.io.IOException;

public abstract class AsyncPerformanceAPIRequest {

    protected abstract StatisticAnswer runRequest(OkHttpClient client, PerformanceAuth auth) throws OzonApiException, IOException;

}
