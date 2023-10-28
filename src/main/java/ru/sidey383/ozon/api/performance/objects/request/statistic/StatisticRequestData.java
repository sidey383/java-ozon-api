package ru.sidey383.ozon.api.performance.objects.request.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticRequestData {
    private Long[] campaigns;
    private Instant from;
    private Instant to;
    private Long[] objects;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private GroupBy groupBy;

    public StatisticRequestData() {}

    public StatisticRequestData(Long[] campaigns, Instant from, Instant to, LocalDate dateFrom, LocalDate dateTo, GroupBy groupBy) {
        this.campaigns = campaigns;
        this.from = from;
        this.to = to;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.groupBy = groupBy;
    }

    public StatisticRequestData(Long[] campaigns, LocalDate dateFrom, LocalDate dateTo, GroupBy groupBy) {
        this.campaigns = campaigns;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.groupBy = groupBy;
    }

    public StatisticRequestData(Long[] campaigns, Instant from, Instant to, Long[] objects, LocalDate dateFrom, LocalDate dateTo) {
        this.campaigns = campaigns;
        this.from = from;
        this.to = to;
        this.objects = objects;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public StatisticRequestData(Long[] campaigns, Instant from, Instant to, Long[] objects, LocalDate dateFrom, LocalDate dateTo, GroupBy groupBy) {
        this.campaigns = campaigns;
        this.from = from;
        this.to = to;
        this.objects = objects;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.groupBy = groupBy;
    }
}
