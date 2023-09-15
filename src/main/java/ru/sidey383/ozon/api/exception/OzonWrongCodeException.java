package ru.sidey383.ozon.api.exception;

import okhttp3.Response;

import java.io.IOException;

public class OzonWrongCodeException extends OzonApiException {

    private String body;

    private final Response response;

    private final int code;

    public OzonWrongCodeException(Response response) {
        this.code = response.code();
        this.response = response;
        try {
            if (response.body() == null)
                return;
            this.body = response.body().string();
        } catch (IOException ignored) {}
    }

    @Override
    public String getMessage() {
        return "Unexpected code " + code + " : " + (body == null ? response : body);
    }

    public String getBody() {
        return body;
    }

    public Response getResponse() {
        return response;
    }

    public int getCode() {
        return code;
    }
}
