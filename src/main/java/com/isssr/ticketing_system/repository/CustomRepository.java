package com.isssr.ticketing_system.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Repository
public interface CustomRepository {

    <T> T customQuery(String query, JdbcTemplate jdbcTemplate, Class<T> returnType);

    List<String> getTablesMetadata(Connection connection, boolean isDefault) throws SQLException;

    List<String> getTableColumnsMetadata(Connection connection, String tableName) throws SQLException;

}
