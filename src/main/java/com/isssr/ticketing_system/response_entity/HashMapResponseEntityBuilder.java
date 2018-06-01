package com.isssr.ticketing_system.response_entity;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class HashMapResponseEntityBuilder extends ResponseEntityBuilder<HashMap<String, Object>> {

    public HashMapResponseEntityBuilder() {
        super(new HashMap<>());
    }

    public HashMapResponseEntityBuilder(HttpStatus status) {
        this();
        this.setStatus(status);
    }

    public HashMapResponseEntityBuilder set(String key, Object value) {
        this.body.put(key, value);
        return this;
    }
}
