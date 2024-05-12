package sport_maps.smap.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import sport_maps.smap.dto.SMarkerDto;
import sport_maps.smap.service.SMarkerServiceImpl;
import org.springframework.http.HttpStatus;

import java.util.List;

import static sport_maps.security.general.SecurityURLs.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/markers")
public class SMarkerController {
    private final SMarkerServiceImpl markerService;

    public SMarkerController(SMarkerServiceImpl markerService) {
        this.markerService = markerService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SMarkerDto> getAllMarkers(@Valid @RequestBody SMarkerDto.Position position) {
        return markerService.getAllMarkers(position);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addMarker(@Valid @RequestBody SMarkerDto sMarkerDto) {
        markerService.addMarker(sMarkerDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarker(@Valid @RequestBody SMarkerDto sMarkerDto) {
        markerService.deleteMarker(sMarkerDto);
    }
}
