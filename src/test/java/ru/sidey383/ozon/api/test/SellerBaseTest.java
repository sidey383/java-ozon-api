package ru.sidey383.ozon.api.test;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.seller.OzonSellerAPI;
import ru.sidey383.ozon.api.seller.objects.DeliveryStatus;
import ru.sidey383.ozon.api.seller.objects.request.DeliveryTimeFilter;
import ru.sidey383.ozon.api.seller.objects.request.FboListRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SellerBaseTest {

    private final Logger logger = LoggerFactory.getLogger(SellerBaseTest.class);

    private static List<TestMarket> marketList;

    private static List<OzonSellerAPI> ozonAPI;

    @BeforeAll
    public static void loadMarkets() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        marketList = mapper.readValue(PerformanceBaseTest.class.getResource("/markets.json"), new TypeReference<>() {
        });
        ozonAPI = new ArrayList<>();
        for (var m : marketList) {
            ozonAPI.add(new OzonSellerAPI(m.sellerClientID(), m.apiKey()));
        }
    }

    @Test
    public void postingFBOListTest() {
        FboListRequest request = new FboListRequest(
                FboListRequest.Direction.ASCENDING,
                new DeliveryTimeFilter(
                        Instant.now().minus(Duration.ofDays(30)),
                        Instant.now(),
                        null
                ),
                FboListRequest.REQUEST_LIMIT,
                0,
                false,
                new FboListRequest.With(
                        true,
                        true
                )
        );
        for (OzonSellerAPI api : ozonAPI) {
            assertDoesNotThrow(() ->
            {
                api.getPostingFboList(request);
            });
        }
    }


}
