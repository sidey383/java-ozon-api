package ru.sidey383.ozon.api.table.sku;

import java.util.List;

public record SkuTableReport(List<SkuReportEntry> entries, SkuReportTotal total) {
}
