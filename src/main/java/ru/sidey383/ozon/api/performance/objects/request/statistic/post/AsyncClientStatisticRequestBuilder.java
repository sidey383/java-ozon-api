package ru.sidey383.ozon.api.performance.objects.request.statistic.post;

import ru.sidey383.ozon.api.performance.objects.request.statistic.GroupBy;
import ru.sidey383.ozon.api.performance.objects.request.statistic.StatisticRequestData;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsyncClientStatisticRequestBuilder {

    private final List<Long> campaigns = new ArrayList<>();
    private GroupBy groupBy;
    private Instant from;
    private Instant to;
    LocalDate dateFrom;
    LocalDate dateTo;

    public AsyncClientStatisticRequestBuilder() {
    }

    public AsyncClientStatisticRequestBuilder addCampaign(Long id) {
        if (campaigns.size() >= 10)
            throw new IllegalStateException("Too many campaigns");
        campaigns.add(id);
        return this;
    }

    public AsyncClientStatisticRequestBuilder addCampaign(Long... ids) {
        if (campaigns.size() + ids.length > 10)
            throw new IllegalStateException("Too many campaigns");
        campaigns.addAll(Arrays.asList(ids));
        return this;
    }

    public AsyncClientStatisticRequestBuilder setGroupBy(GroupBy groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public AsyncClientStatisticRequestBuilder setPeriod(LocalDate dateFrom, LocalDate dateTo) {
        if (ChronoUnit.DAYS.between(dateFrom, dateTo) > 62)
            throw new IllegalArgumentException("Period is too long");
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        return this;
    }

    public AsyncClientStatisticRequestBuilder setPeriod(Instant from, Instant to) {
        if (Duration.between(LocalDate.ofInstant(from, ZoneOffset.UTC), LocalDate.ofInstant(from, ZoneOffset.UTC)).toDays() > 62)
            throw new IllegalArgumentException("Period is too long");
        this.from = from;
        this.to = to;
        return this;
    }

    public AsyncClientStatisticRequest build() {
        if (campaigns.isEmpty())
            throw new IllegalStateException("No company has been add");
        return new AsyncClientStatisticRequest(
                new StatisticRequestData(
                        campaigns.toArray(Long[]::new),
                        from,
                        to,
                        dateFrom,
                        dateTo,
                        groupBy
                ),
                ""
        );
    }

}
