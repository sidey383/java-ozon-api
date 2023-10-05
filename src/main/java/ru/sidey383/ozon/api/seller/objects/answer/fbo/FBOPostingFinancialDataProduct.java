package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sidey383.ozon.api.seller.objects.answer.Currency;

import java.util.List;

/**
 * @param actions Действия.
 * @param currencyCode Валюта ваших цен. Cовпадает с валютой, которая установлена в настройках личного кабинета.<br/>
 * Возможные значения:<br/>
 * RUB — российский рубль,<br/>
 * BYN — белорусский рубль,<br/>
 * KZT — тенге,<br/>
 * EUR — евро,<br/>
 * USD — доллар США,<br/>
 * CNY — юань.<br/>
 * @param clientPrice Цена для клиента.
 * @param commissionAmount комиссии за товар.
 * @param commissionPercent Процент комиссии.
 * @param commissionsCurrencyCode Код валюты, в которой рассчитывались комиссии.
 * @param itemServices Услуги.
 * @param oldPrice Цена до учёта скидок. На карточке товара отображается зачёркнутой.
 * @param payout Выплата продавцу.
 * @param picking Информация о доставке.
 * @param price Цена товара с учётом скидок — это значение показывается на карточке товара.
 * @param productID Идентификатор товара.
 * @param quantity Количество товара в отправлении.
 * @param totalDiscountPercent роцент скидки.
 * @param totalDiscountValue Сумма скидки.
 * **/
public record FBOPostingFinancialDataProduct(
        @JsonProperty("actions") List<String> actions,
        @JsonProperty("currency_code") Currency currencyCode,
        @JsonProperty("client_price") String clientPrice,
        @JsonProperty("commission_amount") double commissionAmount,
        @JsonProperty("commission_percent") long commissionPercent,
        @JsonProperty("commissions_currency_code") String commissionsCurrencyCode,
        @JsonProperty("old_price") double oldPrice,
        @JsonProperty("payout") double payout,
        @JsonProperty("price") double price,
        @JsonProperty("product_id") long productID,
        @JsonProperty("quantity") long quantity, //Always 0 ?
        @JsonProperty("total_discount_percent") double totalDiscountPercent,
        @JsonProperty("total_discount_value") double totalDiscountValue,
        @JsonProperty("item_services") PostingFinancialDataServices itemServices,
        @JsonProperty("picking") ProductPicking picking
) {
}
