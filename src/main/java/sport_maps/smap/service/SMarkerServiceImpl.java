package sport_maps.smap.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import sport_maps.smap.dao.SMarkerDao;
import sport_maps.smap.dto.SMarkerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sport_maps.smap.domain.SMarker;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SMarkerServiceImpl {
    private final SMarkerDao markerDao;
    private final RestTemplate googleApi;
    @Value("${google-api-key}")
    private String apiKey;

    public SMarkerServiceImpl(SMarkerDao markerDao, RestTemplate googleApi) {
        this.markerDao = markerDao;
        this.googleApi = googleApi;
    }

    public void addMarker(SMarkerDto markerDto) {
        if (!isValidGeo(markerDto.getPosition())) throw new IllegalArgumentException("Marker's position is not valid");
        if (markerDao.existsByLngAndLat(markerDto.getPosition().lng, markerDto.getPosition().lat))
            throw new EntityExistsException("Marker with that position already exists");
        markerDao.save(toEntity(markerDto));
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

    public void deleteMarker(SMarkerDto sMarkerDto) {
        markerDao.findByLngAndLat(sMarkerDto.getPosition().lng, sMarkerDto.getPosition().lat).ifPresent(markerDao::delete);
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
            throw new IllegalArgumentException("Unable to parse from JSON.");
        }
        return textLocation;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String>> fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, String>> map;
        try {
            map = mapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not read from json.");
        }
        return map;
    }

    private SMarker toEntity(SMarkerDto dto) {
        SMarker entity = new SMarker();
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
