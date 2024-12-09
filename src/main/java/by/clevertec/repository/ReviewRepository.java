package by.clevertec.repository;

import by.clevertec.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE LOWER(r.text) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Review> searchReviews(@Param("keyword") String keyword);

}
