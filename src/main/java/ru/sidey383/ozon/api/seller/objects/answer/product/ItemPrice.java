package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sidey383.ozon.api.seller.objects.answer.Currency;

public record ItemPrice(
        @JsonProperty("auto_action_enabled") boolean autoActionEnabled,
        @JsonProperty("currency_code") Currency currencyCode,
        @JsonProperty("marketing_price") String marketingPrice,
        @JsonProperty("marketing_seller_price") String marketingSellerPrice,
        @JsonProperty("min_ozon_price") String minOzonPrice,
        @JsonProperty("min_price") String minPrice,
        @JsonProperty("old_price") String oldPrice,
        @JsonProperty("premium_price") String premiumPrice,
        @JsonProperty("price") String price,
        @JsonProperty("recommended_price") String recommendedPrice,
        @JsonProperty("retail_price") String retailPrice,
        @JsonProperty("vat") String vat
) {
}
