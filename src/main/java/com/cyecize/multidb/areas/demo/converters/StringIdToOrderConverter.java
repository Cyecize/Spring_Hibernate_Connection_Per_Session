package com.cyecize.multidb.areas.demo.converters;

import com.cyecize.multidb.areas.demo.entities.Order;
import com.cyecize.multidb.areas.demo.services.OrderService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringIdToOrderConverter implements Converter<String, Order> {

    private final OrderService orderService;

    public StringIdToOrderConverter(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public Order convert(String s) {

        try {
            return this.orderService.findOneById(Long.parseLong(s.trim()));
        } catch (Exception ignored) {}

        return null;
    }
}
