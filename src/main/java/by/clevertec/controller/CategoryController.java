package by.clevertec.controller;

import by.clevertec.API.ApiResponse;
import by.clevertec.dto.CategoryDto;
import by.clevertec.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ApiResponse<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return ApiResponse.<CategoryDto>builder()
                .data(createdCategory)
                .status(true)
                .message("Категория создана успешно")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryDto> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(Long.valueOf(id), categoryDto);
        return ApiResponse.<CategoryDto>builder()
                .data(updatedCategory)
                .status(true)
                .message("Категория обновлена успешно")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder()
                .status(true)
                .message("Категория удалена успешно")
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ApiResponse.<List<CategoryDto>>builder()
                .data(categories)
                .status(true)
                .message("Категории получены")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ApiResponse.<CategoryDto>builder()
                .data(category)
                .status(true)
                .message("Категория получена")
                .build();
    }
}

