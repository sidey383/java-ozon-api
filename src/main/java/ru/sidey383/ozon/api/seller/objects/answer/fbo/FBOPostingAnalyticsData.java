package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @param city Город доставки.
 * @param deliveryType Способ доставки.
 * @param isLegal Получатель юридическое лицо:
 * true — юридическое лицо,
 * false — физическое лицо.
 * @param isPremium Наличие подписки Premium.
 * @param paymentTypeGroupName Способ оплаты.
 * @param region Регион доставки.
 * @param warehouseID Идентификатор склада.
 * @param warehouseName Название склада отправки заказа.
 * **/
public record FBOPostingAnalyticsData(
        @JsonProperty("city") String city,
        @JsonProperty("delivery_type") String deliveryType,
        @JsonProperty("is_legal") boolean isLegal,
        @JsonProperty("is_premium") boolean isPremium,
        @JsonProperty("payment_type_group_name") String paymentTypeGroupName,
        @JsonProperty("region") String region,
        @JsonProperty("warehouse_id") long warehouseID,
        @JsonProperty("warehouse_name") String warehouseName
) {
}
