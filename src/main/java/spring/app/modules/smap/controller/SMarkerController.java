package spring.app.modules.smap.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.app.modules.smap.dto.SMarkerDto;
import spring.app.modules.smap.service.SMarkerServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/sport-maps/v1/markers")
@AllArgsConstructor
public class SMarkerController {

    private final SMarkerServiceImpl markerService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SMarkerDto> getAllMarkers(@Valid @RequestBody SMarkerDto.Position position) {
        return markerService.getAllMarkers(position);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public int addMarker(@Valid @RequestBody SMarkerDto sMarkerDto) {
        return markerService.addMarker(sMarkerDto);
    }
}
