package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.areas.conn.models.DbCredentials;
import com.cyecize.multidb.areas.conn.models.UserDbConnection;

import java.util.Collection;

public interface ConnectionManager {
    boolean testJdbcConnection(DbCredentials dbCredentials);

    UserDbConnection connectWithORM(DbCredentials credentials, Collection<Class<?>> mappedEntities);
}
