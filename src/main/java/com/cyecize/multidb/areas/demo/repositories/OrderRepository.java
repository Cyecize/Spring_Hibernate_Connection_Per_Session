package com.cyecize.multidb.areas.demo.repositories;

import com.cyecize.multidb.areas.conn.services.SessionDbService;
import com.cyecize.multidb.areas.demo.entities.Car;
import com.cyecize.multidb.areas.demo.entities.Order;
import com.cyecize.multidb.areas.demo.entities.User;
import com.cyecize.multidb.repository.BaseRepository;

import javax.persistence.criteria.Join;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderRepository extends BaseRepository<Order, Long> {

    private static final String CUSTOMER_FIELD_NAME = "customer";

    public OrderRepository(SessionDbService sessionDbService) {
        super(sessionDbService);
    }

    public List<Order> findByUser(User user) {
        return super.queryBuilderList(((query, orderRoot) -> {
            Join<Order, User> join = orderRoot.join(CUSTOMER_FIELD_NAME);

            query.where(super.criteriaBuilder.equal(join, user));
        }));
    }

    public List<Order> findByCar(Car car) {
        return super.queryBuilderList(((query, orderRoot) -> {
            Join<Order, Car> join = orderRoot.join(CUSTOMER_FIELD_NAME);

            query.where(super.criteriaBuilder.equal(join, car));
        }));
    }
}
