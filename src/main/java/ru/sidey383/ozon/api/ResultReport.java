package ru.sidey383.ozon.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.Instant;

public class ResultReport<A> {

    private A result;

    private Instant timestamp;

    @JsonGetter("result")
    public A getResult() {
        return result;
    }

    @JsonSetter("result")
    public void setResult(A result) {
        this.result = result;
    }

    @JsonGetter("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    public Instant getTimestamp() {
        return timestamp;
    }

    @JsonSetter("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
