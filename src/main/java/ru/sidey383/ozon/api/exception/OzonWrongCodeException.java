package ru.sidey383.ozon.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;

import java.io.IOException;

public class OzonWrongCodeException extends OzonApiException {

    protected String body;

    protected final int code;

    private final static ObjectMapper mapper = new ObjectMapper();

    OzonWrongCodeException(Response response) {
        this.code = response.code();
        if (response.body() != null) {
            try {
                this.body = response.body().string();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public String getMessage() {
        return "Unexpected code " + code + " : " + (body == null ? "No body" : body);
    }

    public String getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }
}
