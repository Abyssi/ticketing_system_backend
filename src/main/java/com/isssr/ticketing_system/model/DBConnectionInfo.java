package com.isssr.ticketing_system.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NonNull;

@Data
public class DBConnectionInfo {

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String url;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private String username;

    @NonNull
    private String password;
}
