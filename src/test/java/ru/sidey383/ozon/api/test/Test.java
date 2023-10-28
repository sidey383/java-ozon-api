package ru.sidey383.ozon.api.test;

public class Test {

    @org.junit.jupiter.api.Test
    public void formatTest() {
        Long clientId = 123123L;
        String clientSecret = "1234";
        String str =  String.format("\"client_id\":%d,\"client_secret\":\"%s\", \"grant_type\":\"client_credentials\"", clientId, clientSecret);
        System.out.println(str);
    }

}
