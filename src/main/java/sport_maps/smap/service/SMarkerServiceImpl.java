package sport_maps.smap.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sport_maps.smap.dao.SMarkerDao;
import sport_maps.smap.dto.SMarkerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sport_maps.security.service.AuthenticationService;
import sport_maps.smap.domain.SMarker;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class SMarkerServiceImpl {

    private final AuthenticationService authenticationService;
    private final SMarkerDao markerDao;
    private final RestTemplate googleApi;
    @Value("${google-api-key}")
    private String apiKey;

    public int addMarker(SMarkerDto markerDto) {
        if (!isValidGeo(markerDto.getPosition())) {
            throw new IllegalArgumentException("Marker's position is not valid");
        }
        float lng = markerDto.getPosition().lng;
        float lat = markerDto.getPosition().lat;
        Optional<SMarker> byLngAndLat = markerDao.findByLngAndLat(lng, lat);
        if (byLngAndLat.isPresent()) {
            throw new IllegalArgumentException("Marker already exists with position:\n lat: " + lat + "\n lng: " + lng);
        }
        markerDao.save(toEntity(markerDto, new SMarker()));
        return 0;
    }

    public List<SMarkerDto> getAllMarkers(SMarkerDto.Position clientPosition) {
        String textLocation = getTextLocation(clientPosition);
        List<SMarker> allByTextLocation = markerDao.findAllByTextLocation(textLocation);

        return allByTextLocation.stream().map(e -> SMarkerDto.builder()
                .label(e.getLabel())
                .title(e.getTitle())
                .position(new SMarkerDto.Position(e.getLat(), e.getLng()))
                .description(e.getDescription())
                .build()).toList();
    }

    public int deleteMarker(SMarkerDto sMarkerDto) {
        markerDao.findByLngAndLat(sMarkerDto.getPosition().lng, sMarkerDto.getPosition().lat).ifPresent(markerDao::delete);
        return 1;
    }

    private String getTextLocation(SMarkerDto.Position position) {
        String result = googleApi.getForObject("/json?latlng=" + position.lat + "," + position.lng + "&key=" + apiKey, String.class);
        String compoundCode = fromJSON(result).get("plus_code").get("compound_code");
        String textLocation = null;
        if (compoundCode != null) {
            final Pattern p = Pattern.compile("^(\\S+)\\s(.*)");
            Matcher m = p.matcher(compoundCode);
            if (m.find()) {
                textLocation = m.group(2);
            }
        } else {
            throw new IllegalStateException("Unable to parse from JSON");
        }
        return textLocation;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String>> fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, String>> map = null;
        try {
            map = mapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return map;
    }

    private SMarker toEntity(SMarkerDto dto, SMarker entity) {
        entity.setTitle(dto.getTitle());
        entity.setLabel(dto.getLabel());
        entity.setLat(dto.getPosition().lat);
        entity.setLng(dto.getPosition().lng);
        entity.setTextLocation(getTextLocation(dto.getPosition()));
        entity.setDescription(dto.getDescription());
        return entity;
    }

    private boolean isValidGeo(SMarkerDto.Position position) {
        return position.lat >= -90 || position.lat <= 90 && position.lng >= -180 && position.lng <= 180;
    }
}
