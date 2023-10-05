package ru.sidey383.ozon.api;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SingleResultContainer<A> {

    private A result;

    @JsonGetter("result")
    public A getResult() {
        return result;
    }

    @JsonSetter("result")
    public void setResult(A result) {
        this.result = result;
    }
}
