package ru.sidey383.ozon.api.seller.objects.answer.analystics.stock;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockOnWarehouseItem(
        @JsonProperty("sku") long sku,
        @JsonProperty("item_code") String itemCode,
        @JsonProperty("tem_name") String itemName,
        @JsonProperty("free_to_sell_amount") long freeToSellAmount,
        @JsonProperty("promised_amount") long promisedAmount,
        @JsonProperty("reserved_amount") long reservedAmount,
        @JsonProperty("warehouse_name") String warehouseName
) {
}
