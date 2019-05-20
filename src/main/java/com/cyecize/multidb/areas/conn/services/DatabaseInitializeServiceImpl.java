package com.cyecize.multidb.areas.conn.services;

import com.cyecize.multidb.areas.conn.contracts.DbInitable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseInitializeServiceImpl implements DatabaseInitializeService {

    private final List<DbInitable> dbInitableServices;

    @Autowired
    public DatabaseInitializeServiceImpl(List<DbInitable> dbInitablesServices) {
        this.dbInitableServices = dbInitablesServices;
    }

    @Override
    public void initializeDatabase() {
        for (DbInitable dbInitableService : this.dbInitableServices) {
            dbInitableService.initDbValues();
        }
    }
}
