package by.clevertec.service;

import by.clevertec.dao.CarDAO;
import by.clevertec.dao.ClientDAO;
import by.clevertec.dao.ReviewDAO;
import by.clevertec.entity.Car;
import by.clevertec.entity.Client;
import by.clevertec.entity.Review;

public class ReviewService {

    private final ReviewDAO reviewDAO;
    private final CarDAO carDAO;
    private final ClientDAO clientDAO;

    public ReviewService(ReviewDAO reviewDAO, CarDAO carDAO, ClientDAO clientDAO) {
        this.reviewDAO = reviewDAO;
        this.carDAO = carDAO;
        this.clientDAO = clientDAO;
    }

    public void addReview(Long clientId, Long carId, String text, int rating) {
        Client client = clientDAO.findById(clientId);
        Car car = carDAO.findById(carId);

        if (client != null && car != null) {
            Review review = Review.builder()
                    .client(client)
                    .car(car)
                    .text(text)
                    .rating(rating)
                    .build();

            reviewDAO.save(review);
        } else {
            throw new IllegalArgumentException("Клиент или автомобиль не найден.");
        }
    }
}

