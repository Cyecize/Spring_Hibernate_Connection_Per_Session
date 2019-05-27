package com.cyecize.multidb.areas.demo.repositories;

import com.cyecize.multidb.areas.conn.services.SessionDbService;
import com.cyecize.multidb.areas.demo.entities.Car;
import com.cyecize.multidb.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarRepository extends BaseRepository<Car, Long> {
    private static final String MAKE_FIELD_NAME = "make";

    public CarRepository(SessionDbService sessionDbService) {
        super(sessionDbService);
    }

    public List<Car> findByMake(String make) {
        return super.queryBuilderList(((carCriteriaQuery, carRoot) -> carCriteriaQuery.where(
                super.criteriaBuilder.equal(carRoot.get(MAKE_FIELD_NAME), make)
        )));
    }
}
