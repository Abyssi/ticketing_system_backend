package com.isssr.ticketing_system.service.db_metadata;

import com.isssr.ticketing_system.repository.CustomRepositoryImp;
import com.isssr.ticketing_system.service.UserSwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class DBMetadataExtractor {

    @Autowired
    private CustomRepositoryImp customRepositoryImp;

    @Autowired
    private UserSwitchService userSwitchService;


    public List<String> getTableMetadata() throws SQLException {

        Connection connection = this.userSwitchService.getReadOnlyConnection();

        List<String> tableNames = this.customRepositoryImp.getTablesMetadata(connection);

        connection.close();

        return tableNames;


    }

    public List<String> getTableColumns(String tableName) throws SQLException {

        Connection connection = this.userSwitchService.getReadOnlyConnection();

        List<String> columns = this.customRepositoryImp.getTableColumnsMetadata(connection, tableName);

        connection.close();

        return columns;
    }

}
