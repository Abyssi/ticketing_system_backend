package com.isssr.ticketing_system.response_entity;

import org.springframework.data.domain.Page;

public class PageResponseEntityBuilder extends HashMapResponseEntityBuilder {

    @SuppressWarnings("unchecked")
    public PageResponseEntityBuilder(Page page) {
        super();
        this.set("totalPages", page.getTotalPages())
                .setBuilder("content", new ListObjectResponseEntityBuilder(page.getContent()));
    }
}
