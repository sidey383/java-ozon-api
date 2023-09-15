package ru.sidey383.ozon.api.test;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.AnswerList;
import ru.sidey383.ozon.api.performance.OzonPerformanceAPI;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaningAnswer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerformanceBaseTest {

    private final Logger logger = LoggerFactory.getLogger(PerformanceBaseTest.class);

    private static List<TestMarket> marketList;

    private static List<OzonPerformanceAPI> ozonPerformanceAPI;

    @BeforeAll
    public static void loadMarkets() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        marketList = mapper.readValue(PerformanceBaseTest.class.getResource("/markets.json"), new TypeReference<>() {
        });
        ozonPerformanceAPI = new ArrayList<>();
        for (var m : marketList) {
            ozonPerformanceAPI.add(new OzonPerformanceAPI(m.clientID(), m.clientSecret()));
        }
    }

    @Test
    public void authTest() {
        for (OzonPerformanceAPI api : ozonPerformanceAPI) {
            assertDoesNotThrow(api::updateToken);
            assertFalse(api::isTokenExpire);
            logger.info("Token : "  + api.getToken());
        }
    }

    @Test
    public void clientCampaningsTest() {
        for (OzonPerformanceAPI api : ozonPerformanceAPI) {
            assertDoesNotThrow(() -> {
                AnswerList<CampaningAnswer> answerList = api.getClientCampanings(null, null, null);
                logger.info("Answer :");
                for (var a : answerList.getList()) {
                    logger.info(a.toString());
                }
            });
        }
    }

}
