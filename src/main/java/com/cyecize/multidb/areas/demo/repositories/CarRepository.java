package com.cyecize.multidb.areas.demo.repositories;

import com.cyecize.multidb.areas.conn.services.SessionDbService;
import com.cyecize.multidb.areas.demo.entities.Car;
import com.cyecize.multidb.repository.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class CarRepository  extends BaseRepository<Car, Long> {
    public CarRepository(SessionDbService sessionDbService) {
        super(sessionDbService);
    }
}
