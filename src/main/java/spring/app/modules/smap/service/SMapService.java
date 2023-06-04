package spring.app.modules.smap.service;

import spring.app.modules.smap.dto.SMapDto;

import java.util.List;

public interface SMapService {
    int createMap(SMapDto dto);
    int updateMap(SMapDto dto);
    List<SMapDto> getAllMaps(int page);
    SMapDto getById(int id);
    void deleteById(int id);
}
