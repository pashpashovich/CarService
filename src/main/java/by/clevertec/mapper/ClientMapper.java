package by.clevertec.mapper;

import by.clevertec.dto.ClientDto;
import by.clevertec.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mappings({
            @Mapping(source = "client.name", target = "name"),
            @Mapping(source = "client.contacts", target = "contacts"),
    })
    ClientDto clientToClientDto(Client client);

    @Mappings({
            @Mapping(source = "clientDto.name", target = "name"),
            @Mapping(source = "clientDto.contacts", target = "contacts"),
            @Mapping(target = "registrationDate", expression = "java(java.time.LocalDate.now())")
    })
    Client clientDtoToClient(ClientDto clientDto);
}
