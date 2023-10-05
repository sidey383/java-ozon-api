package ru.sidey383.ozon.api.seller.objects.request.sub;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see ru.sidey383.ozon.api.seller.objects.request.AnalyticsDataRequest
 * @see AnalyticsFilter
 * **/
public enum FilterOperation {
    @JsonProperty("EQ")
    EQUALS,
    @JsonProperty("GT")
    GREAT,
    @JsonProperty("GTE")
    GREAT_EQUALS,
    @JsonProperty("LT")
    LOWER,
    @JsonProperty("LTE")
    LOWER_EQUALS
}
