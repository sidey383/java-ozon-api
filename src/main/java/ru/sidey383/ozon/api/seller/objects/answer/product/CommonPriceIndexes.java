package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommonPriceIndexes(
        @JsonProperty("external_index_data") PriceIndexesData externalIndexData,
        @JsonProperty("ozon_index_data") PriceIndexesData ozonIndexData,
        @JsonProperty("price_index") PriceIndex priceIndex,
        @JsonProperty("self_marketplaces_index_data") PriceIndexesSelfData selfMarketplacesIndexData
) {
}
