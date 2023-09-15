package ru.sidey383.ozon.api.performance.objects.answer.campaning;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public record CampaignAutopilotProperties (
        long categoryId,
        String skuAddMode
) {
}
