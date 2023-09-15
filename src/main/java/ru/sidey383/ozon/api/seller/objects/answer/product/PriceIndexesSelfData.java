package ru.sidey383.ozon.api.seller.objects.answer.product;

public record PriceIndexesSelfData(
        String minimal_price,
        String minimal_price_currency,
        double price_index_value
) {
}
