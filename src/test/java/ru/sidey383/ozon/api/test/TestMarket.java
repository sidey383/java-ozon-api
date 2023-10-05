package ru.sidey383.ozon.api.test;

import com.fasterxml.jackson.annotation.JsonSetter;
import ru.sidey383.ozon.api.performance.OzonPerformanceAPI;
import ru.sidey383.ozon.api.seller.OzonSellerAPI;

public class TestMarket {

    private String name;
    private TestMarketSellerAPI seller;
    private TestMarketPerformanceAPI performance;

    private OzonSellerAPI sellerAPI;

    private OzonPerformanceAPI performanceAPI;

    public synchronized OzonSellerAPI getSellerAPI() {
        if (sellerAPI == null) {
            sellerAPI = new OzonSellerAPI(seller.clientID(), seller.apiKey());
        }
        return sellerAPI;
    }

    public synchronized OzonPerformanceAPI getPerformanceAPI() {
        if (performanceAPI == null) {
            performanceAPI = new OzonPerformanceAPI(performance.clientID(), performance.clientSecret());
        }
        return performanceAPI;
    }

    public String getName() {
        return name;
    }

    @JsonSetter
    public void setName(String name) {
        this.name = name;
    }

    @JsonSetter
    public void setSeller(TestMarketSellerAPI seller) {
        this.seller = seller;
    }

    @JsonSetter
    public void setPerformance(TestMarketPerformanceAPI performance) {
        this.performance = performance;
    }
}
