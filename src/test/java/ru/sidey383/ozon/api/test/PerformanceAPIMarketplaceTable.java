package ru.sidey383.ozon.api.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.container.AnswerList;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaignAnswer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class PerformanceAPIMarketplaceTable {

    private final Logger logger = LoggerFactory.getLogger(PerformanceAPIMarketplaceTable.class);

    private static List<TestMarket> markets;

    @BeforeAll
    public static void init() throws IOException {
        markets = new ObjectMapper().readValue(SalesAPITest.class.getResource("/markets.json"), new TypeReference<>() {
        });
    }

    @Test
    public void loadCampaigns () throws OzonApiException, IOException, ExecutionException, InterruptedException {
        Set<String> types = new HashSet<>();
        for (TestMarket market : markets) {
            AnswerList<CampaignAnswer> answerList = market.getPerformanceAPI().getClientCampaigns(null, null, null);
            types.addAll(answerList.getList().stream().map(CampaignAnswer::advObjectType).collect(Collectors.toSet()));
            List<CampaignAnswer> answer = answerList.getList().stream()
                    .filter(c -> CampaignAnswer.ADV_OBJECT_TYPE_BRAND_SHELF.equals(c.advObjectType()))
                    .limit(10)
                    .toList();
            System.out.println(market.getName());
            answer.forEach(System.out::println);
            System.out.println("Actions:");
            answerList.getList().stream()
                    .filter(c -> "ACTION".equals(c.advObjectType()))
                    .forEach(System.out::println);
//            CompletableFuture<OzonPerformanceAPI.StatisticResponse> f = market.getPerformanceAPI().getClientStatisticsFuture(new StatisticRequest(
//                    answer.stream().map(s -> Long.toString(s.id())).toArray(String[]::new),
//                    null,
//                    null,
//                    LocalDate.now(Clock.systemUTC()).minusDays(30),
//                    LocalDate.now(Clock.systemUTC()),
//                    StatisticRequest.GroupBy.DATE
//            ));
//            var val = f.get();
//            if (val.data() != null) {
//                Path p = Path.of("loaded/" + market.getName());
//                Files.copy(new ByteArrayInputStream(val.data()), p);
//            }
        }
        types.forEach(System.out::println);
    }

    @Test
    public void loadSelectedCampaign() throws IOException, ExecutionException, InterruptedException, OzonApiException {
        for (TestMarket m : markets) {
            if (!"ALADUS".equals(m.getName())) {
                continue;
            }
            AnswerList<CampaignAnswer> campaign = m.getPerformanceAPI().getClientCampaigns(new String[]{"2128103"}, null, null);
            System.out.println(campaign.getList().get(0));
            var f = m.getPerformanceAPI().getClientStatisticsFuture(new StatisticRequest(
                    new String[] {"2128103"},
                    null,
                    null,
                    LocalDate.of(2022, 12, 27),
                    LocalDate.of(2022, 12, 31),
                    StatisticRequest.GroupBy.DATE
            ));
            var answer = f.get();
            if (answer.data() != null) {
                Path p = Path.of("loaded/" + String.join(",",answer.status().request().campaigns()));
                Files.copy(new ByteArrayInputStream(answer.data()), p);
            }
            return;
        }
    }

}
