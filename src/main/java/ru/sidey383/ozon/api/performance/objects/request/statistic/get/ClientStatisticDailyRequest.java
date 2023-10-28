package ru.sidey383.ozon.api.performance.objects.request.statistic.get;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Getter
public class ClientStatisticDailyRequest extends ClientStatisticRequest {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private final Long[] campaignIds;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public ClientStatisticDailyRequest(@NotNull Long[] campaignIds, @NotNull LocalDate from, @NotNull LocalDate to) {
        this.campaignIds = campaignIds;
        this.dateFrom = from;
        this.dateTo = to;
    }

    @Override
    public String getType() {
        return "daily";
    }

    @Override
    protected Collection<ParamPair> queryParameters() {
        Collection<ParamPair> pairs = new ArrayList<>();
        for (Long id : campaignIds) {
            pairs.add(new ParamPair("campaignIds", Long.toString(id)));
        }
        pairs.add(new ParamPair("dateFrom", DATE_FORMATTER.format(dateFrom)));
        pairs.add(new ParamPair("dateTo", DATE_FORMATTER.format(dateTo)));
        return Collections.unmodifiableCollection(pairs);
    }
}
