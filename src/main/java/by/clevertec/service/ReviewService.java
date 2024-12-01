package by.clevertec.service;

import by.clevertec.dao.CarDAO;
import by.clevertec.dao.ClientDAO;
import by.clevertec.dao.ReviewDAO;
import by.clevertec.entity.Car;
import by.clevertec.entity.Client;
import by.clevertec.entity.Review;
import by.clevertec.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

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

    public List<Review> searchReviews(String keyword) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Review> query = cb.createQuery(Review.class);
            Root<Review> root = query.from(Review.class);

            if (keyword != null && !keyword.isEmpty()) {
                query.select(root)
                        .where(cb.like(cb.lower(root.get("text")), "%" + keyword.toLowerCase() + "%"));
            } else {
                query.select(root);
            }

            List<Review> reviews = session.createQuery(query).getResultList();
            transaction.commit();
            return reviews;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

}

