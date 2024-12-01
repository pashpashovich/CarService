package by.clevertec.service;

import by.clevertec.dao.CarDAO;
import by.clevertec.dao.ClientDAO;
import by.clevertec.entity.Car;
import by.clevertec.entity.Client;

public class ClientService {

    private final ClientDAO clientDAO;
    private final CarDAO carDAO;

    public ClientService(ClientDAO clientDAO, CarDAO carDAO) {
        this.clientDAO = clientDAO;
        this.carDAO = carDAO;
    }

    public void buyCar(Long clientId, Long carId) {
        Client client = clientDAO.findById(clientId);
        Car car = carDAO.findById(carId);

        if (client != null && car != null) {
            client.getCars().add(car);
            car.setShowroom(null);
            clientDAO.update(client);
            carDAO.update(car);
        } else {
            throw new IllegalArgumentException("Клиент или автомобиль не найден.");
        }
    }
}

