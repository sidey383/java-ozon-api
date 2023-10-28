package ru.sidey383.ozon.api.seller.objects.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.container.ResultReport;
import ru.sidey383.ozon.api.seller.JsonSellerAPIRequest;
import ru.sidey383.ozon.api.seller.objects.answer.product.AnalyticsDataResult;
import ru.sidey383.ozon.api.seller.objects.request.sub.*;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * <a href="https://docs.ozon.ru/api/seller/#tag/AnalyticsAPI">API link</a>
 * **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnalyticsDataRequest extends JsonSellerAPIRequest<ResultReport<AnalyticsDataResult>> {

    public static final long MAX_LIMIT = 1000;

    private static final TypeReference<ResultReport<AnalyticsDataResult>> type = new TypeReference<>(){};

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsDataRequest.class);

    @NotNull
    private final LocalDate dateFrom;
    @NotNull
    private final LocalDate dateTo;
    @NotNull
    private final AnalyticsDimensions[] dimension;
    private final Metric[] metrics;
    @Nullable
    private final AnalyticsFilter[] filters;
    private final long limit;
    private final long offset;
    @Nullable
    private final MetricsSorting[] sort;

    public AnalyticsDataRequest(
            @NotNull LocalDate dateFrom,
            @NotNull LocalDate dateTo,
            @NotNull AnalyticsDimensions[] dimension,
            @NotNull Metric[] metrics,
            @Nullable AnalyticsFilter[] filters,
            long limit,
            long offset,
            @Nullable MetricsSorting[] sort) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.dimension = dimension;
        this.metrics = metrics;
        this.filters = filters;
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    @JsonGetter("date_from")
    public @NotNull LocalDate getDateFrom() {
        return dateFrom;
    }

    @JsonGetter("date_to")
    public @NotNull LocalDate getDateTo() {
        return dateTo;
    }

    @JsonGetter("dimension")
    public @NotNull AnalyticsDimensions[] getDimension() {
        return dimension;
    }

    @JsonGetter("metrics")
    public Metric[] getMetrics() {
        return metrics;
    }

    @JsonGetter("filters")
    public @Nullable AnalyticsFilter[] getFilters() {
        return filters;
    }

    @JsonGetter("limit")
    public long getLimit() {
        return limit;
    }

    @JsonGetter("offset")
    public long getOffset() {
        return offset;
    }

    @JsonGetter("sort")
    public @Nullable MetricsSorting[] getSort() {
        return sort;
    }

    @Override
    @JsonIgnore
    protected @NotNull String getURL() {
        return "/v1/analytics/data";
    }

    @Override
    @JsonIgnore
    public @NotNull TypeReference<ResultReport<AnalyticsDataResult>> getTypeReference() {
        return type;
    }

    @Override
    public String toString() {
        return "AnalyticsDataRequest{" +
                "dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", dimension=" + Arrays.toString(dimension) +
                ", metrics=" + Arrays.toString(metrics) +
                ", filters=" + Arrays.toString(filters) +
                ", limit=" + limit +
                ", offset=" + offset +
                ", sort=" + Arrays.toString(sort) +
                '}';
    }
}
