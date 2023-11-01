package ru.sidey383.ozon.api.seller.objects.request;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import ru.sidey383.ozon.api.container.ItemList;
import ru.sidey383.ozon.api.seller.JsonSellerAPIRequest;
import ru.sidey383.ozon.api.seller.objects.answer.product.DiscountedProductData;

import java.util.Arrays;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/ProductAPI_GetProductInfoDiscounted">API link</a>
 * **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInfoDiscountedRequest extends JsonSellerAPIRequest<ItemList<DiscountedProductData>> {

    @NotNull
    private final Long[] discountedSkus;

    public ProductInfoDiscountedRequest(@NotNull Long[] discountedSkus) {
        this.discountedSkus = discountedSkus;
    }

    @JsonGetter("discounted_skus")
    public Long[] getDiscountedSkus() {
        return discountedSkus;
    }

    @Override
    protected @NotNull String getURL() {
        return "/v1/product/info/discounted";
    }

    @Override
    public @NotNull TypeReference<ItemList<DiscountedProductData>> getTypeReference() {
        return new TypeReference<>() {};
    }

    @Override
    public String toString() {
        return "ProductInfoDiscountedRequest{" +
               "discountedSkus=" + Arrays.toString(discountedSkus) +
               '}';
    }
}
