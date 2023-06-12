package spring.app.modules.smap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.app.modules.smap.domain.SMarker;

import java.util.List;
import java.util.Optional;

@Repository
public interface SMarkerDao extends JpaRepository<SMarker, Long> {
    Optional<SMarker> findByLngAndLat(float lng, float lat);
    List<SMarker> findAllByTextLocation(String textLocation);
}
