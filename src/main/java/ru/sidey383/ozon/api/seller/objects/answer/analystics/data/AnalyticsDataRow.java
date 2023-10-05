package ru.sidey383.ozon.api.seller.objects.answer.analystics.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnalyticsDataRow(
        @JsonProperty("dimensions") AnalyticsDataRowDimension[] dimensions,
        @JsonProperty("metrics") double[] metrics
) {
}
