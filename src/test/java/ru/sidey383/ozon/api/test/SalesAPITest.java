package ru.sidey383.ozon.api.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.seller.objects.container.PageableItemList;
import ru.sidey383.ozon.api.seller.objects.container.SingleResultContainer;
import ru.sidey383.ozon.api.seller.JsonSellerAPIRequest;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductInfo;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductDescription;
import ru.sidey383.ozon.api.seller.objects.request.*;
import ru.sidey383.ozon.api.seller.objects.request.sub.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SalesAPITest {

    private final Logger logger = LoggerFactory.getLogger(SalesAPITest.class);

    private static List<TestMarket> markets;

    @BeforeAll
    public static void init() throws IOException {
        markets = new ObjectMapper().readValue(SalesAPITest.class.getResource("/markets.json"), new TypeReference<>() {
        });
    }

    private void testRequest(JsonSellerAPIRequest<?> request) {
        int i = 0;
        for (var e : markets) {
            i++;
            int finalI = i;
            assertDoesNotThrow(() -> e.getSellerAPI().runRequest(request), () -> "Error in " + e.getName() + " number " + finalI + " request: " + request.toString());
        }
    }

    @Test
    public void analyticsDataRequestTest() {
        AnalyticsDataRequest request = new AnalyticsDataRequest(
                LocalDate.now(),
                LocalDate.now(),
                new AnalyticsDimensions[]{
                        AnalyticsDimensions.DAY,
                        AnalyticsDimensions.SKU
                },
                new Metric[]{
                        Metric.HITS_VIEW,
                        Metric.CANCELLATIONS
                },
                new AnalyticsFilter[]{
                        new AnalyticsFilter(
                                Metric.HITS_VIEW,
                                FilterOperation.LOWER_EQUALS,
                                "0"
                        )
                },
                10,
                0,
                new MetricsSorting[]{
                        new MetricsSorting(
                                Metric.HITS_VIEW,
                                SortingOrder.ASCENDING
                        )
                }
        );
        testRequest(request);
    }

    @Test
    public void fboListRequestTest() {
        FboListRequest request = new FboListRequest(
                SortingOrder.ASCENDING,
                new DeliveryTimeFilter(
                        Instant.now().minusSeconds(60 * 60 * 24 * 5),
                        Instant.now(),
                        null
                ),
                100,
                0,
                false,
                new FboListRequest.With(true, true)
        );
        testRequest(request);
    }

    @Test
    public void productInfoPricesRequestTest() {
        ProductInfoPricesRequest request = new ProductInfoPricesRequest(
                new ProductFilter(
                        null,
                        null,
                        ProductVisibility.VISIBLE
                ),
                null,
                100
        );
        testRequest(request);
    }

    @Test
    public void productRequestTest() {
        ProductListRequest request = new ProductListRequest(
                new ProductFilter(
                        null,
                        null,
                        ProductVisibility.VISIBLE
                ),
                null,
                100
        );
        int i = 0;
        for (var e : markets) {
            i++;
            int finalI = i;
            assertDoesNotThrow(() -> {
                SingleResultContainer<PageableItemList<ProductDescription>> productPairResult = e.getSellerAPI().runRequest(request);
                Optional<ProductDescription> pair = productPairResult.getResult().getItems().stream().findFirst();
                if (pair.isEmpty())
                    throw new IllegalStateException("wrong market fro test, no products");
                ProductInfoRequest infoRequest = new ProductInfoRequest(
                        pair.get().offerID(),
                        null,
                        null
                );
                SingleResultContainer<ProductInfo> productInfoResult = e.getSellerAPI().runRequest(infoRequest);
                if (productInfoResult.getResult() == null)
                    throw new IllegalStateException("can't get product info by offer id");
                infoRequest = new ProductInfoRequest(
                        null,
                        pair.get().productID(),
                        null
                );
                productInfoResult = e.getSellerAPI().runRequest(infoRequest);
                if (productInfoResult.getResult() == null)
                    throw new IllegalStateException("can't get product info by product id");
            }, () -> "Error in " + e.getName() + " number " + finalI + " request: " + request);

        }
    }

    @Test
    public void stockRequestTest() {
        StockRequest request = new StockRequest(
                100,
                0,
                WarehouseType.EXPRESS_DARK_STORE
        );
        testRequest(request);
    }

}
