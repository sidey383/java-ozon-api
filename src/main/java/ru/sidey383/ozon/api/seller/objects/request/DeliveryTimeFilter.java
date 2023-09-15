package ru.sidey383.ozon.api.seller.objects.request;

import ru.sidey383.ozon.api.seller.objects.DeliveryStatus;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public record DeliveryTimeFilter(String since, String to, DeliveryStatus status) {

    private final static DateTimeFormatter format = DateTimeFormatter.ISO_INSTANT;

    public DeliveryTimeFilter(TemporalAccessor since, TemporalAccessor to, DeliveryStatus status) {
        this(format.format(since), format.format(to), status);
    }

    public enum Status {

    }

}
