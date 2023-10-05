package ru.sidey383.ozon.api.seller.objects.request.sub;

/**
 * @see ru.sidey383.ozon.api.seller.objects.request.AnalyticsDataRequest
 * **/
public record MetricsSorting(
        Metric key,
        SortingOrder order
) {
}
