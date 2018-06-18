package com.isssr.ticketing_system.model.db_connection;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "db_connection_info")
@Data
public class DBConnectionInfo {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "db_connection_info_id", updatable = false, nullable = false)
    private Long id;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    private String url;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    private String username;

    @NonNull
    private String password;


}
