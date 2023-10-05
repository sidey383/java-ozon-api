package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/ProductAPI_GetProductInfoPricesV4">API ref</a>
 **/
public record ProductInfoPrices(
        @JsonProperty("acquiring") int acquiring,
        @JsonProperty("commissions") ItemCommissions commissions,
        @JsonProperty("marketing_actions") ItemMarketingActions marketingActions,
        @JsonProperty("offer_id") String offerID,
        @JsonProperty("price") ItemPrice price,
        @JsonProperty("price_index") String priceIndex,
        @JsonProperty("price_indexes") CommonPriceIndexes priceIndexes,
        @JsonProperty("product_id") long productID,
        @JsonProperty("volume_weight") double volumeWeight
) {
}
