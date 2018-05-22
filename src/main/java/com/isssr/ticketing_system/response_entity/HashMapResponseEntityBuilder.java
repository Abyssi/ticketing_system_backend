package com.isssr.ticketing_system.response_entity;

import java.util.HashMap;

public class HashMapResponseEntityBuilder extends ResponseEntityBuilder<HashMap> {

    public HashMapResponseEntityBuilder() {
        this.body = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public HashMapResponseEntityBuilder set(String key, Object value) {
        this.body.put(key, value);
        return this;
    }

    public HashMapResponseEntityBuilder setBuilder(String key, ResponseEntityBuilder responseEntityBuilder) {
        return this.set(key, responseEntityBuilder.getBody());
    }
}
