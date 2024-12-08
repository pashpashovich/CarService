package by.clevertec.controller;

import by.clevertec.API.ApiResponse;
import by.clevertec.entity.CarShowroom;
import by.clevertec.service.CarShowroomService;
import by.clevertec.dto.CarShowroomDto;
import by.clevertec.mapper.CarShowroomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/car-showrooms")
public class CarShowroomController {

    private final CarShowroomService carShowroomService;
    private final CarShowroomMapper carShowroomMapper;

    @Autowired
    public CarShowroomController(CarShowroomService carShowroomService, CarShowroomMapper carShowroomMapper) {
        this.carShowroomService = carShowroomService;
        this.carShowroomMapper = carShowroomMapper;
    }

    @GetMapping
    public ApiResponse<List<CarShowroomDto>> getAllCarShowrooms() {
        List<CarShowroom> carShowrooms = carShowroomService.getAllCarShowrooms();
        return ApiResponse.<List<CarShowroomDto>>builder()
                .data(carShowroomMapper.carShowroomsToCarShowroomDTOs(carShowrooms))
                .status(true)
                .message("Все салоны получены")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CarShowroomDto> getCarShowroomById(@PathVariable Long id) {
        Optional<CarShowroom> carShowroom = carShowroomService.getCarShowroomById(id);
        if (carShowroom.isPresent()) {
            return ApiResponse.<CarShowroomDto>builder()
                    .data(carShowroomMapper.carShowroomToCarShowroomDTO(carShowroom.get()))
                    .status(true)
                    .message("Салон получен")
                    .build();
        } else {
            return ApiResponse.<CarShowroomDto>builder()
                    .status(false)
                    .message("Салон не найден")
                    .build();
        }
    }

    @PostMapping
    public ApiResponse<CarShowroomDto> addOrUpdateCarShowroom(@RequestBody CarShowroomDto carShowroomDto) {
        CarShowroom carShowroom = carShowroomMapper.carShowroomDTOToCarShowroom(carShowroomDto);
        CarShowroom savedCarShowroom = carShowroomService.saveCarShowroom(carShowroom);
        return ApiResponse.<CarShowroomDto>builder()
                .data(carShowroomMapper.carShowroomToCarShowroomDTO(savedCarShowroom))
                .status(true)
                .message("Car showroom saved successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCarShowroom(@PathVariable Long id) {
        carShowroomService.deleteCarShowroom(id);
    }
}

