package ru.sidey383.ozon.api.exception;

public abstract class OzonApiException extends Exception {

    public OzonApiException() {
        super();
    }

    public OzonApiException(String message) {
        super(message);
    }

    public OzonApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public OzonApiException(Throwable cause) {
        super(cause);
    }

    protected OzonApiException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
