package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import ru.sidey383.ozon.api.seller.objects.DeliveryStatus;

import java.util.List;

/**
 * @param analytics_data Данные аналитики
 * @param cancel_reason_id Идентификатор причины отмены отправления.
 * @param created_at Дата и время создания отправления.
 * @param financial_data Финансовые данные.
 * @param in_process_at Дата и время начала обработки отправления.
 * @param order_id Идентификатор заказа, к которому относится отправление.
 * @param order_number Номер заказа, к которому относится отправление.
 * @param posting_number Номер отправления.
 * @param products Список товаров в отправлении.
 * @param status  Статус отправления:
 * {@link DeliveryStatus#AWAITING_PACKAGING} — ожидает упаковки,
 * {@link DeliveryStatus#AWAITING_DELIVER} — ожидает отгрузки,
 * {@link DeliveryStatus#DELIVERING} — доставляется,
 * {@link DeliveryStatus#DELIVERED} — доставлено,
 * {@link DeliveryStatus#CANCELLED} — отменено.
 * **/
public record FBOPosting(
        List<AdditionalDataItem> additional_data,
        FBOPostingAnalyticsData analytics_data,
        long cancel_reason_id,
        String created_at,
        PostingFinancialData financial_data,
        String in_process_at,
        long order_id,
        String order_number,
        String posting_number,
        List<PostingProduct> products,
        DeliveryStatus status
) {
}
