package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductErrors(
        @JsonProperty("code") String code,
        @JsonProperty("state") String state,
        @JsonProperty("level") String level,
        @JsonProperty("description") String description,
        @JsonProperty("field") String field,
        @JsonProperty("attribute_id") Long attributeID,
        @JsonProperty("attribute_name") String attributeName
) {
}
