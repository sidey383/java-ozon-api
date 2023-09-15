package ru.sidey383.ozon.api.seller.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DeliveryStatus {
    @JsonProperty("awaiting_packaging")
    AWAITING_PACKAGING(),
    @JsonProperty("awaiting_deliver")
    AWAITING_DELIVER(),
    @JsonProperty("delivering")
    DELIVERING(),
    @JsonProperty("delivered")
    DELIVERED(),
    @JsonProperty("cancelled")
    CANCELLED(),
    @JsonProperty("awaiting_verification")
    AWAITING_VERIFICATION()
}
