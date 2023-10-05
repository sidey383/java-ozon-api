package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductDescription(
        @JsonProperty("offer_id") String offerID,
        @JsonProperty("product_id") Long productID,
        @JsonProperty("is_fbo_visible") boolean isFboVisible,
        @JsonProperty("is_fbs_visible") boolean isFbsVisible,
        @JsonProperty("archived") boolean archived,
        @JsonProperty("is_discounted") boolean isDiscounted
) {
}
