package spring.app.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.domain.New;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewDao extends JpaRepository<New, Long> {
    @Query(value = "SELECT * FROM public.news", nativeQuery = true)
    List<New> getAllNews(Pageable pageable);

    @Query(value = "SELECT * FROM public.news" +
            " WHERE name=?1", nativeQuery = true)
    Optional<New> getNewByName(String name);

    @Query(value = "SELECT * FROM public.news" +
            " WHERE id_new = ?1", nativeQuery = true)
    Optional<New> getNewById(Long id);

    @Query(value = "SELECT COUNT(*) FROM public.news",
            nativeQuery = true)
    long getAllNewCount();
}
