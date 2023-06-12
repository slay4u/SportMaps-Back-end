package spring.app.modules.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.app.modules.security.domain.User;
import spring.app.modules.security.domain.UserData;

import java.util.Optional;

@Repository
public interface UserDataDao extends JpaRepository<UserData, Long> {
    Optional<UserData> getUserDataByUser(User user);
}
