package ru.sidey383.ozon.api.performance;

import okhttp3.OkHttpClient;
import ru.sidey383.ozon.api.exception.OzonApiException;

import java.io.IOException;

public interface PerformanceAPIRequest<A> {

    A runRequest(OkHttpClient client, PerformanceAuth auth) throws OzonApiException, IOException;

}
