package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PriceIndexesData(
        @JsonProperty("minimal_price") String minimalPrice,
        @JsonProperty("minimal_price_currency") String minimalPriceCurrency,
        @JsonProperty("price_index_value") double priceIndexValue
) {
}
