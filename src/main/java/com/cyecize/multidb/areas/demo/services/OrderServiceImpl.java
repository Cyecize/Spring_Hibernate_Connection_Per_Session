package com.cyecize.multidb.areas.demo.services;

import com.cyecize.multidb.areas.demo.entities.Car;
import com.cyecize.multidb.areas.demo.entities.Order;
import com.cyecize.multidb.areas.demo.entities.User;
import com.cyecize.multidb.areas.demo.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Autowired
    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public void removeOrder(Order order) {
        this.repository.remove(order);
    }

    @Override
    public Order placeOrder(Car car, User customer) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderedCar(car);

        this.repository.persist(order);

        return order;
    }

    @Override
    public Order findOneById(Long id) {
        return this.repository.find(id);
    }

    @Override
    public List<Order> findByUser(User user) {
        return this.repository.findByUser(user);
    }

    @Override
    public List<Order> findByCar(Car car) {
        return this.repository.findByCar(car);
    }

    @Override
    public List<Order> findAll() {
        return this.repository.findAll();
    }
}
