package spring.app.service.impl;

import spring.app.domain.Event;
import spring.app.dto.events.EventAllInfoDto;

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
