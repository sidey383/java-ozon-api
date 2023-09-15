package ru.sidey383.ozon.api.performance.objects.request;

public record TokenRequest(String client_id, String client_secret, String grant_type) {
    public TokenRequest(String client_id, String client_secret) {
        this(client_id, client_secret, "client_credentials");
    }
}
