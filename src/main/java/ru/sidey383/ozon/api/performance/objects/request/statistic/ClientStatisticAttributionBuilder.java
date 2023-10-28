package ru.sidey383.ozon.api.performance.objects.request.statistic;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <a href="https://docs.ozon.ru/api/performance/#operation/AttributionSubmitRequest">"/api/client/statistics/attribution"</>
 **/
public class ClientStatisticAttributionBuilder {

    private final List<Long> campaigns = new ArrayList<>();
    private GroupBy groupBy;
    private Instant from;
    private Instant to;
    LocalDate dateFrom;
    LocalDate dateTo;

    public ClientStatisticAttributionBuilder() {
    }

    public ClientStatisticAttributionBuilder addCampaign(Long id) {
        if (campaigns.size() >= 10)
            throw new IllegalStateException("Too many campaigns");
        campaigns.add(id);
        return this;
    }

    public ClientStatisticAttributionBuilder addCampaign(Long... ids) {
        if (campaigns.size() + ids.length > 10)
            throw new IllegalStateException("Too many campaigns");
        campaigns.addAll(Arrays.asList(ids));
        return this;
    }

    public ClientStatisticAttributionBuilder setGroupBy(GroupBy groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public ClientStatisticAttributionBuilder setPeriod(LocalDate dateFrom, LocalDate dateTo) {
        if (Duration.between(dateFrom, dateTo).toDays() > 62)
            throw new IllegalArgumentException("Period is too long");
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        return this;
    }

    public ClientStatisticAttributionBuilder setPeriod(Instant from, Instant to) {
        if (Duration.between(LocalDate.ofInstant(from, ZoneOffset.UTC), LocalDate.ofInstant(from, ZoneOffset.UTC)).toDays() > 62)
            throw new IllegalArgumentException("Period is too long");
        this.from = from;
        this.to = to;
        return this;
    }

    public ClientStatisticRequest build() {
        if (campaigns.isEmpty())
            throw new IllegalStateException("No company has been add");
        return new ClientStatisticRequest(
                new StatisticRequestData(
                        campaigns.toArray(Long[]::new),
                        from,
                        to,
                        dateFrom,
                        dateTo,
                        groupBy
                ),
                "attribution"
        );
    }

}
