package ru.sidey383.ozon.api;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class AnswerList<T> {
    private List<T> list;

    private long total;

    @JsonGetter
    public List<T> getList() {
        return list;
    }

    @JsonSetter
    public void setList(List<T> list) {
        this.list = list;
    }

    @JsonGetter
    public long getTotal() {
        return total;
    }

    @JsonSetter
    public void setTotal(long total) {
        this.total = total;
    }
}
