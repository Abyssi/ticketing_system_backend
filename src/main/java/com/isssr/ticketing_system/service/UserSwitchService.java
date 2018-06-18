package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.db_connection.DBConnectionModeEnum;
import com.isssr.ticketing_system.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.validation.constraints.Null;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Configuration
@Component
@Service
public class UserSwitchService {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${root.mode.username}")
    private String ROOT_USERNAME;

    @Value("${root.mode.password}")
    private String ROOT_PASSWORD;

    @Value("${readOnly.mode.username}")
    private String USER_USERNAME;

    @Value("${readOnly.mode.password}")
    private String USER_PASSWORD;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CustomRepository customRepository;


    public List doQueryReadOnlyMode(String query) {

        return this.doQueryReadOnlyMode(query, null, null, null);

    }

    //Use a read only user to do job
    public List doQueryReadOnlyMode(String query, @Nullable String dbURL, @Nullable String dbUsername, @Nullable String dbPassword) {
        //Go with the connection of a new user
        this.jdbcTemplate.setDataSource(getDataSource(dbURL, dbUsername, dbPassword, DBConnectionModeEnum.READ_ONLY_MODE));
        Connection connection;
        List list = null;
        try {
            connection = this.jdbcTemplate.getDataSource().getConnection();
            list = this.customRepository.customQuery(query);
            connection.close();
        } catch (SQLException e) {
            System.out.println("SQL connection error: " + e.getMessage());
        }
        return list;
    }

    public Connection getReadOnlyConnection(@Nullable String dbURL, @Nullable String dbUsername, @Nullable String dbPassword) throws SQLException {

        this.jdbcTemplate.setDataSource(getDataSource(dbURL, dbUsername, dbPassword, DBConnectionModeEnum.READ_ONLY_MODE));

        return this.jdbcTemplate.getDataSource().getConnection();
    }

    //Get Connection with a 'read only' user to db
    private DataSource getDataSource(@Nullable String dbURL, @Nullable String dbUsername, @Nullable String dbPassword, DBConnectionModeEnum mode) {

        if (dbURL == null)
            dbURL = this.url; //set default url

        if (dbUsername == null)
            switch (mode) { //set default username
                case READ_ONLY_MODE:
                    dbUsername = this.USER_USERNAME;
                    break;
                case ROOT_MODE:
                    dbUsername = this.ROOT_USERNAME;
            }

        if (dbPassword == null)
            switch (mode) { //set default password
                case READ_ONLY_MODE:
                    dbPassword = this.USER_PASSWORD;
                    break;
                case ROOT_MODE:
                    dbPassword = this.ROOT_PASSWORD;
            }


        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(dbURL);

        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }
}
