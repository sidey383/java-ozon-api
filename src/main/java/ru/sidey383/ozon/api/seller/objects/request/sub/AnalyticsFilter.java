package ru.sidey383.ozon.api.seller.objects.request.sub;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @see ru.sidey383.ozon.api.seller.objects.request.AnalyticsDataRequest
 * **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnalyticsFilter(
        Metric key, //sort parameter
        FilterOperation op, // equals operator
        String value
) {}
