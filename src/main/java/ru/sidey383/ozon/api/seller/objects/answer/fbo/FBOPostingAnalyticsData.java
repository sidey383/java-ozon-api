package ru.sidey383.ozon.api.seller.objects.answer.fbo;

/**
 * @param city Город доставки.
 * @param delivery_type Способ доставки.
 * @param is_legal Получатель юридическое лицо:
 * true — юридическое лицо,
 * false — физическое лицо.
 * @param is_premium Наличие подписки Premium.
 * @param payment_type_group_name Способ оплаты.
 * @param region Регион доставки.
 * @param warehouse_id Идентификатор склада.
 * @param warehouse_name Название склада отправки заказа.
 * **/
public record FBOPostingAnalyticsData(
        String city,
        String delivery_type,
        boolean is_legal,
        boolean is_premium,
        String payment_type_group_name,
        String region,
        long warehouse_id,
        String warehouse_name
) {
}
