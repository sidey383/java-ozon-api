package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PostingFinancialData(
        @JsonProperty("posting_services") PostingFinancialDataServices postingServices,
        @JsonProperty("cluster_from") String clusterFrom,
        @JsonProperty("cluster_to") String clusterTo,
        @JsonProperty("products") List<FBOPostingFinancialDataProduct> products
) {
}
