package com.cyecize.multidb.areas.demo.services;

import com.cyecize.multidb.areas.demo.bindingModels.CarBindingModel;
import com.cyecize.multidb.areas.demo.entities.Car;

import java.util.List;

public interface CarService {

    Car createCar(CarBindingModel bindingModel);

    Car editCar(Car car, CarBindingModel bindingModel);

    Car findById(Long id);

    List<Car> findByMake(String make);

    List<Car> findAll();
}
