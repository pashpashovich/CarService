package by.clevertec.service;

import by.clevertec.dto.CarDto;
import by.clevertec.entity.Car;
import by.clevertec.entity.CarShowroom;
import by.clevertec.exception.NotFoundException;
import by.clevertec.mapper.CarMapper;
import by.clevertec.repository.CarRepository;
import by.clevertec.repository.CarRepositoryCustom;
import by.clevertec.repository.CarRepositoryCustomImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CarRepositoryCustom carRepositoryCustom;

    @Autowired
    public CarService(CarRepository carRepository, CarMapper carMapper, CarRepositoryCustomImpl carRepositoryCustom) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.carRepositoryCustom = carRepositoryCustom;
    }

    @Transactional
    public void assignCarToShowroom(Car car, CarShowroom carShowroom) {
        if (car.getShowroom() != null && car.getShowroom().equals(carShowroom)) throw new IllegalStateException("Машина уже привязана к этому салону");
        car.setShowroom(carShowroom);
        carRepository.save(car);
    }

    public Car getCarById(Long id) {
        if (carRepository.findById(id).isPresent())
        {
            return carRepository.findById(id).get();
        }
        else throw new NotFoundException("Автомобиль не найден");
    }

    public void addCar(@Valid Car car) {
        carRepository.save(car);
    }

    public List<CarDto> findCarsByFilters(String brand, Integer year, String category, Double minPrice, Double maxPrice) {
        return carMapper.carToCarDTO(carRepositoryCustom.findCarsByFilters(brand, year, category, minPrice, maxPrice));
    }

    public List<CarDto> findAllSortedByPrice(String sortOrder) {
        if ("desc".equals(sortOrder)) {
            return carMapper.carToCarDTO(carRepository.findAll(Sort.by(Sort.Order.desc("price"))));
        } else {
            return carMapper.carToCarDTO(carRepository.findAll(Sort.by(Sort.Order.asc("price"))));
        }
    }

    public void deleteCar(Long id) {
        Optional<Car> car = carRepository.findById(id);
        car.ifPresent(carRepository::delete);
    }
}

