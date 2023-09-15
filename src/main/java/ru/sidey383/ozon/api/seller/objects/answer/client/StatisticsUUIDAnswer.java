package ru.sidey383.ozon.api.seller.objects.answer.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatisticsUUIDAnswer(
        @JsonProperty("UUID")
        java.util.UUID uuid,
        boolean vendor) {
}
