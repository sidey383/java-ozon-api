package ru.sidey383.ozon.api.seller.objects.answer.analystics.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public record AnalyticsDataRow(
        @JsonProperty("dimensions") AnalyticsDataRowDimension[] dimensions,
        @JsonProperty("metrics") double[] metrics
) {

    @Override
    public String toString() {
        return "AnalyticsDataRow{" +
               "dimensions=" + Arrays.toString(dimensions) +
               ", metrics=" + Arrays.toString(metrics) +
               '}';
    }
}
