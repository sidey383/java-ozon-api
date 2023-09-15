package ru.sidey383.ozon.api.seller.objects.answer.product;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/ProductAPI_GetProductInfoPricesV4">API ref</a>
 * **/
public record ProductInfoPrices(
        int acquiring,
        ItemCommissions commissions,
        ItemMarketingActions marketing_actions,
        String offer_id,
        ItemPrice price,
        String price_index,
        CommonPriceIndexes price_indexes,
        long product_id,
        double volume_weight
) {
}
