package ru.sidey383.ozon.api.seller.objects.answer.product;

import java.util.List;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/ProductAPI_GetProductInfoPricesV4">API ref</a>
 * **/
public record ProductInfoPricesItems(List<ProductInfoPrices> items, String last_id, int total) {
}
