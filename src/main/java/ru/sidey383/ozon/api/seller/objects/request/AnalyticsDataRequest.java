package ru.sidey383.ozon.api.seller.objects.request;

import java.time.LocalDate;

public record AnalyticsDataRequest(
        LocalDate date_from,
        LocalDate date_to,
        Dimension[] dimension,
        Metric[] metrics,
        AnalyticsFilter[] filters,
        long limit,
        long offset,
        Sorting[] sort
) {

    public static final long MAX_LIMIT = 1000;

    public record Sorting (
            Metric key,
            SoringOrder order
    ){

        public enum SoringOrder {
            ASC,
            DESC
        }

    }

    public record AnalyticsFilter(
            Metric key, //sort parameter
            FilterOp op, // equals operator
            String value
    ) {

        public enum FilterOp {
            EQ, // equals
            GT, // great
            GTE, // great or equals
            LT, // lower
            LTE // lower or equals
        }

    }

    public enum Metric {
        revenue(false),
        ordered_units(false),
        unknown_metric(true),
        hits_view_search(true),
        hits_view_pdp(true),
        hits_view(true),
        hits_tocart_search(true),
        hits_tocart_pdp(true),
        hits_tocart(true),
        session_view_search(true),
        session_view_pdp(true),
        session_view(true),
        conv_tocart_search(true),
        conv_tocart_pdp(true),
        conv_tocart(true),
        returns(true),
        cancellations(true),
        delivered_units(true),
        position_category(true);

        private final boolean isPremium;

        Metric(boolean isPremium) {
            this.isPremium = isPremium;
        }

        public boolean isPremium() {
            return isPremium;
        }
    }

    public enum Dimension {
        unknownDimension(false),
        sku(false),
        spu(false),
        day(false),
        week(false),
        month(false),
        year(true),
        category1(true),
        category2(true),
        category3(true),
        category4(true),
        brand(true),
        modelID(true);

        private final boolean isPremium;


        Dimension(boolean isPremium) {
            this.isPremium = isPremium;
        }

        public boolean isPremium() {
            return isPremium;
        }
    }

}
