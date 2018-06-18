package com.isssr.ticketing_system.service;

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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Configuration
@Component
@Service
public class UserSwitchService {

    @Value("${readOnly.mode}")
    private String USER_MODE;

    @Value("${root.mode}")
    private String ROOT_MODE;

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

    //Use a read only user to do job
    public List doQueryReadOnlyMode(String query) {
        //Go with the connection of a new user
        this.jdbcTemplate.setDataSource(getDataSource(USER_MODE));
        Connection connection;
        List list = null;
        try {
            connection = this.jdbcTemplate.getDataSource().getConnection();
            list = this.customRepository.customQuery(query);
            connection.close();
        } catch (Exception e) {
            System.out.println("Query not allowed");
        }
        return list;
    }

    public Connection getReadOnlyConnection() throws SQLException {

        this.jdbcTemplate.setDataSource(getDataSource(USER_MODE));

        return this.jdbcTemplate.getDataSource().getConnection();
    }

    //Get Connection with a 'read only' user to db
    private DataSource getDataSource(@Nullable String dbURL, String mode) {

        if (dbURL == null)
            dbURL = this.url;

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(dbURL);

        //Connect read-only privilege
        if (mode.equals(USER_MODE)) {
            dataSource.setUsername(USER_USERNAME);
            dataSource.setPassword(USER_PASSWORD);
        }
        //Connect root privilege
        else if (mode.equals(ROOT_MODE)) {
            dataSource.setUsername(ROOT_USERNAME);
            dataSource.setPassword(ROOT_PASSWORD);
        }
        return dataSource;
    }
}
