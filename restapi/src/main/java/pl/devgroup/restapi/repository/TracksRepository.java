package pl.devgroup.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.devgroup.restapi.model.Track;

@Repository
public interface TracksRepository extends JpaRepository<Track, String> {

    Track findByTrackId(String trackId);
}
