package com.isssr.ticketing_system.response_entity;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ListResponseEntityBuilder extends ResponseEntityBuilder<ArrayList<Object>> {

    public ListResponseEntityBuilder() {
        super(new ArrayList<>());
    }

    public ListResponseEntityBuilder(HttpStatus status) {
        this();
        this.setStatus(status);
    }

    public ListResponseEntityBuilder add(Object value) {
        this.body.add(value);
        return this;
    }

    public ListResponseEntityBuilder addAll(List<Object> list) {
        this.body.addAll(list);
        return this;
    }

}
