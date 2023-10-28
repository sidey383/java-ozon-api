package ru.sidey383.ozon.api.performance.objects.request.statistic.get;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
public class ClientStatisticMediaRequest extends ClientStatisticRequest {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private final Long[] campaignIds;
    private final Instant from;
    private final Instant to;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public ClientStatisticMediaRequest(@NotNull Long[] campaignIds, @NotNull LocalDate from, @NotNull LocalDate to) {
        this.campaignIds = campaignIds;
        this.dateFrom = from;
        this.dateTo = to;
        this.from = null;
        this.to = null;
    }

    public ClientStatisticMediaRequest(@NotNull Long[] campaignIds, @NotNull Instant from, @NotNull Instant to) {
        this.campaignIds = campaignIds;
        this.dateFrom = null;
        this.dateTo = null;
        this.from = from;
        this.to = to;
    }

    @Override
    public String getType() {
        return "media";
    }

    @Override
    protected Collection<ParamPair> queryParameters() {
        Collection<ParamPair> pairs = new ArrayList<>();
        for (Long id : campaignIds) {
            pairs.add(new ParamPair("campaignIds", Long.toString(id)));
        }
        if (from != null && to != null) {
            pairs.add(new ParamPair("from", TIME_FORMATTER.format(from)));
            pairs.add(new ParamPair("to", TIME_FORMATTER.format(to)));
        }
        if (dateFrom != null && dateTo != null) {
            pairs.add(new ParamPair("dateFrom", DATE_FORMATTER.format(dateFrom)));
            pairs.add(new ParamPair("dateTo", DATE_FORMATTER.format(dateTo)));
        }
        return Collections.unmodifiableCollection(pairs);
    }
}
