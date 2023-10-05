package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostingProduct(
        @JsonProperty("digital_codes") String[] digitalCodes,
        @JsonProperty("name") String name,
        @JsonProperty("offer_id") String offerID,
        @JsonProperty("currency_code") String currencyCode,
        @JsonProperty("price") String price,
        @JsonProperty("quantity") long quantity,
        @JsonProperty("sku") long sku
) {
}
