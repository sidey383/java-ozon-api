package ru.sidey383.ozon.api.performance.objects.request.statistic.post;

import ru.sidey383.ozon.api.performance.objects.request.statistic.GroupBy;
import ru.sidey383.ozon.api.performance.objects.request.statistic.StatisticRequestData;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsyncClientStatisticVideoRequestBuilder {

    private final List<Long> campaigns = new ArrayList<>();
    private GroupBy groupBy;
    LocalDate dateFrom;
    LocalDate dateTo;

    public AsyncClientStatisticVideoRequestBuilder() {
    }

    public AsyncClientStatisticVideoRequestBuilder addCampaign(Long id) {
        if (campaigns.size() >= 10)
            throw new IllegalStateException("Too many campaigns");
        campaigns.add(id);
        return this;
    }

    public AsyncClientStatisticVideoRequestBuilder addCampaign(Long... ids) {
        if (campaigns.size() + ids.length > 10)
            throw new IllegalStateException("Too many campaigns");
        campaigns.addAll(Arrays.asList(ids));
        return this;
    }

    public AsyncClientStatisticVideoRequestBuilder setGroupBy(GroupBy groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public AsyncClientStatisticVideoRequestBuilder setPeriod(LocalDate dateFrom, LocalDate dateTo) {
        if (Duration.between(dateFrom, dateTo).toDays() > 62)
            throw new IllegalArgumentException("Period is too long");
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        return this;
    }

    public AsyncClientStatisticRequest build() {
        if (campaigns.isEmpty())
            throw new IllegalStateException("No company has been add");
        return new AsyncClientStatisticRequest(
                new StatisticRequestData(
                        campaigns.toArray(Long[]::new),
                        dateFrom,
                        dateTo,
                        groupBy
                ),
                "video"
        );
    }

}
