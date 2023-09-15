package ru.sidey383.ozon.api.seller.objects.answer.analystics.data;

public record AnalyticsDataRow(
        AnalyticsDataRowDimension[] dimensions,
        double[] metrics
) {



}
