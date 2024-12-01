package by.clevertec.service;

import by.clevertec.dao.CarDAO;
import by.clevertec.dao.CarShowroomDAO;
import by.clevertec.entity.Car;
import by.clevertec.entity.CarShowroom;
import by.clevertec.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class CarService {

    private final CarDAO carDAO;
    private final CarShowroomDAO showroomDAO;

    public CarService(CarDAO carDAO, CarShowroomDAO showroomDAO) {
        this.carDAO = carDAO;
        this.showroomDAO = showroomDAO;
    }

    public void addCarToShowroom(Car car, Long showroomId) {
        CarShowroom showroom = showroomDAO.findById(showroomId);
        if (showroom != null) {
            car.setShowroom(showroom);
            carDAO.save(car);
        } else {
            throw new IllegalArgumentException("Автосалон с ID " + showroomId + " не найден.");
        }
    }

    public List<Car> findCarsByFilters(String brand, Integer year, String category, Double minPrice, Double maxPrice) {
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
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
            return session.createQuery(query).getResultList();
        }
    }

    public List<Car> findAllSortedByPrice(String sortOrder) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Car> query = cb.createQuery(Car.class);
            Root<Car> root = query.from(Car.class);
            if ("DESC".equalsIgnoreCase(sortOrder)) {
                query.orderBy(cb.desc(root.get("price")));
            } else {
                query.orderBy(cb.asc(root.get("price")));
            }
            query.select(root);
            return session.createQuery(query).getResultList();
        }
    }

    public List<Car> findCarsWithPagination(int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Car> query = cb.createQuery(Car.class);
            Root<Car> root = query.from(Car.class);
            query.select(root);
            return session.createQuery(query)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        }
    }
}

