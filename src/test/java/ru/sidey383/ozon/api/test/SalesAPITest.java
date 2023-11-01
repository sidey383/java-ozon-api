package ru.sidey383.ozon.api.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.container.PageableItemList;
import ru.sidey383.ozon.api.container.SingleResultContainer;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;
import ru.sidey383.ozon.api.seller.JsonSellerAPIRequest;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductInfo;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductDescription;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductInfoItem;
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
                        Metric.REVENUE,
                        Metric.ORDERED_UNITS
                },
                new AnalyticsFilter[]{
                        new AnalyticsFilter(
                                Metric.REVENUE,
                                FilterOperation.GREAT_EQUALS,
                                "10.0"
                        )
                },
                10,
                0,
                new MetricsSorting[]{
                        new MetricsSorting(
                                Metric.REVENUE,
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
                WarehouseType.ALL
        );
        testRequest(request);
    }

    @Test
    public void productInfoDiscountedRequestTest() throws OzonWrongCodeException, IOException {
        ProductListRequest request = new ProductListRequest(null, null, 1);
        boolean hasProduct = false;
        for (var e : markets) {
            List<ProductDescription> productDescriptions = e.getSellerAPI().runRequest(request).getResult().getItems();
            if (productDescriptions.isEmpty())
                continue;
            ProductDescription productDescription = productDescriptions.get(0);
            ProductInfoListRequest infoRequest = new ProductInfoListRequest(new String[]{productDescription.offerID()}, null, null);
            var v1 = e.getSellerAPI().runRequest(infoRequest);
            if (v1.getResult().getItems().isEmpty())
                continue;
            ProductInfoItem prod = v1.getResult().getItems().get(0);
            long sku = Math.max(prod.sku(), Math.max(prod.fboSku(), prod.fbsSku()));
            if (sku == 0)
                continue;
            ProductInfoDiscountedRequest testRequest = new ProductInfoDiscountedRequest(new Long[]{sku});
            var data = e.getSellerAPI().runRequest(testRequest);
            hasProduct = true;
        }
        assertTrue(hasProduct, "Can't found products for test");
    }

    @Test
    public void productInfoListRequestTest() throws OzonWrongCodeException, IOException {
        ProductListRequest request = new ProductListRequest(null, null, 1);
        boolean hasProduct = false;
        for (var e : markets) {
            List<ProductDescription> productDescriptions = e.getSellerAPI().runRequest(request).getResult().getItems();
            if (productDescriptions.isEmpty())
                continue;
            ProductDescription productDescription = productDescriptions.get(0);
            ProductInfoListRequest testRequest1 = new ProductInfoListRequest(new String[]{productDescription.offerID()}, null, null);
            assertDoesNotThrow(() -> {
                        var v1 = e.getSellerAPI()
                                .runRequest(testRequest1);
                        assertFalse(
                                v1.getResult().getItems().isEmpty(),
                                "No result with request by offer id"
                        );
                    },
                    () -> "Request: " + testRequest1
            );
            ProductInfoListRequest testRequest2 = new ProductInfoListRequest(null, new Long[]{productDescription.productID()}, null);
            assertDoesNotThrow(() -> {
                        var v2 = e.getSellerAPI()
                                .runRequest(testRequest1);
                        assertFalse(
                                v2.getResult().getItems().isEmpty(),
                                "No result with request by product id"
                        );
                        var prod = v2.getResult().getItems().get(0);
                        ProductInfoListRequest testRequest3 = new ProductInfoListRequest(null, null, new Long[]{Math.max(prod.sku(), Math.max(prod.fboSku(), prod.fbsSku()))});
                        assertDoesNotThrow(() -> {
                                    var v3 = e.getSellerAPI().runRequest(testRequest3);
                                    assertFalse(
                                            v3.getResult().getItems().isEmpty(),
                                            "No result with request by sku"
                                    );
                                },
                                () -> "Request: " + testRequest3
                        );
                    },
                    () -> "Request: " + testRequest2
            );
            hasProduct = true;
        }
        assertTrue(hasProduct, "Can't found products for test");
    }

}
