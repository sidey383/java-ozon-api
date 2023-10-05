package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostingFinancialDataServices(
        @JsonProperty("marketplace_service_item_deliv_to_customer") double marketplaceServiceItemDelivToCustomer,
        @JsonProperty("marketplace_service_item_direct_flow_trans") double marketplaceServiceItemDirectFlowTrans,
        @JsonProperty("marketplace_service_item_dropoff_ff") double marketplaceServiceItemDropoffFf,
        @JsonProperty("marketplace_service_item_dropoff_pvz") double marketplaceServiceItemDropoffPvz,
        @JsonProperty("marketplace_service_item_dropoff_sc") double marketplaceServiceItemDropoffSc,
        @JsonProperty("marketplace_service_item_fulfillment") double marketplaceServiceItemFulfillment,
        @JsonProperty("marketplace_service_item_pickup") double marketplaceServiceItemPickup,
        @JsonProperty("marketplace_service_item_return_after_deliv_to_customer") double marketplaceServiceItemReturnAfterDelivToCustomer,
        @JsonProperty("marketplace_service_item_return_flow_trans") double marketplaceServiceItemReturnFlowTrans,
        @JsonProperty("marketplace_service_item_return_not_deliv_to_customer") double marketplaceServiceItemReturnNotDelivToCustomer,
        @JsonProperty("marketplace_service_item_return_part_goods_customer") double marketplaceServiceItemReturnPartFoodsCustomer
) {
}
