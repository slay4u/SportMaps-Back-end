package sport_maps.smap.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import sport_maps.commons.util.convert.SimpleEntityConverter;
import sport_maps.smap.dao.SMapDao;
import sport_maps.smap.dto.SMapDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.smap.domain.SMap;

@Service
@Transactional
@AllArgsConstructor
public class SMapServiceImpl implements SMapService{

    private final SMapDao mapDao;

    @Override
    public int createMainMap(SMapDto dto) {
        if (mapDao.findAll().isEmpty()) {
            SMap map = SimpleEntityConverter.convert(dto, new SMap());
            mapDao.save(map);
        } else {
            throw new EntityExistsException("Main map exists");
        }
        return 0;
    }

    @Override
    public int updateMainMap(SMapDto dto) {
        SMap map = SimpleEntityConverter.convert(dto, new SMap());
        SMap inDb = mapDao.findAll().stream().findAny().orElseThrow(() -> new EntityNotFoundException("No main map found"));
        mapDao.save(updateContent(map, inDb));
        return 0;
    }

    private SMap updateContent(SMap from, SMap to) {
        to.setZoom(from.getZoom());
        to.setType(from.getType());
        return to;
    }
}
