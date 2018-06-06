package com.isssr.ticketing_system.repository;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Repository
public interface CustomRepository {

    List customQuery(String query);

    List<String> getTablesMetadata(Connection connection) throws SQLException;

    List<String> getTableColumnsMetadata(Connection connection, String tableName) throws SQLException;

}
