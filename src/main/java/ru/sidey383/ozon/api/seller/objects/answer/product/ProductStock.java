package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductStock(
        @JsonProperty("coming") int coming,
        @JsonProperty("present") int present,
        @JsonProperty("reserved") int reserved
) {
}
