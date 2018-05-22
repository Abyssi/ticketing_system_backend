package com.isssr.ticketing_system.response_entity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder<T> {

    protected T body;
    private HttpStatus status;

    public ResponseEntityBuilder setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public T getBody() {
        return body;
    }

    public ResponseEntity<T> build() {
        return new ResponseEntity<>(this.body, this.status);
    }

}
