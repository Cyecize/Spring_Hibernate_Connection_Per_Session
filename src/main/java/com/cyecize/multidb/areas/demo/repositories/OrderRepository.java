package com.cyecize.multidb.areas.demo.repositories;

import com.cyecize.multidb.areas.conn.services.SessionDbService;
import com.cyecize.multidb.areas.demo.entities.Order;
import com.cyecize.multidb.repository.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderRepository extends BaseRepository<Order, Long> {
    public OrderRepository(SessionDbService sessionDbService) {
        super(sessionDbService);
    }
}
