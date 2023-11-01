package ru.sidey383.ozon.api.seller.objects.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.sidey383.ozon.api.container.ItemList;
import ru.sidey383.ozon.api.container.SingleResultContainer;
import ru.sidey383.ozon.api.seller.JsonSellerAPIRequest;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductInfoItem;

import java.util.Arrays;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/ProductAPI_GetProductInfoListV2">API link</a>
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInfoListRequest extends JsonSellerAPIRequest<SingleResultContainer<ItemList<ProductInfoItem>>> {

    @Nullable
    private final String[] offerID;

    @Nullable
    private final Long[] productID;

    @Nullable
    private final Long[] sku;

    public ProductInfoListRequest(@Nullable String[] offerID, @Nullable Long[] productID, @Nullable Long[] sku) {
        this.offerID = offerID;
        this.productID = productID;
        this.sku = sku;
    }

    @JsonGetter("offer_id")
    public String[] getOfferID() {
        return offerID;
    }

    @JsonGetter("product_id")
    public Long[] getProductID() {
        return productID;
    }

    @JsonGetter("sku")
    public Long[] getSku() {
        return sku;
    }

    @Override
    protected @NotNull String getURL() {
        return "/v2/product/info/list";
    }

    @Override
    public @NotNull TypeReference<SingleResultContainer<ItemList<ProductInfoItem>>> getTypeReference() {
        return new TypeReference<>() {
        };
    }

    @Override
    public String toString() {
        return "ProductInfoListRequest{" +
               "offerID=" + Arrays.toString(offerID) +
               ", productID=" + Arrays.toString(productID) +
               ", sku=" + Arrays.toString(sku) +
               '}';
    }
}
