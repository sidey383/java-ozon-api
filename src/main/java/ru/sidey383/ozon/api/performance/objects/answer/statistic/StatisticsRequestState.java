package ru.sidey383.ozon.api.performance.objects.answer.statistic;

public enum StatisticsRequestState {
    NOT_STARTED(false),
    IN_PROGRESS(false),
    ERROR(true),
    OK(true);

    private final boolean isTerminate;


    StatisticsRequestState(boolean isTerminate) {
        this.isTerminate = isTerminate;
    }

    public boolean isTerminate() {
        return isTerminate;
    }
}
