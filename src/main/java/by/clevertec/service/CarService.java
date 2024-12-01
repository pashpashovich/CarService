package by.clevertec.service;

import by.clevertec.dao.CarDAO;
import by.clevertec.dao.CarShowroomDAO;
import by.clevertec.entity.Car;
import by.clevertec.entity.CarShowroom;

public class CarService {

    private final CarDAO carDAO;
    private final CarShowroomDAO showroomDAO;

    public CarService(CarDAO carDAO, CarShowroomDAO showroomDAO) {
        this.carDAO = carDAO;
        this.showroomDAO = showroomDAO;
    }

    public void addCarToShowroom(Car car, Long showroomId) {
        CarShowroom showroom = showroomDAO.findById(showroomId);
        if (showroom != null) {
            car.setShowroom(showroom);
            carDAO.save(car);
        } else {
            throw new IllegalArgumentException("Автосалон с ID " + showroomId + " не найден.");
        }
    }
}

