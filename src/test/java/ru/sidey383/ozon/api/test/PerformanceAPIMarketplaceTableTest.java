package ru.sidey383.ozon.api.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.container.AnswerList;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaignAnswer;
import ru.sidey383.ozon.api.performance.objects.request.ClientCampaignRequest;
import ru.sidey383.ozon.api.performance.objects.request.statistic.GroupBy;
import ru.sidey383.ozon.api.performance.objects.request.statistic.post.AsyncClientStatisticRequestBuilder;
import ru.sidey383.ozon.api.performance.objects.request.statistic.post.AsyncClientStatisticRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PerformanceAPIMarketplaceTableTest {

    private final Logger logger = LoggerFactory.getLogger(PerformanceAPIMarketplaceTableTest.class);

    private static List<TestMarket> markets;

    @BeforeAll
    public static void init() throws IOException {
        markets = new ObjectMapper().readValue(SalesAPITest.class.getResource("/markets.json"), new TypeReference<>() {
        });
    }

    @Test
    public void loadCampaigns() throws OzonApiException, IOException {
        Set<String> types = new HashSet<>();
        for (TestMarket market : markets) {
            ClientCampaignRequest request = new ClientCampaignRequest(null, null, null);
            AnswerList<CampaignAnswer> answerList = market.getPerformanceAPI().runRequest(request);
            types.addAll(answerList.getList().stream().map(CampaignAnswer::advObjectType).collect(Collectors.toSet()));
            System.out.println(market.getName());
            answerList.getList().forEach(System.out::println);
        }
        types.forEach(System.out::println);
    }

    @Test
    public void loadSelectedCampaign() throws Throwable {
        TestMarket m = markets.get(0);
        ClientCampaignRequest request1 = new ClientCampaignRequest(null, null, null);
        AnswerList<CampaignAnswer> campaign = m.getPerformanceAPI().runRequest(request1);
        CampaignAnswer campaignAnswer = campaign.getList().get(0);
        AsyncClientStatisticRequest request2 = new AsyncClientStatisticRequestBuilder()
                .addCampaign(campaignAnswer.id())
                .setPeriod(
                        LocalDate.of(2022, 12, 27),
                        LocalDate.of(2022, 12, 31)
                )
                .setGroupBy(GroupBy.DATE)
                .build();
        var f = m.getPerformanceAPI().runAsyncRequest(request2);
        var answer = f.get();
        if (answer.exception() != null)
            throw answer.exception();
        Assertions.assertNotNull(answer.data());
    }

}
