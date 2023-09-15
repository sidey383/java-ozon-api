package ru.sidey383.ozon.api.seller.objects.answer.fbo;

/**
 * @param marketplace_service_item_deliv_to_customer Последняя миля.
 * @param marketplace_service_item_direct_flow_trans Магистраль.
 * @param marketplace_service_item_dropoff_ff Обработка отправления на фулфилмент складе (ФФ).
 * @param marketplace_service_item_dropoff_pvz Обработка отправления в ПВЗ.
 * @param marketplace_service_item_dropoff_sc Обработка отправления в сортировочном центре.
 * @param marketplace_service_item_fulfillment Сборка заказа.
 * @param marketplace_service_item_pickup Выезд транспортного средства по адресу продавца для забора отправлений (Pick-up).
 * @param marketplace_service_item_return_after_deliv_to_customer Обработка возврата.
 * @param marketplace_service_item_return_flow_trans Обратная магистраль.
 * @param marketplace_service_item_return_not_deliv_to_customer Обработка отмен.
 * @param marketplace_service_item_return_part_goods_customer Обработка невыкупа.
 * **/
public record PostingFinancialDataServices(
        double marketplace_service_item_deliv_to_customer,
        double marketplace_service_item_direct_flow_trans,
        double marketplace_service_item_dropoff_ff,
        double marketplace_service_item_dropoff_pvz,
        double marketplace_service_item_dropoff_sc,
        double marketplace_service_item_fulfillment,
        double marketplace_service_item_pickup,
        double marketplace_service_item_return_after_deliv_to_customer,
        double marketplace_service_item_return_flow_trans,
        double marketplace_service_item_return_not_deliv_to_customer,
        double marketplace_service_item_return_part_goods_customer
) {
}
