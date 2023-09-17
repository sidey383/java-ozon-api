package ru.sidey383.ozon.api.table.sku;

public record SkuReportTotal(
        Integer showing,
        Integer clicks,
        Double ctr,
        Double avgClientPrice,
        Double cost,
        Integer orders,
        Double revenue,
        Integer modelOrders,
        Double modelRevenue
) {
}
