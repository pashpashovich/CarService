package by.clevertec.repository;

import by.clevertec.entity.Car;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepositoryCustom {
    List<Car> findCarsByFilters(String brand, Integer year, String category, Double minPrice, Double maxPrice);
}
