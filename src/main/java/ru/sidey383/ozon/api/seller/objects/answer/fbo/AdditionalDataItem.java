package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AdditionalDataItem(
        @JsonProperty("key") String key,
        @JsonProperty("value") String value
) {
}
