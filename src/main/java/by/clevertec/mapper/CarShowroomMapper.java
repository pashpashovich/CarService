package by.clevertec.mapper;

import by.clevertec.dto.CarShowroomDto;
import by.clevertec.entity.CarShowroom;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarShowroomMapper {

    CarShowroom carShowroomDTOToCarShowroom(CarShowroomDto carShowroomDto);

    CarShowroomDto carShowroomToCarShowroomDTO(CarShowroom carShowroom);

    List<CarShowroomDto> carShowroomsToCarShowroomDTOs(List<CarShowroom> carShowrooms);
}

