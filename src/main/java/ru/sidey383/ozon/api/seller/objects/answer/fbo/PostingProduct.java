package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import java.util.List;

public record PostingProduct(
        List<String> digital_codes,
        String name,
        String offer_id,
        String currency_code,
        String price,
        long quantity,
        long sku
) {
}
