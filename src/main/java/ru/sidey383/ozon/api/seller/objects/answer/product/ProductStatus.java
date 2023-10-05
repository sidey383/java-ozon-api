package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record ProductStatus(
        @JsonProperty("state") String state,
        @JsonProperty("state_failed") String stateFailed,
        @JsonProperty("moderate_status") String moderateStatus,
        @JsonProperty("decline_reasons") String[] declineReasons,
        @JsonProperty("validation_state") String validationState,
        @JsonProperty("state_name") String stateName,
        @JsonProperty("state_description") String stateDescription,
        @JsonProperty("is_failed") boolean isFailed,
        @JsonProperty("is_created") boolean isCreated,
        @JsonProperty("state_tooltip") String stateTooltip,
        @JsonProperty("item_errors") ProductErrors[] itemErrors,
        @JsonProperty("state_updated_at") Instant stateUpdatedAt
) {
}
