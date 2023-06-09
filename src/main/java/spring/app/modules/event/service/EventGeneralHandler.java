package spring.app.modules.event.service;

import spring.app.modules.event.domain.Event;
import spring.app.modules.event.dto.EventAllInfoDto;

import java.util.List;
import java.util.stream.Collectors;

public interface EventGeneralHandler {
    default EventAllInfoDto allInfoDto(Event event) {
        return EventAllInfoDto.builder()
                .id(event.getIdEvent())
                .name(event.getName())
                .eventDate(String.valueOf(event.getEventDate()))
                .desc(event.getDescription())
                .sportType(String.valueOf(event.getSportType()))
                .build();
    }

    default List<EventAllInfoDto> listToDto(List<Event> events) {
        return events.stream().map(this::allInfoDto).collect(Collectors.toList());
    }
}
