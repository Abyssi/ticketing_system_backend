package com.isssr.ticketing_system.response_entity;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public class PageResponseEntityBuilder extends HashMapResponseEntityBuilder {

    public PageResponseEntityBuilder() {
        super();
    }

    public PageResponseEntityBuilder(HttpStatus status) {
        super(status);
    }

    public PageResponseEntityBuilder(Page page) {
        super();
        this.setPage(page);
    }

    public PageResponseEntityBuilder(HttpStatus status, Page page) {
        this(status);
        this.setPage(page);
    }

    public PageResponseEntityBuilder setPage(Page page) {
        this.set("totalPages", page.getTotalPages()).set("content", page.getContent());
        return this;
    }
}
