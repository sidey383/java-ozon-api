package ru.sidey383.ozon.api.seller.objects.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.seller.objects.container.RowList;
import ru.sidey383.ozon.api.seller.objects.container.SingleResultContainer;
import ru.sidey383.ozon.api.seller.JsonSellerAPIRequest;
import ru.sidey383.ozon.api.seller.objects.answer.analystics.stock.StockOnWarehouseItem;
import ru.sidey383.ozon.api.seller.objects.request.sub.WarehouseType;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/AnalyticsAPI_AnalyticsGetStockOnWarehousesV2">API link</a>
 * **/
public class StockRequest extends JsonSellerAPIRequest<SingleResultContainer<RowList<StockOnWarehouseItem>>> {

    public final static int MAX_LIMIT = 1000;

    private static final TypeReference<SingleResultContainer<RowList<StockOnWarehouseItem>>> type = new TypeReference<>(){};

    private static final Logger logger = LoggerFactory.getLogger(StockRequest.class);

    private final long limit;
    private final long offset;
    @Nullable
    private final WarehouseType warehouseType;

    public StockRequest(long limit, long offset, @Nullable WarehouseType warehouseType) {
        this.limit = limit;
        this.offset = offset;
        this.warehouseType = warehouseType;
    }

    @JsonGetter("limit")
    public long getLimit() {
        return limit;
    }

    @JsonGetter("offset")
    public long getOffset() {
        return offset;
    }

    @JsonGetter("warehouse_type")
    public @Nullable WarehouseType getWarehouseType() {
        return warehouseType;
    }

    @Override
    protected @NotNull String getPath() {
        return "/v2/analytics/stock_on_warehouses";
    }

    @Override
    protected @NotNull Logger getLogger() {
        return logger;
    }

    @Override
    @JsonIgnore
    public @NotNull TypeReference<SingleResultContainer<RowList<StockOnWarehouseItem>>> getTypeReference() {
        return type;
    }

    @Override
    public String toString() {
        return "StockRequest{" +
                "limit=" + limit +
                ", offset=" + offset +
                ", warehouseType=" + warehouseType +
                '}';
    }
}
