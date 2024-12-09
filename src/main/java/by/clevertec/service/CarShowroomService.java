package by.clevertec.service;

import by.clevertec.entity.CarShowroom;
import by.clevertec.repository.CarShowroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarShowroomService {

    private final CarShowroomRepository carShowroomRepository;

    @Autowired
    public CarShowroomService(CarShowroomRepository carShowroomRepository) {
        this.carShowroomRepository = carShowroomRepository;
    }

    public CarShowroom saveCarShowroom(CarShowroom carShowroom) {
        return carShowroomRepository.save(carShowroom);
    }

    public List<CarShowroom> getAllCarShowrooms() {
        return carShowroomRepository.findAll();
    }

    public Optional<CarShowroom> getCarShowroomById(Long id) {
        return carShowroomRepository.findById(id);
    }

    public void deleteCarShowroom(Long id) {
        carShowroomRepository.deleteById(id);
    }
}

