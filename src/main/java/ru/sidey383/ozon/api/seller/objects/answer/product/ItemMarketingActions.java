package ru.sidey383.ozon.api.seller.objects.answer.product;

import java.util.List;

public record ItemMarketingActions(

        List<MarketingAction> actions,
        String current_period_from,
        String current_period_to,
        boolean ozon_actions_exist
) {
}
