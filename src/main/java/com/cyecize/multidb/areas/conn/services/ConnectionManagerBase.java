package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.areas.conn.models.DbCredentials;
import com.cyecize.multidb.areas.conn.models.PersistenceUnitInfoImpl;
import com.cyecize.multidb.areas.conn.models.UserDbConnection;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class ConnectionManagerBase implements ConnectionManager {

    protected static final Properties COMMON_ORM_CONNECTION_PROPERTIES = new Properties();

    static {
        COMMON_ORM_CONNECTION_PROPERTIES.setProperty("hibernate.show_sql", "true");
        COMMON_ORM_CONNECTION_PROPERTIES.setProperty("hibernate.hbm2ddl.auto", "update");
    }

    private final DatabaseInitializeService databaseInitializeService;

    protected final ModelMapper modelMapper;

    protected ConnectionManagerBase(DatabaseInitializeService databaseInitializeService, ModelMapper modelMapper) {
        this.databaseInitializeService = databaseInitializeService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDbConnection connectWithORM(DbCredentials credentials, Collection<Class<?>> mappedEntities) {
        UserDbConnection connection = this.createORMConnection(credentials, mappedEntities);
        this.databaseInitializeService.initializeDatabase();
        return connection;
    }

    @Override
    public boolean testJdbcConnection(DbCredentials dbCredentials) {
        try (Connection connection = this.createJdbcConnection(dbCredentials)) {
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    protected abstract Connection createJdbcConnection(DbCredentials credentials) throws SQLException;

    protected abstract Properties getProviderSpecificProperties(DbCredentials credentials);

    private UserDbConnection createORMConnection(DbCredentials credentials, Collection<Class<?>> mappedEntities) {
        final UserDbConnection userDbConnection = new UserDbConnection(credentials);

        final String persistenceUnitName = UUID.randomUUID().toString();

        Properties connectionProperties = this.getProviderSpecificProperties(credentials);

        connectionProperties.setProperty("persistence-unit", persistenceUnitName);
        connectionProperties.setProperty("hibernate.connection.username", credentials.getUsername());
        if (credentials.getPassword() != null) {
            connectionProperties.setProperty("hibernate.connection.password", credentials.getPassword());
        }

        List<String> mappedEntitiesNames = mappedEntities.stream().map(Class::getName).collect(Collectors.toList());
        PersistenceUnitInfo persistenceUnitInfo = new PersistenceUnitInfoImpl(persistenceUnitName, mappedEntitiesNames, connectionProperties);

        EntityManagerFactory emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(persistenceUnitInfo, connectionProperties);

        userDbConnection.setOrmConnection(emf);
        userDbConnection.setEntityManager(emf.createEntityManager());

        return userDbConnection;
    }
}
