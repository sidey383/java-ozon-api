package ru.sidey383.ozon.api.exception;

import okhttp3.Response;

public class OzonRequestLimitException extends OzonWrongCodeException {

    OzonRequestLimitException(Response response) {
        super(response);
    }

    @Override
    public String getMessage() {
        return "Request limit exceeded " + code + " : " + (body == null ? "No body" : body);
    }

}
