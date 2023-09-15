package ru.sidey383.ozon.api.performance.objects.answer.campaning;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CampaningAnswer(
        String id,
        String title,
        CampaningState state,
        String advObjectType,
        LocalDate fromDate,
        LocalDate toDate,
        long budget, // (RUB / 1.000.000)
        long dailyBudget,  // (RUB / 1.000.000)
        ProductCampaignPlacement[] placement,
        String productAutopilotStrategy,
        CampaignAutopilotProperties autopilot,
        Instant createdAt,
        Instant updatedAt

) {

    @Override
    public String toString() {
        return "CampaningAnswer{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", state=" + state +
                ", advObjectType='" + advObjectType + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", budget=" + budget +
                ", dailyBudget=" + dailyBudget +
                ", placement=" + Arrays.toString(placement) +
                ", productAutopilotStrategy='" + productAutopilotStrategy + '\'' +
                ", autopilot=" + autopilot +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
