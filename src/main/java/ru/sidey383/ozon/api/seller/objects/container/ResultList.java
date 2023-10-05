package ru.sidey383.ozon.api.seller.objects.container;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class ResultList<T> {

    private List<T> result;

    @JsonGetter("result")
    public List<T> getResult() {
        return result;
    }

    @JsonSetter("result")
    public void setResult(List<T> result) {
        this.result = result;
    }

}
