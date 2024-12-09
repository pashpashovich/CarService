package by.clevertec.mapper;

import by.clevertec.dto.ReviewDto;
import by.clevertec.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "car.id", target = "carId")
    ReviewDto reviewToReviewDto(Review review);

    @Mapping(source = "clientId", target = "client.id")
    @Mapping(source = "carId", target = "car.id")
    Review reviewDtoToReview(ReviewDto reviewDto);

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "car.id", target = "carId")
    List<ReviewDto> reviewToReviewDto(List<Review> review);
}
