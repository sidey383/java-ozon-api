package ru.sidey383.ozon.api.seller.objects.request;

public record ClientStatisticsRequest(
        String[] campaigns,
        String from,
        String to,
        String dateFrom,
        String dateTo,
        GroupBy groupBy

) {

    public enum GroupBy {
        NO_GROUP_BY, DATE, START_OF_WEEK, START_OF_MONTH
    }

}
