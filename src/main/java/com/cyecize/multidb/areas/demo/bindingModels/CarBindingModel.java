package com.cyecize.multidb.areas.demo.bindingModels;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CarBindingModel {

    @NotNull
    @NotEmpty
    private String make;

    @NotNull
    @NotEmpty
    private String model;

    @NotNull
    private Long travelledDistance;

    @NotNull
    private Double price;

    public CarBindingModel() {

    }

    public String getMake() {
        return this.make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getTravelledDistance() {
        return this.travelledDistance;
    }

    public void setTravelledDistance(Long travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
