package by.clevertec.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private Long id;
    @NotBlank(message = "Бренд автомобиля не может быть пустой")
    private String brand;
    @NotBlank(message = "Модель автомобиля не может быть пустой")
    private String model;
    @Min(value = 1886, message = "Год выпуска должен быть не ранее 1886")
    @Max(value = 2025, message = "Год выпуска должен быть не позднее 2025")
    private int year;
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше 0")
    private BigDecimal price;
    @NotBlank(message = "Категория не может быть пустой")
    private String category;
    private String showroomName;
}
