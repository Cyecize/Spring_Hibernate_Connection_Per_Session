package com.cyecize.multidb.areas.conn.enums;

import com.cyecize.multidb.MultiDbApplication;
import com.cyecize.multidb.areas.conn.services.ConnectionManager;
import com.cyecize.multidb.areas.conn.services.ConnectionManagerForMsSQL;
import com.cyecize.multidb.areas.conn.services.ConnectionManagerForMySQL;

public enum DatabaseProvider {
    MY_SQL(ConnectionManagerForMySQL.class), MS_SQL_SERVER(ConnectionManagerForMsSQL.class);

    private final Class<? extends ConnectionManager> connectionManagerType;

    DatabaseProvider(Class<? extends ConnectionManager> connectionManagerType) {
        this.connectionManagerType = connectionManagerType;
    }

    public ConnectionManager getConnectionManager() {
        if (MultiDbApplication.springContext == null) {
            throw new RuntimeException("Application not initialized");
        }

        return MultiDbApplication.springContext.getBean(this.connectionManagerType);
    }
}
