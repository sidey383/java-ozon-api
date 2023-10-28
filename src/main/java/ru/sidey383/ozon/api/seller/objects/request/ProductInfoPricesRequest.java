package ru.sidey383.ozon.api.seller.objects.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.container.PageableItemList;
import ru.sidey383.ozon.api.container.SingleResultContainer;
import ru.sidey383.ozon.api.seller.JsonSellerAPIRequest;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductInfoPrices;
import ru.sidey383.ozon.api.seller.objects.request.sub.ProductFilter;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/ProductAPI_GetProductInfoPricesV4">API link</a>
 * **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInfoPricesRequest extends JsonSellerAPIRequest<SingleResultContainer<PageableItemList<ProductInfoPrices>>> {

    public static final long MAX_LIMIT = 1000;

    private static final TypeReference<SingleResultContainer<PageableItemList<ProductInfoPrices>>> type = new TypeReference<>(){};

    private static final Logger logger = LoggerFactory.getLogger(ProductInfoPricesRequest.class);

    @NotNull
    private final ProductFilter filter;
    @Nullable
    private final String lastID;
    private final long limit;

    public ProductInfoPricesRequest(@NotNull ProductFilter filter, @Nullable String lastId, long limit) {
        this.filter = filter;
        this.lastID = lastId;
        this.limit = limit;
    }

    @JsonGetter("filter")
    public @NotNull ProductFilter getFilter() {
        return filter;
    }

    @JsonGetter("last_id")
    public @Nullable String getLastID() {
        return lastID;
    }

    @JsonGetter("limit")
    public long getLimit() {
        return limit;
    }

    @Override
    @JsonIgnore
    protected @NotNull String getURL() {
        return "/v4/product/info/prices";
    }

    @Override
    @JsonIgnore
    public @NotNull TypeReference<SingleResultContainer<PageableItemList<ProductInfoPrices>>> getTypeReference() {
        return type;
    }

    @Override
    public String toString() {
        return "ProductInfoPricesRequest{" +
                "filter=" + filter +
                ", lastID='" + lastID + '\'' +
                ", limit=" + limit +
                '}';
    }
}
