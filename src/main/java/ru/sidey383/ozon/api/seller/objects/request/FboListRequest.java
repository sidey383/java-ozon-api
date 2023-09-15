package ru.sidey383.ozon.api.seller.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Period;
import java.time.temporal.TemporalAmount;

public record FboListRequest(
        Direction dir,
        DeliveryTimeFilter filter,
        long limit,
        long offset,
        boolean translit,
        With with) {

    public static final long REQUEST_LIMIT = 1000;

    public static final TemporalAmount MAX_PERIOD = Period.ofDays(90);

    public enum Direction {
        @JsonProperty("ASC")
        ASCENDING(),
        @JsonProperty("DESC")
        DESCENDING()
    }

    public record With(boolean analytics_data, boolean financial_data) {}

}
