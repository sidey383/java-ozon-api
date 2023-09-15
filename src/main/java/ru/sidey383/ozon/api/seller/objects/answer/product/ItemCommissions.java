package ru.sidey383.ozon.api.seller.objects.answer.product;

public record ItemCommissions(
    double fbo_deliv_to_customer_amount,
    double fbo_direct_flow_trans_max_amount,
    double fbo_direct_flow_trans_min_amount,
    double fbo_fulfillment_amount,
    double fbo_return_flow_amount,
    double fbo_return_flow_trans_min_amount,
    double fbo_return_flow_trans_max_amount,
    double fbs_deliv_to_customer_amount,
    double fbs_direct_flow_trans_max_amount,
    double fbs_direct_flow_trans_min_amount,
    double fbs_first_mile_min_amount,
    double fbs_first_mile_max_amount,
    double fbs_return_flow_amount,
    double fbs_return_flow_trans_max_amount,
    double fbs_return_flow_trans_min_amount,
    double sales_percent_fbo,
    double sales_percent_fbs,
    double sales_percent
) {
}
