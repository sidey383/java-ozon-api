package ru.sidey383.ozon.api.seller.objects.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidey383.ozon.api.container.ResultList;
import ru.sidey383.ozon.api.seller.JsonSellerAPIRequest;
import ru.sidey383.ozon.api.seller.objects.answer.fbo.FBOPosting;
import ru.sidey383.ozon.api.seller.objects.request.sub.DeliveryTimeFilter;
import ru.sidey383.ozon.api.seller.objects.request.sub.SortingOrder;

import java.time.Period;
import java.time.temporal.TemporalAmount;

/**
 * <a href="https://docs.ozon.ru/api/seller/#operation/PostingAPI_GetFboPostingList">API link</a>
 * **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FboListRequest extends JsonSellerAPIRequest<ResultList<FBOPosting>> {

    public static final long MAX_LIMIT = 1000;

    private static final TypeReference<ResultList<FBOPosting>> type = new TypeReference<>(){};

    private final Logger logger = LoggerFactory.getLogger(FboListRequest.class);

    @Nullable
    private final SortingOrder dir;
    @NotNull
    private final DeliveryTimeFilter filter;
    private final long limit;
    private final long offset;
    private final boolean translit;
    @Nullable
    private final With with;

    public static final TemporalAmount MAX_PERIOD = Period.ofDays(90);

    public FboListRequest(
            @Nullable SortingOrder dir,
            @NotNull DeliveryTimeFilter filter,
            long limit,
            long offset,
            boolean translit,
            @Nullable With with) {
        this.dir = dir;
        this.filter = filter;
        this.limit = limit;
        this.offset = offset;
        this.translit = translit;
        this.with = with;
    }

    @JsonGetter("dir")
    public @Nullable SortingOrder getDir() {
        return dir;
    }

    @JsonGetter("filter")
    public @NotNull DeliveryTimeFilter getFilter() {
        return filter;
    }

    @JsonGetter("limit")
    public long getLimit() {
        return limit;
    }

    @JsonGetter("offset")
    public long getOffset() {
        return offset;
    }

    @JsonGetter("translit")
    public boolean isTranslit() {
        return translit;
    }

    @JsonGetter("with")
    public @Nullable With getWith() {
        return with;
    }

    @Override
    @JsonIgnore
    protected @NotNull String getURL() {
        return "/v2/posting/fbo/list";
    }

    @Override
    @JsonIgnore
    protected @NotNull Logger getLogger() {
        return logger;
    }

    @Override
    @JsonIgnore
    public @NotNull TypeReference<ResultList<FBOPosting>> getTypeReference() {
        return type;
    }


    public record With(boolean analytics_data, boolean financial_data) {}

    @Override
    public String toString() {
        return "FboListRequest{" +
                "dir=" + dir +
                ", filter=" + filter +
                ", limit=" + limit +
                ", offset=" + offset +
                ", translit=" + translit +
                ", with=" + with +
                '}';
    }
}
