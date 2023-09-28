package ru.sidey383.ozon.api.exception;

import okhttp3.Response;

public class OzonExceptionFactory {

    public static OzonWrongCodeException onWrongCode(Response response) {
        return switch (response.code()) {
            case 401 -> new OzonUnauthorizedException(response);
            case 500 -> new OzonInternalErrorException(response);
            case 429 -> new OzonRequestLimitException(response);
            case 200 -> throw  new IllegalArgumentException("Ok request");
            default -> new OzonWrongCodeException(response);
        };
    }

}
