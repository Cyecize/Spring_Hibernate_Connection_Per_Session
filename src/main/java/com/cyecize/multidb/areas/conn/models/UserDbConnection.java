package com.cyecize.multidb.areas.conn.models;

import com.cyecize.multidb.areas.conn.services.ConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;

public class UserDbConnection {

    private DbCredentials credentials;

    private ConnectionManager connectionManager;

    private EntityManagerFactory ormConnection;

    private EntityManager entityManager;

    private long lastUpdatedTime;

    public UserDbConnection() {
        this.lastUpdatedTime = new Date().getTime();
    }

    public UserDbConnection(DbCredentials dbCredentials) {
        this();
        this.credentials = dbCredentials;
        this.connectionManager = dbCredentials.getDatabaseProvider().getConnectionManager();
    }

    public void closeConnections() {
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }

        if (this.ormConnection != null && this.ormConnection.isOpen()) {
            this.ormConnection.close();
        }

        this.ormConnection = null;
        this.entityManager = null;
    }

    public DbCredentials getCredentials() {
        return this.credentials;
    }

    public void setCredentials(DbCredentials credentials) {
        this.credentials = credentials;
    }

    public EntityManagerFactory getOrmConnection() {
        return this.ormConnection;
    }

    public void setOrmConnection(EntityManagerFactory ormConnection) {
        this.ormConnection = ormConnection;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public long getLastUpdatedTime() {
        return this.lastUpdatedTime;
    }

    public void setLastUpdatedTime(long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
