package com.sita.dto;

import java.util.List;

public class PagedResult<T> {
    private final int page;
    private final int size;
    private final int total;
    private final List<T> items;

    public PagedResult(int page, int size, int total, List<T> items) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.items = items;
    }

    public int getPage() { return page; }
    public int getSize() { return size; }
    public int getTotal() { return total; }
    public List<T> getItems() { return items; }
}

