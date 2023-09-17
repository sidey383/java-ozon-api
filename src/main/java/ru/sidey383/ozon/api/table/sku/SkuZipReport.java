package ru.sidey383.ozon.api.table.sku;

import java.util.List;

public record SkuZipReport(String name, String header, List<SkuReportEntry> entries, SkuReportTotal total) {
}