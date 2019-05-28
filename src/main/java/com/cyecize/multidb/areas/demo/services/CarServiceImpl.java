package com.cyecize.multidb.areas.demo.services;

import com.cyecize.multidb.areas.demo.bindingModels.CarBindingModel;
import com.cyecize.multidb.areas.demo.entities.Car;
import com.cyecize.multidb.areas.demo.repositories.CarRepository;
import com.cyecize.multidb.utils.ModelMerger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository repository;

    private final ModelMapper modelMapper;

    private final ModelMerger modelMerger;

    @Autowired
    public CarServiceImpl(CarRepository repository, ModelMapper modelMapper, ModelMerger modelMerger) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.modelMerger = modelMerger;
    }

    @Override
    public Car createCar(CarBindingModel bindingModel) {
        Car car = this.modelMapper.map(bindingModel, Car.class);
        this.repository.persist(car);

        return car;
    }

    @Override
    public Car editCar(Car car, CarBindingModel bindingModel) {
        this.modelMerger.merge(bindingModel, car);

        this.repository.merge(car);

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
