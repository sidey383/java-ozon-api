package ru.sidey383.ozon.api.seller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;
import ru.sidey383.ozon.api.seller.objects.answer.analystics.data.AnalyticsDataResult;
import ru.sidey383.ozon.api.seller.objects.answer.analystics.data.AnalyticsDataRow;
import ru.sidey383.ozon.api.seller.objects.answer.analystics.stock.StockOnWarehouseItem;
import ru.sidey383.ozon.api.seller.objects.answer.analystics.stock.StockOnWarehouseItemList;
import ru.sidey383.ozon.api.seller.objects.answer.fbo.FBOList;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductInfoPricesItems;
import ru.sidey383.ozon.api.seller.objects.answer.product.ProductInfoPricesList;
import ru.sidey383.ozon.api.seller.objects.request.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class OzonSellerAPI {

    private final Logger logger = LoggerFactory.getLogger(OzonSellerAPI.class);

    private static final String API_URL = "https://api-seller.ozon.ru";

    private final String client_id;

    private final String api_key;

    private final ObjectMapper mapper;

    private final OkHttpClient client = new OkHttpClient();

    public OzonSellerAPI(String client_id, String api_key) {
        this.api_key = api_key;
        this.client_id = client_id;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public FBOList getPostingFboList(FboListRequest request) throws OzonWrongCodeException, IOException {
        return getRequest("/v2/posting/fbo/list", request, FBOList.class);
    }

    public ProductInfoPricesItems getProductInfoPrices(ProductInfoPricesRequest request) throws OzonWrongCodeException, IOException {
        return getRequest("/v4/product/info/prices", request, ProductInfoPricesList.class).result();
    }

    public List<StockOnWarehouseItem> getAnalyticsStockOnWarehouse(StockRequest request) throws OzonWrongCodeException, IOException {
        return getRequest("/v2/analytics/stock_on_warehouses", request, StockOnWarehouseItemList.class).result().rows();
    }

    public List<AnalyticsDataRow> getAnalyticsData(AnalyticsDataRequest request) throws OzonWrongCodeException, IOException {
        return Arrays.asList(getRequest("/v1/analytics/data", request, AnalyticsDataResult.class).result().data());
    }

    public <A> A getRequest(String method, Object request, Class<A> result) throws OzonWrongCodeException, IOException {
        String json = mapper.writeValueAsString(request);
        Request apiRequest = new Request.Builder()
                .url(API_URL + method)
                .addHeader("Client-Id", client_id)
                .addHeader("Api-Key", api_key)
                .post(RequestBody
                        .create(
                                json,
                                MediaType.parse("application/json")
                        )).build();
        try (Response response = client.newCall(apiRequest).execute()) {
            if (!response.isSuccessful()) {
                return switch (response.code()) {
                    case 429 -> {
                        logger.info("Request limit for {} response: {}", method, response);
                        try {
                            Thread.sleep(20000);
                            yield  getRequest(method, request, result);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    default -> throw new OzonWrongCodeException(response);
                };
            }
            assert response.body() != null;
            return mapper.readValue(response.body().string(), result);
        }
    }

}
