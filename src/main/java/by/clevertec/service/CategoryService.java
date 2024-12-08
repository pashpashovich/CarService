package by.clevertec.service;

import by.clevertec.dto.CategoryDto;
import by.clevertec.entity.Category;
import by.clevertec.mapper.CategoryMapper;
import by.clevertec.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.categoryDtoToCategory(categoryDto);
        category = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDto(category);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(categoryDto.getName());
            category = categoryRepository.save(category);
            return categoryMapper.categoryToCategoryDto(category);
        }
        throw new IllegalArgumentException("Категория не найдена с id " + id);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.categoriesToCategoryDtos(categories);
    }

    public CategoryDto getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            return categoryMapper.categoryToCategoryDto(optionalCategory.get());
        }
        throw new IllegalArgumentException("Категория не найдена с id " + id);
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }
}