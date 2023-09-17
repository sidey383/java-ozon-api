package ru.sidey383.ozon.api.performance.objects.answer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record StatisticAnswer(
        @JsonProperty("UUID")
        UUID uuid,
        boolean vendor
) {
}
