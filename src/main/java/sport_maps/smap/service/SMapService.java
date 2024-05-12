package sport_maps.smap.service;

import sport_maps.smap.dto.SMapDto;

public interface SMapService {
    void createMainMap(SMapDto dto);
    void updateMainMap(SMapDto dto);
}
