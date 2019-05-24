package com.cyecize.multidb.areas.demo.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @ManyToOne(targetEntity = Car.class)
    @JoinColumn(name = "ordered_car_id", nullable = false, referencedColumnName = "id")
    private Car orderedCar;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "id")
    private User customer;

    public Order() {

    }

    @PrePersist
    public void prePersist() {
        this.purchaseDate = LocalDateTime.now();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPurchaseDate() {
        return this.purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Car getOrderedCar() {
        return this.orderedCar;
    }

    public void setOrderedCar(Car orderedCar) {
        this.orderedCar = orderedCar;
    }

    public User getCustomer() {
        return this.customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }
}
