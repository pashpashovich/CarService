package by.clevertec.controller;

import by.clevertec.API.ApiResponse;
import by.clevertec.dto.ReviewDto;
import by.clevertec.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ApiResponse<ReviewDto> addReview(@RequestBody ReviewDto reviewDto) {
        ReviewDto addedReview = reviewService.addReview(reviewDto);
        return ApiResponse.<ReviewDto>builder()
                .status(true)
                .message("Отзыв добавлен успешно")
                .data(addedReview)
                .build();
    }

    @GetMapping
    public ApiResponse<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        return ApiResponse.<List<ReviewDto>>builder()
                .status(true)
                .message("Все отзывы получены успешно")
                .data(reviews)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ReviewDto> getReviewById(@PathVariable Long id) {
        ReviewDto review = reviewService.getReviewById(id);
        return ApiResponse.<ReviewDto>builder()
                .status(true)
                .message("Отзыв получен успешно")
                .data(review)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ReviewDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        ReviewDto updatedReview = reviewService.updateReview(id, reviewDto);
        return ApiResponse.<ReviewDto>builder()
                .status(true)
                .message("Отзыв обновлен успешно")
                .data(updatedReview)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ApiResponse.<Void>builder()
                .status(true)
                .message("Отзыв удален успешно")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ReviewDto>> searchReviews(@RequestParam(required = false) String keyword) {
        List<ReviewDto> reviews = reviewService.searchReviews(keyword);
        return ApiResponse.<List<ReviewDto>>builder()
                .status(true)
                .data(reviews)
                .message("Отзывы успешно найдены")
                .build();
    }
}
