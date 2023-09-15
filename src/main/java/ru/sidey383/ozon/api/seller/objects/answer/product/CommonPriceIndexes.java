package ru.sidey383.ozon.api.seller.objects.answer.product;

public record CommonPriceIndexes(
        PriceIndexesData external_index_data,
        PriceIndexesData ozon_index_data,
        PriceIndex price_index,
        PriceIndexesSelfData self_marketplaces_index_data
) {

    public enum PriceIndex {
        WITHOUT_INDEX,
        PROFIT,
        AVG_PROFIT,
        NON_PROFIT
    }

}
