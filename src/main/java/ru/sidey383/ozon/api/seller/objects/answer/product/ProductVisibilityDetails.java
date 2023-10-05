package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductVisibilityDetails(
        @JsonProperty("active_product") boolean activeProduct,
        @JsonProperty("has_price") boolean hasPrice,
        @JsonProperty("has_stock") boolean hasStock
) {
}
