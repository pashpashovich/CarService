package by.clevertec.service;

import by.clevertec.entity.Car;
import by.clevertec.entity.Client;
import by.clevertec.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientService {
    public static void buyCar(Client client, Car car) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            if (client != null && car != null) {
                client.getCars().add(car);
                car.setShowroom(null);
                session.merge(client);
                session.merge(car);
            } else {
                throw new IllegalArgumentException("Клиент или автомобиль не найден.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

}

