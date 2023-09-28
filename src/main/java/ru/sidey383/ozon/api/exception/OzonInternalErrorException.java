package ru.sidey383.ozon.api.exception;

import okhttp3.Response;

public class OzonInternalErrorException extends OzonWrongCodeException {

    OzonInternalErrorException(Response response) {
        super(response);
    }

    @Override
    public String getMessage() {
        return "Ozon server internal error " + code + " : " + (body == null ? "No body" : body);
    }

}
