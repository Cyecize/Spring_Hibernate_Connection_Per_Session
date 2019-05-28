package com.cyecize.multidb.areas.demo.services;

import com.cyecize.multidb.areas.demo.entities.Car;
import com.cyecize.multidb.areas.demo.entities.Order;
import com.cyecize.multidb.areas.demo.entities.User;

import java.util.List;

public interface OrderService {

    void removeOrder(Order order);

    Order placeOrder(Car car, User customer);

    Order findOneById(Long id);

    List<Order> findByUser(User user);

    List<Order> findByCar(Car car);

    List<Order> findAll();
}
