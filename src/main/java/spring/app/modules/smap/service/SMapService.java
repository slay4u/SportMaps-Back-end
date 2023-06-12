package spring.app.modules.smap.service;

import spring.app.modules.smap.dto.SMapDto;

import java.util.List;

public interface SMapService {
    int createMainMap(SMapDto dto);
    int updateMainMap(SMapDto dto);
}
