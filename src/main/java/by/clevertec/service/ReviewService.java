package by.clevertec.service;

import by.clevertec.dto.ReviewDto;
import by.clevertec.entity.Car;
import by.clevertec.entity.Client;
import by.clevertec.entity.Review;
import by.clevertec.mapper.ReviewMapper;
import by.clevertec.repository.CarRepository;
import by.clevertec.repository.ClientRepository;
import by.clevertec.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         CarRepository carRepository,
                         ClientRepository clientRepository,
                         ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    public ReviewDto addReview(ReviewDto reviewDto) {
        Review review = reviewMapper.reviewDtoToReview(reviewDto);

        Car car = carRepository.findById(reviewDto.getCarId())
                .orElseThrow(() -> new IllegalArgumentException("Автомобиль не найден"));
        Client client = clientRepository.findById(reviewDto.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Клиент не найден"));

        review.setCar(car);
        review.setClient(client);

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.reviewToReviewDto(savedReview);
    }

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::reviewToReviewDto)
                .toList();
    }

    public ReviewDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        return reviewMapper.reviewToReviewDto(review);
    }

    @Transactional
    public ReviewDto updateReview(Long id, ReviewDto reviewDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        review.setText(reviewDto.getText());
        review.setRating(reviewDto.getRating());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.reviewToReviewDto(updatedReview);
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<ReviewDto> searchReviews(String keyword) {
        return reviewMapper.reviewToReviewDto(reviewRepository.searchReviews(keyword));
    }
}
