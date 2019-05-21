package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.areas.conn.models.UserDbConnection;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionDbServiceImpl implements SessionDbService {
    private UserDbConnection dbConnection;

    public SessionDbServiceImpl() {

    }

    @Override
    public void setConnection(UserDbConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.updateDbConnectionTime();
    }

    @Override
    public boolean hasOpenConnection() {
        return this.getConnection() != null &&
                this.getConnection().getOrmConnection() != null &&
                this.getConnection().getOrmConnection().isOpen();
    }

    @Override
    public UserDbConnection getConnection() {
        this.updateDbConnectionTime();
        return this.dbConnection;
    }

    private void updateDbConnectionTime() {
        if (this.dbConnection != null) {
            this.dbConnection.setLastUpdatedTime(new Date().getTime());
        }
    }
}
