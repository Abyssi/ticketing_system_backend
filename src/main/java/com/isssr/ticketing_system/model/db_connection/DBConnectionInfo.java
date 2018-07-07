package com.isssr.ticketing_system.model.db_connection;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "db_connection_info")
public class DBConnectionInfo {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "db_connection_info_id", updatable = false, nullable = false)
    private Long id;

    @JsonView(JsonViews.Detailed.class)
    private String url;

    @JsonView(JsonViews.Detailed.class)
    private String username;


    private String password;

    private String driver;

    public DBConnectionInfo(String url, String username, String password) {

        this.setUrl(url);

        this.username = username;
        this.password = password;

    }

    public void setUrl(String url) {

        this.url = url;

        if (this.url != null) {

            if (this.url.matches("jdbc:mysql:.*")) {
                this.driver = "com.mysql.jdbc.Driver";

                return;
            }

        }

        this.driver = "org.postgresql.Driver";
    }
}
