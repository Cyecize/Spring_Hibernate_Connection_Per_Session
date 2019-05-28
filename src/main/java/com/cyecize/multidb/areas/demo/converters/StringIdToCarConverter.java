package com.cyecize.multidb.areas.demo.converters;

import com.cyecize.multidb.areas.demo.entities.Car;
import com.cyecize.multidb.areas.demo.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringIdToCarConverter implements Converter<String, Car> {

    private final CarService carService;

    @Autowired
    public StringIdToCarConverter(CarService carService) {
        this.carService = carService;
    }

    @Override
    public Car convert(String s) {
        try {
            return this.carService.findById(Long.parseLong(s.trim()));
        } catch (Exception ignored) {
        }

        return null;
    }
}
