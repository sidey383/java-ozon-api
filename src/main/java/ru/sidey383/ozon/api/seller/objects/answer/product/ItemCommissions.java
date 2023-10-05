package ru.sidey383.ozon.api.seller.objects.answer.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemCommissions(
        @JsonProperty("fbo_deliv_to_customer_amount") double fboDelivToCustomerAmount,
        @JsonProperty("fbo_direct_flow_trans_max_amount") double fboDirectFlowTransMaxAmount,
        @JsonProperty("fbo_direct_flow_trans_min_amount") double fboDirectFlowTransMinAmount,
        @JsonProperty("fbo_fulfillment_amount") double fboFulfillmentAmount,
        @JsonProperty("fbo_return_flow_amount") double fboReturnFlowAmount,
        @JsonProperty("fbo_return_flow_trans_min_amount") double fboReturnFlowTransMinAmount,
        @JsonProperty("fbo_return_flow_trans_max_amount") double fboReturnFlowTransMaxAmount,
        @JsonProperty("fbs_deliv_to_customer_amount") double fbsDelivToCustomerAmount,
        @JsonProperty("fbs_direct_flow_trans_max_amount") double fbsDirectFlowTransMaxAmount,
        @JsonProperty("fbs_direct_flow_trans_min_amount") double fbsDirectFlowTransMinAmount,
        @JsonProperty("fbs_first_mile_min_amount") double fbsFirstMileMinAmount,
        @JsonProperty("fbs_first_mile_max_amount") double fbsFirstMileMaxAmount,
        @JsonProperty("fbs_return_flow_amount") double fbsReturnFlowAmount,
        @JsonProperty("fbs_return_flow_trans_max_amount") double fbsReturnFlowTransMaxAmount,
        @JsonProperty("fbs_return_flow_trans_min_amount") double fbsReturnFlowTransMinAmount,
        @JsonProperty("sales_percent_fbo") double salesPercentFbo,
        @JsonProperty("sales_percent_fbs") double salesPercentFbs,
        @JsonProperty("sales_percent") double salesPercent
) {
}
