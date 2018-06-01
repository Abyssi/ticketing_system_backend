package com.isssr.ticketing_system.response_entity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder<T> {

    protected T body;
    private HttpStatus status;

    public ResponseEntityBuilder() {
    }

    public ResponseEntityBuilder(T body) {
        this();

        this.body = body;
    }

    public ResponseEntityBuilder(HttpStatus status) {
        this();

        this.status = status;
    }

    public ResponseEntityBuilder(HttpStatus status, T body) {
        this();

        this.body = body;
        this.status = status;
    }

    public ResponseEntityBuilder<T> setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public T getBody() {
        return body;
    }

    public ResponseEntityBuilder<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public ResponseEntity<T> build() {
        return new ResponseEntity<>(this.body, this.status);
    }
}
