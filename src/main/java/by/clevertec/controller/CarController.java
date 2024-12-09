package by.clevertec.controller;

import by.clevertec.API.ApiResponse;
import by.clevertec.dto.CarDto;
import by.clevertec.entity.Car;
import by.clevertec.entity.CarShowroom;
import by.clevertec.mapper.CarMapper;
import by.clevertec.service.CarService;
import by.clevertec.service.CarShowroomService;
import by.clevertec.service.CategoryService;
import by.clevertec.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;
    private final CarShowroomService showroomService;
    private final CarMapper carMapper;
    private final CategoryService categoryService;
    private final ClientService clientService;

    @Autowired
    public CarController(CarService carService, CarShowroomService showroomService, CarMapper carMapper, CategoryService categoryService, ClientService clientService) {
        this.carService = carService;
        this.showroomService = showroomService;
        this.carMapper = carMapper;
        this.categoryService = categoryService;
        this.clientService = clientService;
    }

    @PostMapping()
    public ApiResponse<CarDto> addCar(@Validated @RequestBody CarDto carDto) {
        Car car = carMapper.carDtoToCar(carDto, categoryService);
        carService.addCar(car);
        return ApiResponse.<CarDto>builder()
                .data(carMapper.carToCarDTO(car))
                .status(true)
                .message("Автомобиль добавлен успешно")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CarDto> updateCar(@PathVariable Long id, @Validated @RequestBody CarDto carDTO) {
        Car car = carMapper.carDtoToCar(carDTO, categoryService);
        car.setId(id);
        carService.addCar(car);
        return ApiResponse.<CarDto>builder()
                .data(carMapper.carToCarDTO(car))
                .status(true)
                .message("Автомобиль обновлен успешно")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ApiResponse.<Void>builder()
                .status(true)
                .message("Автомобиль удален успешно")
                .build();
    }

    @PostMapping("/{carId}/showroom/{showroomId}")
    public ApiResponse<Void> assignCarToShowroom(@PathVariable Long carId, @PathVariable Long showroomId) {
        Car car = carService.getCarById(carId);
        CarShowroom showroom = showroomService.getCarShowroomById(showroomId).get();
        carService.assignCarToShowroom(car, showroom);
        return ApiResponse.<Void>builder()
                .status(true)
                .message("Автомобиль привязан к салону")
                .build();
    }

    @PostMapping("/{carId}/client/{clientId}")
    public ApiResponse<Void> assignCarToClient(@PathVariable Long carId, @PathVariable Long clientId) {
        clientService.assignCarToClient(carId, clientId);
        return ApiResponse.<Void>builder()
                .status(true)
                .message("Клиент купил автомобиль успешно")
                .build();
    }

    @GetMapping
    public ApiResponse<List<CarDto>> getCars(@RequestParam(required = false) String brand,
                                             @RequestParam(required = false) Integer year,
                                             @RequestParam(required = false) String category,
                                             @RequestParam(required = false) Double minPrice,
                                             @RequestParam(required = false) Double maxPrice) {
        List<CarDto> cars = carService.findCarsByFilters(brand, year, category, minPrice, maxPrice);
        return ApiResponse.<List<CarDto>>builder()
                .data(cars)
                .status(true)
                .message("Автомобили получены успешно")
                .build();
    }

    @GetMapping("/sorted")
    public ApiResponse<List<CarDto>> getCarsSorted(@RequestParam(required = false) String order) {
        List<CarDto> cars = carService.findAllSortedByPrice(order);
        return ApiResponse.<List<CarDto>>builder()
                .data(cars)
                .status(true)
                .message("Автомобили получены успешно")
                .build();
    }
}
