package com.isssr.ticketing_system.model.auto_generated.db_metadata;

import lombok.Data;

@Data
public class Column {

    private String name;

    private boolean selected;

    public Column(String name) {
        this.name = name;

        this.selected = false;
    }
}
