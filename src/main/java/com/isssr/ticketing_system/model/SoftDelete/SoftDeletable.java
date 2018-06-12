package com.isssr.ticketing_system.model.SoftDelete;

public interface SoftDeletable {
    void delete();

    void restore();

    boolean isDeleted();
}


