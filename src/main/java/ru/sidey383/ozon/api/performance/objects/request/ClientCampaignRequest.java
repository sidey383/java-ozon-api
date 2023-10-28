package ru.sidey383.ozon.api.performance.objects.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.Nullable;
import ru.sidey383.ozon.api.BaseRequest;
import ru.sidey383.ozon.api.container.AnswerList;
import ru.sidey383.ozon.api.exception.OzonApiException;
import ru.sidey383.ozon.api.performance.PerformanceAPIRequest;
import ru.sidey383.ozon.api.performance.PerformanceAuth;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaignAnswer;
import ru.sidey383.ozon.api.performance.objects.answer.campaning.CampaignState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * <a href="https://performance.ozon.ru:443/api/client/campaign">/api/client/campaign</a>
 */
public class ClientCampaignRequest extends BaseRequest implements PerformanceAPIRequest<AnswerList<CampaignAnswer>> {

    private static final String url = "https://performance.ozon.ru/api/client/campaign";

    protected static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Nullable
    private final List<Long> campaignID;

    @Nullable
    private final String type;

    @Nullable
    private final CampaignState state;

    public ClientCampaignRequest(@Nullable List<Long> campaignID, @Nullable String type, @Nullable CampaignState state) {
        this.campaignID = campaignID;
        this.type = type;
        this.state = state;
    }

    public Builder builder() {
        return new Builder();
    }

    @Override
    public AnswerList<CampaignAnswer> runRequest(OkHttpClient client, PerformanceAuth auth) throws OzonApiException, IOException {
        HttpUrl httpUtl = HttpUrl.parse(url);
        if (httpUtl == null)
            throw new IllegalStateException("Can't parse http url " + url);
        HttpUrl.Builder urlBuilder = httpUtl.newBuilder();
        if (campaignID != null)
            campaignID.forEach(id -> urlBuilder.addQueryParameter("campaignIds", Long.toString(id)));
        if (type != null)
            urlBuilder.addQueryParameter("advObjectType", type);
        if (state != null)
            urlBuilder.addQueryParameter("state", state.name());
        Request request = new Request.Builder()
                .url(urlBuilder.build().url())
                .header(auth.getAuthorizeHeaderName(), auth.getAuthorizationHeaderValue())
                .get()
                .build();
        return mapper.readValue(executeRequest(client, request), new TypeReference<>() {});
    }


    public static class Builder {

        private final List<Long> campaignID = new ArrayList<>();

        private String type;

        private CampaignState state;

        private Builder() {
        }

        public Builder addCampaign(Long id) {
            campaignID.add(id);
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setState(CampaignState state) {
            this.state = state;
            return this;
        }

        public ClientCampaignRequest build() {
            return new ClientCampaignRequest(campaignID, type, state);
        }


    }

}
