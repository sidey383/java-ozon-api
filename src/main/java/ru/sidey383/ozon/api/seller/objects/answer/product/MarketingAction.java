package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MarketingAction(
        @JsonProperty("date_from") String dateFrom,
        @JsonProperty("date_to") String dateTo,
        @JsonProperty("discount_value") String discountValue,
        @JsonProperty("title") String title
) {
}
