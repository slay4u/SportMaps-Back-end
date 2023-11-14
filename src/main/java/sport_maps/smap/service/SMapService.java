package sport_maps.smap.service;

import sport_maps.smap.dto.SMapDto;

public interface SMapService {
    int createMainMap(SMapDto dto);
    int updateMainMap(SMapDto dto);
}
