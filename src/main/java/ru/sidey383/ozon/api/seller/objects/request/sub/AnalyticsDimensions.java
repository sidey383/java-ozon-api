package ru.sidey383.ozon.api.seller.objects.request.sub;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @see ru.sidey383.ozon.api.seller.objects.request.AnalyticsDataRequest
 * **/
public enum AnalyticsDimensions {

    @JsonProperty("unknownDimension")
    UNKNOWN_DIMENSION(false),
    @JsonProperty("sku")
    SKU(false),
    @JsonProperty("spu")
    SPU(false),
    @JsonProperty("day")
    DAY(false),
    @JsonProperty("week")
    WEEK(false),
    @JsonProperty("month")
    MONTH(false),
    @JsonProperty("year")
    YEAR(true),
    @JsonProperty("category1")
    CATEGORY_1(true),
    @JsonProperty("category2")
    CATEGORY_2(true),
    @JsonProperty("category3")
    CATEGORY_3(true),
    @JsonProperty("category4")
    CATEGORY_4(true),
    @JsonProperty("brand")
    BRAND(true),
    @JsonProperty("modelID")
    MODEL_ID(true);

    private final boolean isPremium;


    AnalyticsDimensions(boolean isPremium) {
        this.isPremium = isPremium;
    }

    public boolean isPremium() {
        return isPremium;
    }

}
