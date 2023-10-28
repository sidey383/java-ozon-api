package ru.sidey383.ozon.api.performance.objects.answer;

import ru.sidey383.ozon.api.performance.objects.answer.statistic.StatisticStatus;

public record StatisticResponse(Throwable exception, StatisticStatus status, byte[] data) {
}