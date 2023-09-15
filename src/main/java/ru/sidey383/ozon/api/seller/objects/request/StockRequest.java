package ru.sidey383.ozon.api.seller.objects.request;

public record StockRequest(long limit, long offset, WarehouseType warehouse_type) {

    public final static int REQUEST_LIMIT = 1000;

    public enum WarehouseType {
        EXPRESS_DARK_STORE, NOT_EXPRESS_DARK_STORE, ALL
    }

}
