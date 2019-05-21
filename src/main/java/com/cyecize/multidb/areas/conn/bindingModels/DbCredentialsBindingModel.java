package com.cyecize.multidb.areas.conn.bindingModels;

import com.cyecize.multidb.areas.conn.enums.DatabaseProvider;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DbCredentialsBindingModel {
    @NotNull
    private DatabaseProvider databaseProvider;

    @NotEmpty
    private String host;

    @NotNull
    @Min(0)
    private Integer port;

    @NotNull
    private String username;

    private String password;

    @NotEmpty
    private String databaseName;

    public DbCredentialsBindingModel() {

    }

    public DatabaseProvider getDatabaseProvider() {
        return this.databaseProvider;
    }

    public void setDatabaseProvider(DatabaseProvider databaseProvider) {
        this.databaseProvider = databaseProvider;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
