package ru.sidey383.ozon.api.seller.objects.container;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class PageableItemList<T> {

    private List<T> items;

    private long total;

    private String lastID;

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

    @JsonGetter("last_id")
    public String getLastID() {
        return lastID;
    }

    @JsonSetter("last_id")
    public void setLastID(String lastID) {
        this.lastID = lastID;
    }

}
