package ru.sidey383.ozon.api.performance.objects.request.statistic;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientStatisticVideoBuilder {

    private final List<Long> campaigns = new ArrayList<>();
    private GroupBy groupBy;
    LocalDate dateFrom;
    LocalDate dateTo;

    public ClientStatisticVideoBuilder() {
    }

    public ClientStatisticVideoBuilder addCampaign(Long id) {
        if (campaigns.size() >= 10)
            throw new IllegalStateException("Too many campaigns");
        campaigns.add(id);
        return this;
    }

    public ClientStatisticVideoBuilder addCampaign(Long... ids) {
        if (campaigns.size() + ids.length > 10)
            throw new IllegalStateException("Too many campaigns");
        campaigns.addAll(Arrays.asList(ids));
        return this;
    }

    public ClientStatisticVideoBuilder setGroupBy(GroupBy groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public ClientStatisticVideoBuilder setPeriod(LocalDate dateFrom, LocalDate dateTo) {
        if (Duration.between(dateFrom, dateTo).toDays() > 62)
            throw new IllegalArgumentException("Period is too long");
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        return this;
    }

    public ClientStatisticRequest build() {
        if (campaigns.isEmpty())
            throw new IllegalStateException("No company has been add");
        return new ClientStatisticRequest(
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
