package by.clevertec.repository;

import by.clevertec.entity.Car;
import by.clevertec.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CarRepositoryCustomImpl implements CarRepositoryCustom {
    private static final String PRICE = "price";

    @Override
    public List<Car> findCarsByFilters(String brand, Integer year, String category, Double minPrice, Double maxPrice)
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Car> query = cb.createQuery(Car.class);
            Root<Car> root = query.from(Car.class);
            List<Predicate> predicates = new ArrayList<>();
            if (brand != null && !brand.isEmpty()) {
                predicates.add(cb.equal(root.get("brand"), brand));
            }
            if (year != null) {
                predicates.add(cb.equal(root.get("year"), year));
            }
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category").get("name"), category));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(PRICE), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(PRICE), maxPrice));
            }
            query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
            return session.createQuery(query).getResultList();
        }
    }

}
