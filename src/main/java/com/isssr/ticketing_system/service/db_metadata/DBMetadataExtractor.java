package com.isssr.ticketing_system.service.db_metadata;

import com.isssr.ticketing_system.model.auto_generated.db_metadata.Column;
import com.isssr.ticketing_system.model.auto_generated.db_metadata.Table;
import com.isssr.ticketing_system.repository.CustomRepositoryImp;
import com.isssr.ticketing_system.service.UserSwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBMetadataExtractor {

    @Autowired
    private CustomRepositoryImp customRepositoryImp;

    @Autowired
    private UserSwitchService userSwitchService;


    public List<Table> getTableMetadata() throws SQLException {

        return this.getTableMetadata(null, null, null, null);

    }

    public List<Column> getTableColumns(String tableName) throws SQLException {

        return this.getTableColumns(tableName, null, null, null, null);

    }


    public List<Table> getTableMetadata(@Nullable String dbURL, @Nullable String dbUsername, @Nullable String dbPassword, @Nullable String dbDriver) throws SQLException {

        //if dbUrl is null, this is a default request
        boolean isDefault = dbURL == null;

        Connection connection = this.userSwitchService.getReadOnlyConnection(dbURL, dbUsername, dbPassword, dbDriver);

        List<String> tableNames = this.customRepositoryImp.getTablesMetadata(connection, isDefault);

        connection.close();

        List<Table> tables = new ArrayList<>();

        for (String tableName : tableNames) {

            Table table = new Table(tableName);

            tables.add(table);

        }

        return tables;


    }

    public List<Column> getTableColumns(String tableName, @Nullable String dbURL, @Nullable String dbUsername, @Nullable String dbPassword, @Nullable String dbDriver) throws SQLException {

        Connection connection = this.userSwitchService.getReadOnlyConnection(dbURL, dbUsername, dbPassword, dbDriver);

        List<String> columnNames = this.customRepositoryImp.getTableColumnsMetadata(connection, tableName);

        connection.close();

        List<Column> columns = new ArrayList<>();

        for (String columnName : columnNames) {

            Column column = new Column(columnName);

            columns.add(column);

        }

        return columns;
    }

}
