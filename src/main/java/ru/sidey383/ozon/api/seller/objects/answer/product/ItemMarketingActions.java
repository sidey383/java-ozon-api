package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ItemMarketingActions(
        @JsonProperty("actions") List<MarketingAction> actions,
        @JsonProperty("current_period_from") String currentPeriodFrom,
        @JsonProperty("current_period_to") String currentPeriodTo,
        @JsonProperty("ozon_actions_exist") boolean ozonActionsExist
) {
}
