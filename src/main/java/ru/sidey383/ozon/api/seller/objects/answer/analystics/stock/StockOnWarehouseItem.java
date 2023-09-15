package ru.sidey383.ozon.api.seller.objects.answer.analystics.stock;

public record StockOnWarehouseItem(
        long sku,
        String item_code,
        String item_name,
        long free_to_sell_amount,
        long promised_amount,
        long reserved_amount,
        String warehouse_name
) {}
