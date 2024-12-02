package by.clevertec.service;

import by.clevertec.dao.CarDAO;
import by.clevertec.dao.ClientDAO;
import by.clevertec.entity.Car;
import by.clevertec.entity.Client;
import by.clevertec.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClientService {

    private final ClientDAO clientDAO;
    private final CarDAO carDAO;

    public ClientService(ClientDAO clientDAO, CarDAO carDAO) {
        this.clientDAO = clientDAO;
        this.carDAO = carDAO;
    }

    public void buyCar(Long clientId, Long carId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Client client = session.get(Client.class, clientId);
            Car car = session.get(Car.class, carId);
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

