package com.isssr.ticketing_system.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Implementation of custom repository for executing custom queries
@Repository
@Transactional(readOnly = true)
public class CustomRepositoryImp implements CustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List customQuery(String query) {
        javax.persistence.Query customQuery = entityManager.createNativeQuery(query);
        return customQuery.getResultList();
    }

    @Override
    public List<String> getTablesMetadata(Connection connection) throws SQLException {

        DatabaseMetaData md = connection.getMetaData();

        ResultSet rs = md.getTables(null, null, "ts_%", new String[]{"TABLE"});

        List<String> tableNames = new ArrayList<>();

        while (rs.next()) {

            tableNames.add(rs.getString("TABLE_NAME"));

        }

        return tableNames;

    }

    @Override
    public List<String> getTableColumnsMetadata(Connection connection, String tableName) throws SQLException {

        DatabaseMetaData md = connection.getMetaData();

        ResultSet rs = md.getColumns(null, null, tableName, null);

        List<String> columns = new ArrayList<>();

        while (rs.next()) {

            columns.add(rs.getString("COLUMN_NAME"));

        }

        return columns;
    }
}
