package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiscountedProductData(
        @JsonProperty("comment_reason_damaged") String commentReasonDamaged,
        @JsonProperty("condition") String condition,
        @JsonProperty("condition_estimation") String conditionEstimation,
        @JsonProperty("defects") String defects,
        @JsonProperty("discounted_sku") Long discountedSku,
        @JsonProperty("mechanical_damage") String mechanicalDamage,
        @JsonProperty("package_damage") String packageDamage,
        @JsonProperty("packaging_violation") String packagingViolation,
        @JsonProperty("reason_damaged") String reasonDamaged,
        @JsonProperty("repair") String repair,
        @JsonProperty("shortage") String shortage,
        @JsonProperty("sku") Long sku,
        @JsonProperty("warranty_type") String warrantyType
) {
}
