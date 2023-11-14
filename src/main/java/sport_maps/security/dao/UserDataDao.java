package sport_maps.security.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sport_maps.security.domain.User;
import sport_maps.security.domain.UserData;

import java.util.List;

@Repository
public interface UserDataDao extends JpaRepository<UserData, Long> {


    List<UserData> getUserDataByUser(User user, Pageable p);

/*    @Query(value = "select *\n" +
            "from (select last_seen,\n" +
            "             row_number() over (order by last_seen desc) as rn\n" +
            "      from user_data where user_id = ?2) t\n" +
            "where t.rn = ?1", nativeQuery = true)
    Map<Long, LocalDateTime> getLatestLastSeen(long shift, User user);*/
}
