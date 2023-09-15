package ru.sidey383.ozon.api.seller.objects.answer.product;

import ru.sidey383.ozon.api.seller.objects.answer.Currency;

public record ItemPrice(
        boolean auto_action_enabled,
        Currency currency_code,
        String marketing_price,
        String marketing_seller_price,
        String min_ozon_price,
        String min_price,
        String old_price,
        String premium_price,
        String price,
        String recommended_price,
        String retail_price,
        String vat
) {

}
