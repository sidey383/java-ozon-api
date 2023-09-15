package ru.sidey383.ozon.api.seller.objects.answer.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sidey383.ozon.api.seller.objects.request.ClientStatisticsRequest;

import java.util.UUID;

public record StatisticsStatus(
        @JsonProperty("UUID")
        UUID uuid,
        State state,
        String createdAt,
        String updatedAt,
        ClientStatisticsRequest request,
        String error,
        String link,
        Kind kind
) {
    public enum State {
        NOT_STARTED(false), IN_PROGRESS(false), ERROR(true), OK(true);

        private boolean isFinal;

        State(boolean isFinal) {

        }

        public boolean isFinal() {
            return isFinal;
        }

    }

    public enum Kind {
        STATS, SEARCH_PHRASES, ATTRIBUTION, VIDEO
    }

}
