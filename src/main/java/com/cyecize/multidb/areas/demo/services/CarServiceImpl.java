package com.cyecize.multidb.areas.demo.services;

import com.cyecize.multidb.areas.demo.bindingModels.CreateCarBindingModel;
import com.cyecize.multidb.areas.demo.entities.Car;
import com.cyecize.multidb.areas.demo.repositories.CarRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository repository;

    private final ModelMapper modelMapper;

    @Autowired
    public CarServiceImpl(CarRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Car createCar(CreateCarBindingModel bindingModel) {
        Car car = this.modelMapper.map(bindingModel, Car.class);
        this.repository.persist(car);

        return car;
    }

    @Override
    public Car findById(Long id) {
        return this.repository.find(id);
    }

    @Override
    public List<Car> findByMake(String make) {
        return this.repository.findByMake(make);
    }

    @Override
    public List<Car> findAll() {
        return this.repository.findAll();
    }
}
