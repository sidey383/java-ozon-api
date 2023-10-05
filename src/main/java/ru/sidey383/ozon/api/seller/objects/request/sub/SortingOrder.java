package ru.sidey383.ozon.api.seller.objects.request.sub;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see ru.sidey383.ozon.api.seller.objects.request.FboListRequest
 * @see MetricsSorting
 * **/
public enum SortingOrder {

    @JsonProperty("ASC")
    ASCENDING(),
    @JsonProperty("DESC")
    DESCENDING()

}
