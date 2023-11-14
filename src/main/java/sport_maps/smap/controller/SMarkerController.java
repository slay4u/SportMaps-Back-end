package sport_maps.smap.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import sport_maps.smap.dto.SMarkerDto;
import sport_maps.smap.service.SMarkerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static sport_maps.commons.BaseController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/markers")
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

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public int deleteMarker(@Valid @RequestBody SMarkerDto sMarkerDto) {
        return markerService.deleteMarker(sMarkerDto);
    }
}
