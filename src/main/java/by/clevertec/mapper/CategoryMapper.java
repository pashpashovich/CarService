package by.clevertec.mapper;

import by.clevertec.dto.CategoryDto;
import by.clevertec.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id", source = "category.id")
    @Mapping(target = "name", source = "category.name")
    CategoryDto categoryToCategoryDto(Category category);

    @Mapping(target = "id", source = "categoryDto.id")
    @Mapping(target = "name", source = "categoryDto.name")
    @Mapping(target = "cars",ignore = true)
    Category categoryDtoToCategory(CategoryDto categoryDto);

    List<CategoryDto> categoriesToCategoryDtos(List<Category> categories);
}
