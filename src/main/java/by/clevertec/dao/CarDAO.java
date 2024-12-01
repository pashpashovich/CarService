package by.clevertec.dao;

import by.clevertec.entity.Car;
import by.clevertec.entity.Client;
import by.clevertec.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    public void save(Car car) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(car);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Car findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Car.class, id);
        }
    }

    public List<Car> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Car", Car.class).list();
        }
    }

    public void update(Car car) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(car);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void delete(Car car) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<Client> clients = session.createQuery(
                            "SELECT c FROM Client c JOIN c.cars car WHERE car.id = :carId", Client.class)
                    .setParameter("carId", car.getId())
                    .list();
            for (Client client : clients) {
                client.getCars().remove(car);
                session.merge(client);
            }

            session.createNativeQuery("DELETE FROM client_cars WHERE car_id = :carId")
                    .setParameter("carId", car.getId())
                    .executeUpdate();

            if (car.getCategory() != null) {
                car.getCategory().getCars().remove(car);
                session.merge(car.getCategory());
            }

            if (car.getShowroom() != null) {
                car.getShowroom().getCars().remove(car);
                session.merge(car.getShowroom());
            }

            car = session.contains(car) ? car : session.merge(car);
            session.remove(car);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }


}