package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sidey383.ozon.api.seller.objects.answer.Currency;

import java.time.Instant;

@JsonIgnoreProperties({"min_ozon_price", "errors", "price_index", "state", "service_type", "description_category_id"})
public record ProductInfo(
        @JsonProperty("barcode") String barcode,
        @JsonProperty("barcodes") String[] barcodes,
        @JsonProperty("buybox_price") String buyboxPrice,
        @JsonProperty("category_id") Long categoryID,
        @JsonProperty("color_image") String colorImage,
        @JsonProperty("commissions") ProductCommissions[] commissions,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("sku") Long sku,
        @JsonProperty("fbo_sku") Long fboSku,
        @JsonProperty("fbs_sku") Long fbsSku,
        @JsonProperty("id") Long id,
        @JsonProperty("images") String[] images,
        @JsonProperty("primary_image") String primaryImage,
        @JsonProperty("images360") String[] images360,
        @JsonProperty("has_discounted_item") boolean hasDiscountedItem,
        @JsonProperty("is_discounted") boolean isDiscounted,
        @JsonProperty("discounted_stocks") ProductStock discountedStocks,
        @JsonProperty("is_kgt") boolean isKgt,
        @JsonProperty("is_prepayment") boolean isPrepayment,
        @JsonProperty("is_prepayment_allowed") boolean isPrepaymentAllowed,
        @JsonProperty("currency_code") Currency currencyCode,
        @JsonProperty("marketing_price") Double marketingPrice,
        @JsonProperty("min_price") Double minPrice,
        @JsonProperty("name") String name,
        @JsonProperty("offer_id") String offerID,
        @JsonProperty("old_price") Double oldPrice,
        @JsonProperty("premium_price") Double premiumPrice,
        @JsonProperty("price") Double price,
        @JsonProperty("price_indexes") CommonPriceIndexes priceIndexes,
        @JsonProperty("recommended_price") Double recommendedPrice,
        @JsonProperty("status") ProductStatus status,
        @JsonProperty("sources") ProductSource[] sources,
        @JsonProperty("stocks") ProductStock stocks,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("vat") String vat,
        @JsonProperty("visibility_details") ProductVisibilityDetails visibilityDetails,
        @JsonProperty("visible") boolean visible,
        @JsonProperty("volume_weight") Double volumeWeight
) {
}
