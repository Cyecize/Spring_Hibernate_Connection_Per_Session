package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.areas.conn.models.UserDbConnection;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
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

    /**
     * Updated the Db Connection last accessed time so that it doesn't get closed due to inactivity.
     */
    @Override
    public void sendKeepAlive() {
        if (this.dbConnection != null) {
            this.dbConnection.setLastUpdatedTime(new Date().getTime());
        }
    }

    @Override
    public void setConnection(UserDbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public boolean hasOpenConnection() {
        try {
            return this.dbConnection != null &&
                    this.dbConnection.getOrmConnection() != null &&
                    this.dbConnection.getOrmConnection().isOpen() &&
                    ((SessionImpl) this.dbConnection.getEntityManager().unwrap(Session.class)).connection().isValid(2);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserDbConnection getConnection() {
        return this.dbConnection;
    }
}
