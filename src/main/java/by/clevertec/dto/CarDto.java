package by.clevertec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    private String category;
    private String showroomName;
}
