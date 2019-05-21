package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.areas.conn.constants.DbConnectionConstants;
import com.cyecize.multidb.areas.conn.models.UserDbConnection;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class DbConnectionStorageServiceImpl implements DbConnectionStorageService {

    private final List<UserDbConnection> availableConnections;

    public DbConnectionStorageServiceImpl() {
        this.availableConnections = new ArrayList<>();
    }

    @Override
    public void closeAllConnections() {
        this.availableConnections.forEach(UserDbConnection::closeConnections);
        this.availableConnections.clear();
    }

    @Override
    @Scheduled(cron = "*/10 * * * * *")
    public void filterOldConnections() {
        long minConnectionTime = new Date().getTime() - DbConnectionConstants.USER_CONNECTION_MAX_INACTIVE_TIME_MILLIS;

        this.availableConnections.removeIf(conn -> {
            if (conn.getLastUpdatedTime() < minConnectionTime) {
                conn.closeConnections();
                return true;
            }

            return false;
        });
    }

    @Override
    public void addConnection(UserDbConnection dbConnection) {
        this.availableConnections.add(dbConnection);
    }
}
