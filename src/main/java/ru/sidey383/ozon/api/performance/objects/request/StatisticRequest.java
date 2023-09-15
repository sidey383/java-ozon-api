package ru.sidey383.ozon.api.performance.objects.request;

import java.time.Instant;
import java.time.LocalDate;

public record StatisticRequest(
        String[] campaigns,
        Instant from,
        Instant to,
        LocalDate dateFrom,
        LocalDate dateTo,
        GroupBy groupBy

) {

    public enum GroupBy {
        NO_GROUP_BY,
        DATE,
        START_OF_WEEK,
        START_OF_MONTH
    }
}
