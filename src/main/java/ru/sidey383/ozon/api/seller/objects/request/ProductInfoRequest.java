package ru.sidey383.ozon.api.seller.objects.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.container.SingleResultContainer;
import ru.sidey383.ozon.api.seller.JsonSellerAPIRequest;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductInfo;

import java.util.Objects;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/ProductAPI_GetProductInfoV2">API link</a>
 * **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInfoRequest extends JsonSellerAPIRequest<SingleResultContainer<ProductInfo>> {

    private static final TypeReference<SingleResultContainer<ProductInfo>> type = new TypeReference<>(){};

    private static final Logger logger = LoggerFactory.getLogger(ProductInfoRequest.class);

    private final String offerID;

    private final Long productID;

    private final Long sku;

    /**
     * Хотя бы один из параметров долен быть представлен
     * **/
    public ProductInfoRequest(@Nullable String offerId, @Nullable Long productId, @Nullable Long sku) throws IllegalArgumentException {
        if (Objects.isNull(offerId) && Objects.isNull(productId) && Objects.isNull(sku))
            throw new IllegalArgumentException("Wrong request arguments");
        offerID = offerId;
        productID = productId;
        this.sku = sku;
    }

    @JsonGetter("offer_id")
    public String getOfferID() {
        return offerID;
    }

    @JsonGetter("product_id")
    public Long getProductID() {
        return productID;
    }

    @JsonGetter("sku")
    public Long getSku() {
        return sku;
    }

    @Override
    protected @NotNull String getURL() {
        return "/v2/product/info";
    }

    @Override
    @JsonIgnore
    public @NotNull TypeReference<SingleResultContainer<ProductInfo>> getTypeReference() {
        return type;
    }

    @Override
    public String toString() {
        return "ProductInfoRequest{" +
                "offerID='" + offerID + '\'' +
                ", productID=" + productID +
                ", sku=" + sku +
                '}';
    }
}
