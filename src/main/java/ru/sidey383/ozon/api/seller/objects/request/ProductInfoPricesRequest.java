package ru.sidey383.ozon.api.seller.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductInfoPricesRequest (
        Filter filter,
        String last_id,
        long limit
) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Filter(
            Visibility visibility,
            String[] offer_id,
            long[] product_id
    ) {}

    public enum Visibility {
        ALL,
        VISIBLE,
        INVISIBLE,
        EMPTY_STOCK,
        NOT_MODERATED,
        MODERATED,
        DISABLED,
        STATE_FAILED,
        READY_TO_SUPPLY,
        VALIDATION_STATE_PENDING,
        VALIDATION_STATE_FAIL,
        VALIDATION_STATE_SUCCESS,
        TO_SUPPLY,
        IN_SALE,
        REMOVED_FROM_SALE,
        BANNED,
        OVERPRICED,
        CRITICALLY_OVERPRICED,
        EMPTY_BARCODE,
        BARCODE_EXISTS,
        QUARANTINE,
        ARCHIVED,
        OVERPRICED_WITH_STOCK,
        PARTIAL_APPROVED,
        IMAGE_ABSENT,
        MODERATION_BLOCK
    }

}
