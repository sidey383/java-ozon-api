package ru.sidey383.ozon.api.performance.objects.answer.statistic;
public record ReportInfo(
        String name,
        Company campaigns,
        StatisticStatus meta
) {

    public record Company(String id, String title) {}

}
