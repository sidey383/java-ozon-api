package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"reasons"})
public record ProductVisibilityDetails(
        @JsonProperty("active_product") boolean activeProduct,
        @JsonProperty("has_price") boolean hasPrice,
        @JsonProperty("has_stock") boolean hasStock
) {
}
