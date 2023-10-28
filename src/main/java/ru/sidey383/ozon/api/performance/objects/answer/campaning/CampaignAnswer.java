package ru.sidey383.ozon.api.performance.objects.answer.campaning;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CampaignAnswer(
        long id, //
        String title, //
        CampaignState state, //
        String advObjectType, //
        LocalDate fromDate, //
        LocalDate toDate, //
        long budget, // (RUB / 1.000.000)
        long dailyBudget,  // (RUB / 1.000.000)
        ProductCampaignPlacement[] placement, //
        String productAutopilotStrategy, //
        CampaignAutopilotProperties autopilot, //
        Instant createdAt, //
        Instant updatedAt, //
        CampaignMode productCampaignMode,  //
        String PaymentType

) {

    public static final String ADV_OBJECT_TYPE_SKU = "SKU";
    public static final String ADV_OBJECT_TYPE_BANNER = "BANNER";
    public static final String ADV_OBJECT_TYPE_BRAND_SHELF = "BRAND_SHELF";
    public static final String ADV_OBJECT_TYPE_BOOSTING_SKU = "BOOSTING_SKU";
    public static final String ADV_OBJECT_TYPE_ACTION_CAS = "ACTION_CAS";

    public static final String ADV_OBJECT_TYPE_SEARCH_PROMO = "SEARCH_PROMO";

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
                ", productCampaignMode='" + productCampaignMode + '\'' +
                ", PaymentType='" + PaymentType + '\'' +
                '}';
    }
}
