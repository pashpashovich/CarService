package by.clevertec.service;

import by.clevertec.dao.CarDAO;
import by.clevertec.entity.Car;
import by.clevertec.entity.CarShowroom;
import by.clevertec.util.HibernateUtil;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarService {

    private final CarDAO carDAO;
    private static final String PRICE = "price";

    public CarService(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    public void assignCarToShowroom(Car car, CarShowroom carShowroom) {
        if (car.getShowroom() != null && car.getShowroom().equals(carShowroom)) {
            throw new IllegalStateException("Car is already assigned to this showroom.");
        }
        car.setShowroom(carShowroom);
        carDAO.update(car);
    }


    public void addCar(Car car) {
        carDAO.save(car);
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
                predicates.add(cb.greaterThanOrEqualTo(root.get(PRICE), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(PRICE), maxPrice));
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
                query.orderBy(cb.desc(root.get(PRICE)));
            } else {
                query.orderBy(cb.asc(root.get(PRICE)));
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

    public List<Car> findCarsByShowroomWithEntityGraph(Long showroomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CarShowroom showroom;
            try (EntityManager entityManager = session.getEntityManagerFactory().createEntityManager()) {
                EntityGraph<?> graph = entityManager.getEntityGraph("CarShowroom.cars");
                showroom = entityManager.find(
                        CarShowroom.class,
                        showroomId,
                        Map.of("jakarta.persistence.fetchgraph", graph)
                );
            }
            return showroom.getCars();
        }
    }

    public List<Car> findCarsByShowroomWithJoinFetch(Long showroomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c FROM Car c " +
                                    "JOIN FETCH c.showroom s " +
                                    "WHERE s.id = :showroomId", Car.class)
                    .setParameter("showroomId", showroomId)
                    .list();
        }
    }

}

