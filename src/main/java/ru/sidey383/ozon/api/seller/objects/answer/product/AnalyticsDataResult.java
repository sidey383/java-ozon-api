package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sidey383.ozon.api.seller.objects.answer.analystics.data.AnalyticsDataRow;

import java.util.List;

public record AnalyticsDataResult(
        @JsonProperty("data") List<AnalyticsDataRow> data,
        @JsonProperty("totals") Double[] totals
) {
}
