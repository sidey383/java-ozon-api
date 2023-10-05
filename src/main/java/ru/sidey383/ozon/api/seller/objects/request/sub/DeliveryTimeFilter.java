package ru.sidey383.ozon.api.seller.objects.request.sub;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.sidey383.ozon.api.seller.objects.DeliveryStatus;

import java.time.Instant;

/**
 * @see ru.sidey383.ozon.api.seller.objects.request.FboListRequest
 * **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DeliveryTimeFilter(
        @NotNull
        Instant since,
        @NotNull
        Instant to,
        @Nullable
        DeliveryStatus status
) {}
