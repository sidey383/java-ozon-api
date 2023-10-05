package ru.sidey383.ozon.api.seller.objects.container;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class ItemList<T> {
    private List<T> items;

    private long total;

    @JsonGetter("items")
    public List<T> getItems() {
        return items;
    }

    @JsonSetter("items")
    public void setItems(List<T> items) {
        this.items = items;
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
