package ru.sidey383.ozon.api.performance.exception;

public class TokenUpdateError extends RuntimeException {

    public TokenUpdateError(Throwable reason) {
        super("Token update error", reason);
    }

}
