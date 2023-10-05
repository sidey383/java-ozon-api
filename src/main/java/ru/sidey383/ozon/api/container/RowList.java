package ru.sidey383.ozon.api.container;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class RowList<A> {

    private List<A> rows;

    @JsonGetter("rows")
    public List<A> getRows() {
        return rows;
    }

    @JsonSetter("rows")
    public void setRows(List<A> rows) {
        this.rows = rows;
    }
}
