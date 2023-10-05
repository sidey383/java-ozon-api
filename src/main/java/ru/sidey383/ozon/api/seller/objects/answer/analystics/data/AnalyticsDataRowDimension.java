package ru.sidey383.ozon.api.seller.objects.answer.analystics.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AnalyticsDataRowDimension(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name
) {
}
