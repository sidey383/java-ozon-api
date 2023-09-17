package ru.sidey383.ozon.api.table.sku;

import java.util.List;

public interface SkuReport {
    List<SkuReportEntry> entries();
    SkuReportTotal total();

}
