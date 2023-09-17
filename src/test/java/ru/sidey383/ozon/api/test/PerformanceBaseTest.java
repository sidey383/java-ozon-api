package ru.sidey383.ozon.api.test;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

import okhttp3.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.AnswerList;
import ru.sidey383.ozon.api.ItemList;
import ru.sidey383.ozon.api.exception.OzonWrongCodeException;
import ru.sidey383.ozon.api.performance.OzonPerformanceAPI;
import ru.sidey383.ozon.api.performance.objects.answer.StatisticAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaningAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.statistic.ReportInfo;
import ru.sidey383.ozon.api.performance.objects.answer.statistic.StatisticStatus;
import ru.sidey383.ozon.api.performance.objects.answer.statistic.StatisticsRequestState;
import ru.sidey383.ozon.api.performance.objects.request.StatisticRequest;
import ru.sidey383.ozon.api.table.sku.SkuTableReport;
import ru.sidey383.ozon.api.table.sku.SkuReportEntry;
import ru.sidey383.ozon.api.table.sku.SkuReportParser;
import ru.sidey383.ozon.api.table.sku.SkuZipReport;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
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
            logger.info("Token : " + api.getToken());
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

    @Test
    public void createStatisticReport() throws OzonWrongCodeException, IOException, InterruptedException {
        OzonPerformanceAPI api = ozonPerformanceAPI.get(0);
        AnswerList<CampaningAnswer> answer = api.getClientCampanings(null, null, null);
        for (CampaningAnswer a : answer.getList()) {
            logger.info(a.toString());
        }
        StatisticAnswer statistic;
        do {
            try {
                List<CampaningAnswer> answerList = answer.getList().stream()
                        .limit(10).toList();
                statistic = api.getClientStatistics(new StatisticRequest(
                        answerList.stream().map(CampaningAnswer::id).toArray(String[]::new),
                        Instant.now().minus(Duration.ofDays(5)),
                        Instant.now(),
                        null,
                        null,
                        StatisticRequest.GroupBy.DATE
                ));
                break;
            } catch (OzonWrongCodeException e) {
                if (e.getCode() == 429) {
                    logger.info(e.getBody());
                    Thread.sleep(1000);
                } else {
                    throw e;
                }
            }
        } while (true);
        logger.info("Token: " + api.getToken());
        StatisticStatus status;
        do {
            Thread.sleep(1000);
            status = api.getClientStatisticStatus(statistic.uuid());
            logger.info(status.toString());
        } while (!status.state().isTerminate());
        if (status.state() == StatisticsRequestState.OK) {
            try(Response response = api.getClientStatisticsReport(status)) {
                if (response.body() != null) {
                    SkuReportParser parser = new SkuReportParser();
                    List<SkuZipReport> zipReport = parser.readZipReports(response.body().byteStream());
                    for (SkuZipReport report : zipReport) {
                        if (report == null) {
                            logger.info("Can't read entry");
                            continue;
                        }
                        logger.info(report.name() + " " + report.header());
                        for (SkuReportEntry e : report.entries()) {
                            logger.info(e.toString());
                        }
                        logger.info(report.total().toString());
                    }
                }
            }
        }

    }

    @Test
    public void getStatisticsList() throws OzonWrongCodeException, IOException, InterruptedException {
        for (OzonPerformanceAPI api : ozonPerformanceAPI) {
            api.updateToken();
            logger.info(api.getToken());
            ItemList<ReportInfo> list = api.getClientStatisticsList(0, 10);
            logger.info("Count " + list.getTotal());
            for (ReportInfo info : list.getItems()) {
                logger.info(info.toString());
            }
        }
    }

}
