package spring.app.modules.smap.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.modules.commons.exception.AuthenticationException;
import spring.app.modules.security.domain.User;
import spring.app.modules.security.service.AuthenticationService;
import spring.app.modules.smap.dto.SMapDto;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SMapServiceImpl implements SMapService{

    private final AuthenticationService authenticationService;

    @Override
    public int createMap(SMapDto dto) {
        User user = authenticationService.getDomainUser();
        if (user.getRole().isUser()) {
            throw new AuthenticationException("Access denied for user: " + user);
        }
        return 0;
    }

    @Override
    public int updateMap(SMapDto dto) {
        return 0;
    }

    @Override
    public List<SMapDto> getAllMaps(int page) {
        return null;
    }

    @Override
    public SMapDto getById(int id) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }
}
