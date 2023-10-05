package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sidey383.ozon.api.seller.objects.DeliveryStatus;

import java.util.List;

/**
 * @param analyticsData   Данные аналитики
 * @param cancelReasonID Идентификатор причины отмены отправления.
 * @param createdAt       Дата и время создания отправления.
 * @param financialData   Финансовые данные.
 * @param inProcessAt    Дата и время начала обработки отправления.
 * @param orderID        Идентификатор заказа, к которому относится отправление.
 * @param orderNumber     Номер заказа, к которому относится отправление.
 * @param postingNumber   Номер отправления.
 * @param products         Список товаров в отправлении.
 * @param status           Статус отправления:
 *                         {@link DeliveryStatus#AWAITING_PACKAGING} — ожидает упаковки,
 *                         {@link DeliveryStatus#AWAITING_DELIVER} — ожидает отгрузки,
 *                         {@link DeliveryStatus#DELIVERING} — доставляется,
 *                         {@link DeliveryStatus#DELIVERED} — доставлено,
 *                         {@link DeliveryStatus#CANCELLED} — отменено.
 **/
public record FBOPosting(
        @JsonProperty("additional_data") List<AdditionalDataItem> additionalData,
        @JsonProperty("analytics_data") FBOPostingAnalyticsData analyticsData,
        @JsonProperty("cancel_reason_id") long cancelReasonID,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("financial_data") PostingFinancialData financialData,
        @JsonProperty("in_process_at") String inProcessAt,
        @JsonProperty("order_id") long orderID,
        @JsonProperty("order_number") String orderNumber,
        @JsonProperty("posting_number") String postingNumber,
        @JsonProperty("products") List<PostingProduct> products,
        @JsonProperty("status") DeliveryStatus status
) {
}
