package ru.sidey383.ozon.api.seller.objects.request.sub;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * @see ru.sidey383.ozon.api.seller.objects.request.ProductListRequest
 * @see ru.sidey383.ozon.api.seller.objects.request.ProductInfoPricesRequest
 * **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductFilter {

    @Nullable
    private final String[] offerIDs;
    @Nullable
    private final Long[] productIDs;
    @Nullable
    private final ProductVisibility visibility;

    public ProductFilter(@Nullable String[] offerId, @Nullable Long[] productId, @Nullable ProductVisibility visibility) {
        offerIDs = offerId;
        productIDs = productId;
        this.visibility = visibility;
    }

    @JsonGetter("offer_id")
    public @Nullable String[] getOfferIDs() {
        return offerIDs;
    }

    @JsonGetter("product_id")
    public @Nullable Long[] getProductIDs() {
        return productIDs;
    }

    @JsonGetter("visibility")
    public @Nullable ProductVisibility getVisibility() {
        return visibility;
    }

    @Override
    public String toString() {
        return "ProductFilter{" +
                "offerIDs=" + Arrays.toString(offerIDs) +
                ", productIDs=" + Arrays.toString(productIDs) +
                ", visibility=" + visibility +
                '}';
    }
}
