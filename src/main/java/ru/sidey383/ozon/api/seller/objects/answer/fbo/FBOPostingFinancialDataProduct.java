package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import ru.sidey383.ozon.api.seller.objects.answer.Currency;

import java.util.List;

/**
 * @param actions Действия.
 * @param currency_code Валюта ваших цен. Cовпадает с валютой, которая установлена в настройках личного кабинета.<br/>
 * Возможные значения:<br/>
 * RUB — российский рубль,<br/>
 * BYN — белорусский рубль,<br/>
 * KZT — тенге,<br/>
 * EUR — евро,<br/>
 * USD — доллар США,<br/>
 * CNY — юань.<br/>
 * @param client_price Цена для клиента.
 * @param commission_amount Размер комиссии за товар.
 * @param commission_percent Процент комиссии.
 * @param commissions_currency_code Код валюты, в которой рассчитывались комиссии.
 * @param item_services Услуги.
 * @param old_price Цена до учёта скидок. На карточке товара отображается зачёркнутой.
 * @param payout Выплата продавцу.
 * @param picking Информация о доставке.
 * @param price Цена товара с учётом скидок — это значение показывается на карточке товара.
 * @param product_id Идентификатор товара.
 * @param quantity Количество товара в отправлении.
 * @param total_discount_percent роцент скидки.
 * @param total_discount_value Сумма скидки.
 * **/
public record FBOPostingFinancialDataProduct(
        List<String> actions,
        Currency currency_code,
        String client_price,
        double commission_amount,
        long commission_percent,
        String commissions_currency_code,
        double old_price,
        double payout,
        double price,
        long product_id,
        long quantity, //Always 0 ?
        double total_discount_percent,
        double total_discount_value,
        PostingFinancialDataServices item_services,
        ProductPicking picking
) {
}
