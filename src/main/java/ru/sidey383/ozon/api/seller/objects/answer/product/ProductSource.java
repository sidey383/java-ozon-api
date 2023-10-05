package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductSource(
        @JsonProperty("is_enabled") boolean isEnabled,
        @JsonProperty("sku") Long sku,
        @JsonProperty("source") String source
) {
}
