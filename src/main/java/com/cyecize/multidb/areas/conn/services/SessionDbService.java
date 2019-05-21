package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.areas.conn.models.UserDbConnection;

public interface SessionDbService {

    void sendKeepAlive();

    void setConnection(UserDbConnection dbConnection);

    boolean hasOpenConnection();

    UserDbConnection getConnection();
}
