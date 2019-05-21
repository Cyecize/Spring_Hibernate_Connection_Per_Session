package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.areas.conn.models.UserDbConnection;

public interface DbConnectionStorageService {

    void closeAllConnections();

    void filterOldConnections();

    void addConnection(UserDbConnection dbConnection);
}
