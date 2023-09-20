package ru.sidey383.ozon.api.performance.exception;

import ru.sidey383.ozon.api.exception.OzonApiException;

public class OzonAPINoBodyException extends OzonApiException {

    public OzonAPINoBodyException(String method, int code) {
        super(method + " Code:" + code);
    }

}
