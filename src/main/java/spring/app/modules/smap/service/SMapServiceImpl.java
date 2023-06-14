package spring.app.modules.smap.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.modules.commons.exception.AlreadyExistException;
import spring.app.modules.commons.exception.AuthenticationException;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.commons.util.convert.SimpleEntityConverter;
import spring.app.modules.security.domain.User;
import spring.app.modules.security.service.AuthenticationService;
import spring.app.modules.smap.dao.SMapDao;
import spring.app.modules.smap.domain.SMap;
import spring.app.modules.smap.dto.SMapDto;

import java.util.List;

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
            throw new AlreadyExistException("Main map exists");
        }
        return 0;
    }

    @Override
    public int updateMainMap(SMapDto dto) {
        SMap map = SimpleEntityConverter.convert(dto, new SMap());
        SMap inDb = mapDao.findAll().stream().findAny().orElseThrow(() -> new NotFoundException("No main map found"));
        mapDao.save(updateContent(map, inDb));
        return 0;
    }

    private SMap updateContent(SMap from, SMap to) {
        to.setZoom(from.getZoom());
        to.setType(from.getType());
        return to;
    }
}
