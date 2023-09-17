package ru.sidey383.ozon.api.table.sku;

import java.time.LocalDate;

public record SkuReportEntry(
        LocalDate date,
        Long sku,
        String name,
        Double price,
        Integer showing,
        Integer clicks,
        Double ctr,
        Double avgClickPrice,
        Double cost,
        Integer orders,
        Double revenue,
        Integer modelOrders,
        Double modelRevenue
        ) {
}
