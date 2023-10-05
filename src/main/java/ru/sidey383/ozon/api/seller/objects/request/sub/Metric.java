package ru.sidey383.ozon.api.seller.objects.request.sub;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see ru.sidey383.ozon.api.seller.objects.request.AnalyticsDataRequest
 * @see AnalyticsFilter
 * @see MetricsSorting
 * **/
public enum Metric {

    @JsonProperty("revenue")
    REVENUE(false),
    @JsonProperty("ordered_units")
    ORDERED_UNITS(false),
    @JsonProperty("unknown_metric")
    UNKNOWN_METRIC(true),
    @JsonProperty("hits_view_search")
    HITS_VIEW_SEARCH(true),
    @JsonProperty("hits_view_pdp")
    HITS_VIEW_PDP(true),
    @JsonProperty("hits_view")
    HITS_VIEW(true),
    @JsonProperty("hits_tocart_search")
    HITS_TOCART_SEARCH(true),
    @JsonProperty("hits_tocart_pdp")
    HITS_TOCART_PDP(true),
    @JsonProperty("hits_tocart")
    HITS_TOCART(true),
    @JsonProperty("session_view_search")
    SESSION_VIEW_SEARCH(true),
    @JsonProperty("session_view_pdp")
    SESSION_VIEW_PDP(true),
    @JsonProperty("session_view")
    SESSION_VIEW(true),
    @JsonProperty("conv_tocart_search")
    CONV_TOCART_SEARCH(true),
    @JsonProperty("conv_tocart_pdp")
    CONV_TOCART_PDP(true),
    @JsonProperty("conv_tocart")
    CONV_TOCART(true),
    @JsonProperty("returns")
    RETURNS(true),
    @JsonProperty("cancellations")
    CANCELLATIONS(true),
    @JsonProperty("delivered_units")
    DELIVERED_UNITS(true),
    @JsonProperty("position_category")
    POSITION_CATEGORY(true);

    private final boolean isPremium;

    Metric(boolean isPremium) {
        this.isPremium = isPremium;
    }

    public boolean isPremium() {
        return isPremium;
    }
}
