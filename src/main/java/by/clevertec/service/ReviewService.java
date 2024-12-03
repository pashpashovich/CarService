package by.clevertec.service;

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
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.List;

public class ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewService(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public void addReview(Client client, Car car, String text, int rating) {
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

    public List<Review> searchReviewsByText(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            SearchSession searchSession = Search.session(session);
            return searchSession.search(Review.class)
                    .where(f -> f.match()
                            .fields("text")
                            .matching(keyword)
                    )
                    .fetchAllHits();
        }
    }

    public List<Review> searchReviewsByRating(int rating) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            SearchSession searchSession = Search.session(session);
            return searchSession.search(Review.class)
                    .where(f -> f.match()
                            .field("rating")
                            .matching(rating)
                    )
                    .fetchAllHits();
        }
    }

}

