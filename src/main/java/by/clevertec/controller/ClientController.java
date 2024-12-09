package by.clevertec.controller;

import by.clevertec.API.ApiResponse;
import by.clevertec.dto.ClientDto;
import by.clevertec.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ApiResponse<ClientDto> addClient(@RequestBody ClientDto clientDto) {
        ClientDto savedClient = clientService.saveClient(clientDto);
        return ApiResponse.<ClientDto>builder()
                .data(savedClient)
                .status(true)
                .message("Клиент добавлен успешно")
                .build();
    }

    @GetMapping
    public ApiResponse<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = clientService.getAllClients();
        return ApiResponse.<List<ClientDto>>builder()
                .data(clients)
                .status(true)
                .message("Клиенты получены успешно")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ClientDto> getClientById(@PathVariable Long id) {
        return clientService.getClientDtoById(id)
                .map(clientDto -> ApiResponse.<ClientDto>builder()
                        .data(clientDto)
                        .status(true)
                        .message("Клиент получен успешно")
                        .build())
                .orElseGet(() -> ApiResponse.<ClientDto>builder()
                        .data(null)
                        .status(false)
                        .message("Клиент не найден")
                        .build());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ApiResponse.<String>builder()
                .data("Клиент удален")
                .status(true)
                .message("Клиент удален успешно")
                .build();
    }
}
