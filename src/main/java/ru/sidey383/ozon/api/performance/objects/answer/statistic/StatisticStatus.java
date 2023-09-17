package ru.sidey383.ozon.api.performance.objects.answer.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.sidey383.ozon.api.performance.objects.request.StatisticRequest;

import java.time.Instant;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StatisticStatus (
        UUID uuid,
        StatisticsRequestState state,
        Instant createdAt,
        Instant updatedAt,
        StatisticRequest request,
        String error,
        String link,
        Kind kind

) {

    public enum Kind {
        STATS,
        SEARCH_PHRASES,
        ATTRIBUTION,
        VIDEO
    }

}
