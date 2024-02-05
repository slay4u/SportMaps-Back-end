package sport_maps.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import sport_maps.security.domain.Token;

@NoRepositoryBean
public interface TokenDao<T extends Token> extends JpaRepository<T, Long> {

}
