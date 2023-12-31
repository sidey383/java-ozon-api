package ru.sidey383.ozon.api.container;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class AnswerList<T> {
    private List<T> list;

    private long total;

    @JsonGetter("list")
    public List<T> getList() {
        return list;
    }

    @JsonSetter("list")
    public void setList(List<T> list) {
        this.list = list;
    }

    @JsonGetter("total")
    public long getTotal() {
        return total;
    }

    @JsonSetter("total")
    public void setTotal(long total) {
        this.total = total;
    }
}
