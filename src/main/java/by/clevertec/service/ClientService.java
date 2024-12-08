package by.clevertec.service;

import by.clevertec.dto.ClientDto;
import by.clevertec.entity.Car;
import by.clevertec.entity.Client;
import by.clevertec.mapper.ClientMapper;
import by.clevertec.repository.CarRepository;
import by.clevertec.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final CarRepository carRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper, CarRepository carRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.carRepository = carRepository;
    }

    public ClientDto saveClient(ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        Client savedClient = clientRepository.save(client);
        return clientMapper.clientToClientDto(savedClient);
    }

    @Transactional
    public Optional<ClientDto> getClientDtoById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(clientMapper::clientToClientDto);
    }
    @Transactional
    public Optional<Client> getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client;
    }

    @Transactional
    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(clientMapper::clientToClientDto).toList();
    }

    @Transactional
    public void assignCarToClient(Long carId, Long clientId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("Автомобиль не найден"));
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Клиент не найден"));

        if (car.getShowroom() == null) {
            throw new IllegalStateException("Этот автомобиль уже был куплен");
        }
        client.getCars().add(car);
        car.setShowroom(null);
        carRepository.save(car);
        clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
