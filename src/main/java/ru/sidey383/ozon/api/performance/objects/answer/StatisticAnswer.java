package ru.sidey383.ozon.api.performance.objects.answer;

import java.util.UUID;

public record StatisticAnswer(
        UUID uuid,
        boolean vendor
) {
}
