package ru.sidey383.ozon.api.seller.objects.answer.analystics.data;

public record AnalyticsGetDataResponseResult(
        AnalyticsDataRow[] data,
        double[] totals
) {
}
