package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductCommissions(
        @JsonProperty("delivery_amount") Double deliveryAmount,
        @JsonProperty("min_value") Double minValue,
        @JsonProperty("percent") Double percent,
        @JsonProperty("return_amount") Double returnAmount,
        @JsonProperty("sale_schema") String saleSchema,
        @JsonProperty("value") Double value
) {
}
