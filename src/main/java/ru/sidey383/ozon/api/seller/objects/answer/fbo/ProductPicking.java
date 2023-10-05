package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductPicking(
        @JsonProperty("amount") double amount,
        @JsonProperty("moment") String moment,
        @JsonProperty("tag") String tag
) {
}
