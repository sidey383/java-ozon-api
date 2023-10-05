package ru.sidey383.ozon.api.seller.objects.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.seller.objects.container.PageableItemList;
import ru.sidey383.ozon.api.seller.objects.container.SingleResultContainer;
import ru.sidey383.ozon.api.seller.JsonSellerAPIRequest;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductDescription;
import ru.sidey383.ozon.api.seller.objects.request.sub.ProductFilter;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/ProductAPI_GetProductList">API link</a>
 * **/
public class ProductListRequest extends JsonSellerAPIRequest<SingleResultContainer<PageableItemList<ProductDescription>>> {

    public static final int MAX_LIMIT = 1000;

    private static final TypeReference<SingleResultContainer<PageableItemList<ProductDescription>>> type = new TypeReference<>(){};

    private static final Logger logger = LoggerFactory.getLogger(ProductListRequest.class);

    @Nullable
    private final ProductFilter filter;
    @Nullable
    private final String lastID;
    private final int limit;

    public ProductListRequest(@Nullable ProductFilter filter, @Nullable String lastID, int limit) {
        this.filter = filter;
        this.lastID = lastID;
        this.limit = limit;
    }

    @JsonGetter("filter")
    public @Nullable ProductFilter getFilter() {
        return filter;
    }

    @JsonGetter("last_id")
    public @Nullable String getLastID() {
        return lastID;
    }

    @JsonGetter("limit")
    public int getLimit() {
        return limit;
    }

    @Override
    @JsonIgnore
    protected @NotNull String getPath() {
        return "/v2/product/list";
    }

    @Override
    @JsonIgnore
    protected @NotNull Logger getLogger() {
        return logger;
    }

    @Override
    @JsonIgnore
    public @NotNull TypeReference<SingleResultContainer<PageableItemList<ProductDescription>>> getTypeReference() {
        return type;
    }

    @Override
    public String toString() {
        return "ProductListRequest{" +
                "filter=" + filter +
                ", lastID='" + lastID + '\'' +
                ", limit=" + limit +
                '}';
    }
}
