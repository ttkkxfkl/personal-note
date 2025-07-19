package com.personalnote.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResult<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private boolean first;
    private boolean last;
    private boolean empty;

    public PageResult() {}

    public PageResult(Page<T> page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.empty = page.isEmpty();
    }

    public static <T> PageResult<T> of(Page<T> page) {
        return new PageResult<>(page);
    }
}