package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.areas.conn.models.DbCredentials;
import com.mysql.jdbc.Driver;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.hibernate.dialect.MySQL57Dialect;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Service
public class ConnectionManagerForMySQL extends ConnectionManagerBase {
    private static final Properties ORM_MYSQL_PROPERTIES = new Properties();

    static {
        ORM_MYSQL_PROPERTIES.putAll(COMMON_ORM_CONNECTION_PROPERTIES);
        ORM_MYSQL_PROPERTIES.setProperty("hibernate.dialect", MySQL57Dialect.class.getName());
        ORM_MYSQL_PROPERTIES.setProperty("hibernate.connection.driver_class", Driver.class.getName());
    }

    public ConnectionManagerForMySQL(DatabaseInitializeService databaseInitializeService, ModelMapper modelMapper) {
        super(databaseInitializeService, modelMapper);
    }

    @Override
    protected Connection createJdbcConnection(DbCredentials credentials) throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setUser(credentials.getUsername());
        dataSource.setPassword(credentials.getPassword());
        dataSource.setServerName(credentials.getHost());
        dataSource.setPort(credentials.getPort());
        dataSource.setUseSSL(false);
        dataSource.setLoginTimeout(2);
        dataSource.setConnectTimeout(2);

        return dataSource.getConnection();
    }

    @Override
    protected Properties getProviderSpecificProperties(DbCredentials credentials) {
        Properties properties = new Properties();
        properties.putAll(ORM_MYSQL_PROPERTIES);

        final String connectionString = String.format(
                "jdbc:mysql://%s:%d/%s?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false",
                credentials.getHost(),
                credentials.getPort(),
                credentials.getDatabaseName());

        properties.setProperty("hibernate.connection.url", connectionString);

        return properties;
    }
}
