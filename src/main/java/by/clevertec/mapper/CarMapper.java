package by.clevertec.mapper;

import by.clevertec.dto.CarDto;
import by.clevertec.entity.Car;
import by.clevertec.entity.Category;
import by.clevertec.service.CategoryService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(source = "category", target = "category", qualifiedByName = "mapCategoryToEntity")
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "showroom", ignore = true)
    Car carDtoToCar(CarDto carDto, @Context CategoryService categoryService);

    @Mapping(source = "category", target = "category", qualifiedByName = "mapCategoryToString")
    @Mapping(target = "showroomName", ignore = true)
    CarDto carToCarDTO(Car car);

    @Mapping(source = "category", target = "category", qualifiedByName = "mapCategoryToEntity")
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "showroom", ignore = true)
    List<Car> carDtoToCar(List<CarDto> carDto, @Context CategoryService categoryService);

    @Mapping(source = "category", target = "category", qualifiedByName = "mapCategoryToString")
    @Mapping(target = "showroomName", ignore = true)
    List<CarDto> carToCarDTO(List<Car> car);

    // Преобразование имени категории в объект Category
    @Named("mapCategoryToEntity")
    default Category mapCategoryToEntity(String categoryName, @Context CategoryService categoryService) {
        return categoryService.getCategoryByName(categoryName);
    }

    @Named("mapCategoryToString")
    default String mapCategoryToString(Category category) {
        return category != null ? category.getName() : null;
    }
}