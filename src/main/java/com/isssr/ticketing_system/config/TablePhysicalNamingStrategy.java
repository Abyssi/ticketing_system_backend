package com.isssr.ticketing_system.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
public class TablePhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl implements Serializable {

    @Value("${db.table.prefix}")
    private String TABLE_PREFIX;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return new Identifier(addPrefix(name.getText()), name.isQuoted());
    }


    private String addPrefix(String name) {

        return TABLE_PREFIX + name;

    }
}