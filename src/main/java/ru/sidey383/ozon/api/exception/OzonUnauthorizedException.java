package ru.sidey383.ozon.api.exception;

import okhttp3.Response;

public class OzonUnauthorizedException extends OzonWrongCodeException {

    OzonUnauthorizedException(Response response) {
        super(response);
    }

    @Override
    public String getMessage() {
        return "Unauthorized " + code + " : " + (body == null ? "No body" : body);
    }
}
