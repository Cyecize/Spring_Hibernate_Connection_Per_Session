package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.areas.conn.models.DbCredentials;
import com.cyecize.multidb.areas.conn.models.SQLServerCustomDialect;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ConnectionManagerForMsSQL extends ConnectionManagerBase {
    private static final Properties ORM_CONNECTION_SQL_SERVER_PROPERTIES = new Properties();

    static {
        ORM_CONNECTION_SQL_SERVER_PROPERTIES.putAll(COMMON_ORM_CONNECTION_PROPERTIES);
        ORM_CONNECTION_SQL_SERVER_PROPERTIES.put("hibernate.connection.driver_class", SQLServerDriver.class.getName());
        ORM_CONNECTION_SQL_SERVER_PROPERTIES.put("hibernate.dialect", SQLServerCustomDialect.class.getName());
    }

    public ConnectionManagerForMsSQL(DatabaseInitializeService databaseInitializeService, ModelMapper modelMapper) {
        super(databaseInitializeService, modelMapper);
    }

    @Override
    protected Connection createJdbcConnection(DbCredentials credentials) throws SQLException {
        SQLServerDataSource dataSource = new SQLServerDataSource();

        dataSource.setUser(credentials.getUsername());
        dataSource.setPassword(credentials.getPassword());
        dataSource.setServerName(credentials.getHost());
        dataSource.setPortNumber(credentials.getPort());
        dataSource.setLoginTimeout(2);

        return dataSource.getConnection();
    }

    @Override
    protected Properties getProviderSpecificProperties(DbCredentials credentials) {
        Properties properties = new Properties();
        properties.putAll(ORM_CONNECTION_SQL_SERVER_PROPERTIES);

        final String connectionString = String.format(
                "jdbc:sqlserver://%s:%d;DatabaseName=%s",
                credentials.getHost(),
                credentials.getPort(),
                credentials.getDatabaseName()
        );

        properties.put("hibernate.connection.url", connectionString);

        //TODO: Replace with MySQL createDatabaseIfNotExists equivalent.
        this.createDatabaseIfNotExists(credentials);

        return properties;
    }

    private void createDatabaseIfNotExists(DbCredentials credentials) {
        try (Connection connection = this.createJdbcConnection(credentials)) {

            //If there is no database, create one
            if (this.getAllDatabaseNames(connection).stream().noneMatch(dbName -> dbName.toLowerCase().equals(credentials.getDatabaseName().toLowerCase()))) {
                connection.prepareStatement("CREATE DATABASE " + credentials.getDatabaseName()).execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getAllDatabaseNames(Connection connection) {
        final String databaseAlias = "Database";
        final String queryForGettingAllSchemas = String.format("SELECT [db].name AS [%s] FROM master.sys.databases AS [db]", databaseAlias);

        List<String> dbNames = new ArrayList<>();

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(queryForGettingAllSchemas)) {
            while (resultSet.next()) {
                dbNames.add(resultSet.getString(databaseAlias));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dbNames;
    }
}
