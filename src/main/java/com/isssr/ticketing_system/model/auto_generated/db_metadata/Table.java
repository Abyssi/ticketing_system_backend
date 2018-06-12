package com.isssr.ticketing_system.model.auto_generated.db_metadata;

import lombok.Data;

import java.util.List;

@Data
public class Table {

    private String name;

    private boolean selected;

    private List<Column> columns;

    public Table(String name) {
        this.name = name;

        this.selected = false;
    }
}
