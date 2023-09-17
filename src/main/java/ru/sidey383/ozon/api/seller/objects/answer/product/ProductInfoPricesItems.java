package ru.sidey383.ozon.api.seller.objects.answer.product;

import ru.sidey383.ozon.api.seller.objects.request.ProductInfoPricesRequest;

import java.util.List;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/ProductAPI_GetProductInfoPricesV4">API ref</a>
 * **/
public record ProductInfoPricesItems(List<ProductInfoPrices> items, String last_id, int total) {

    public ProductInfoPricesRequest getNext(ProductInfoPricesRequest old) {
        return new ProductInfoPricesRequest(old.filter(), last_id, old.limit());
    }

}
