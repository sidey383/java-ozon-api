package ru.sidey383.ozon.api.table.sku;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SkuReportParser {

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final CSVFormat csvFormat = CSVFormat.Builder.create()
            .setDelimiter(";")
            .setQuote('"')
            .setRecordSeparator("\n\r")
            .setIgnoreEmptyLines(true)
            .setHeader()
            .build();

    public List<SkuZipReport> readZipReports(InputStream is) throws IOException {
        List<SkuZipReport> reports = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(is)) { //TODO: fix invalid block length
            ZipEntry zipEntry;
            top:
            while ((zipEntry = zis.getNextEntry()) != null) {
                CSVParser parser = new CSVParser(new InputStreamReader(is), csvFormat);
                List<SkuReportEntry> entries = new ArrayList<>();
                Iterator<CSVRecord> iterator = parser.iterator();
                CSVRecord superHearRecord = iterator.next();
                if (!superHearRecord.get(0).isEmpty()) {
                    reports.add(null);
                    zis.closeEntry();
                    continue;
                }
                String header = superHearRecord.get(1);
                CSVRecord headerRecord = iterator.next();
                for (int i = 0; i < Header.values().length; i++) {
                    String field = headerRecord.get(i);
                    if (!Objects.equals(field, Header.values()[i].name)) {
                        reports.add(null);
                        zis.closeEntry();
                        continue top;
                    }
                }
                SkuReportTotal total = null;
                while (iterator.hasNext()) {
                    CSVRecord r = iterator.next();
                    if (r.get(0).equals("Всего")) {
                        total = new SkuReportTotal(
                                Integer.parseInt(r.get(4)),
                                Integer.parseInt(r.get(5)),
                                parseDouble(r.get(6)),
                                parseDouble(r.get(7)),
                                parseDouble(r.get(8)),
                                Integer.parseInt(r.get(9)),
                                parseDouble(r.get(10)),
                                Integer.parseInt(r.get(11)),
                                parseDouble(r.get(12))
                        );
                        continue;
                    }
                    LocalDate date = LocalDate.from(dateFormatter.parse(r.get(0)));
                    entries.add(
                            new SkuReportEntry(
                                    date,
                                    Long.getLong(r.get(1)),
                                    r.get(2),
                                    parseDouble(r.get(3)),
                                    Integer.parseInt(r.get(4)),
                                    Integer.parseInt(r.get(5)),
                                    parseDouble(r.get(6)),
                                    parseDouble(r.get(7)),
                                    parseDouble(r.get(8)),
                                    Integer.parseInt(r.get(9)),
                                    parseDouble(r.get(10)),
                                    Integer.parseInt(r.get(11)),
                                    parseDouble(r.get(12))
                            )
                    );
                }
                reports.add(new SkuZipReport(zipEntry.getName(), header, entries, total));
                zis.closeEntry();
            }
        }
        return reports;
    }

    public SkuTableReport readTableReport(InputStream is) throws IOException {
        CSVParser parser = new CSVParser(new InputStreamReader(is), csvFormat);
        List<SkuReportEntry> entries = new ArrayList<>();
        Iterator<CSVRecord> iterator = parser.iterator();
        CSVRecord headerRecord = iterator.next();
        for (int i = 0; i < Header.values().length; i++) {
            String field = headerRecord.get(i);
            if (!Objects.equals(field, Header.values()[i].name)) {
                throw new WrongFormatException("Wrong header " + headerRecord);
            }
        }
        SkuReportTotal total = null;
        while (iterator.hasNext()) {
            CSVRecord r = iterator.next();
            if (r.get(0).equals("Всего")) {
                total = new SkuReportTotal(
                        Integer.parseInt(r.get(4)),
                        Integer.parseInt(r.get(5)),
                        parseDouble(r.get(6)),
                        parseDouble(r.get(7)),
                        parseDouble(r.get(8)),
                        Integer.parseInt(r.get(9)),
                        parseDouble(r.get(10)),
                        Integer.parseInt(r.get(11)),
                        parseDouble(r.get(12))
                );
                continue;
            }
            LocalDate date = LocalDate.from(dateFormatter.parse(r.get(0)));
            entries.add(
                    new SkuReportEntry(
                            date,
                            Long.getLong(r.get(1)),
                            r.get(2),
                            parseDouble(r.get(3)),
                            Integer.parseInt(r.get(4)),
                            Integer.parseInt(r.get(5)),
                            parseDouble(r.get(6)),
                            parseDouble(r.get(7)),
                            parseDouble(r.get(8)),
                            Integer.parseInt(r.get(9)),
                            parseDouble(r.get(10)),
                            Integer.parseInt(r.get(11)),
                            parseDouble(r.get(12))
                    )
            );
        }
        return new SkuTableReport(entries, total);
    }

    public SkuTableReport readTableReport(boolean hasFileHeader, InputStream is) throws IOException {
        CSVParser parser = new CSVParser(new InputStreamReader(is), csvFormat);
        List<SkuReportEntry> entries = new ArrayList<>();
        Iterator<CSVRecord> iterator = parser.iterator();
        String header = null;
        if (hasFileHeader) {
            CSVRecord firstRecord = iterator.next();
            if (!firstRecord.get(0).isEmpty())
                throw new WrongFormatException("Wrong first record " + firstRecord);
            header = firstRecord.get(1);
        }
        CSVRecord headerRecord = iterator.next();
        for (int i = 0; i < Header.values().length; i++) {
            String field = headerRecord.get(i);
            if (!Objects.equals(field, Header.values()[i].name)) {
                throw new WrongFormatException("Wrong header " + headerRecord);
            }
        }
        SkuReportTotal total = null;
        while (iterator.hasNext()) {
            CSVRecord r = iterator.next();
            if (r.get(0).equals("Всего")) {
                total = new SkuReportTotal(
                        Integer.parseInt(r.get(4)),
                        Integer.parseInt(r.get(5)),
                        parseDouble(r.get(6)),
                        parseDouble(r.get(7)),
                        parseDouble(r.get(8)),
                        Integer.parseInt(r.get(9)),
                        parseDouble(r.get(10)),
                        Integer.parseInt(r.get(11)),
                        parseDouble(r.get(12))
                );
                continue;
            }
            LocalDate date = LocalDate.from(dateFormatter.parse(r.get(0)));
            entries.add(
                    new SkuReportEntry(
                            date,
                            Long.getLong(r.get(1)),
                            r.get(2),
                            parseDouble(r.get(3)),
                            Integer.parseInt(r.get(4)),
                            Integer.parseInt(r.get(5)),
                            parseDouble(r.get(6)),
                            parseDouble(r.get(7)),
                            parseDouble(r.get(8)),
                            Integer.parseInt(r.get(9)),
                            parseDouble(r.get(10)),
                            Integer.parseInt(r.get(11)),
                            parseDouble(r.get(12))
                    )
            );
        }
        return new SkuTableReport(entries, total);
    }

    private Double parseDouble(String val) {
        return Double.parseDouble(val.replaceAll("[^\\d,.]", "").replace(',', '.'));
    }

    private enum Header {
        DAY("День"),
        SKU("sku"),
        NAME("Название товара"),
        PRICE("Цена товара, ₽"),
        SHOWING("Показы"),
        CLICKS("Клики"),
        CTR("CTR (%)"),
        AVG_CLICK_PRICE("Ср. цена клика, ₽"),
        COST("Расход, ₽, с НДС"),
        ORDERS("Заказы"),
        REVENUE("Выручка, ₽"),
        MODEL_ORDER("Заказы модели"),
        MODEL_REVENUE("Выручка с заказов модели, ₽");
        private final String name;

        Header(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}
