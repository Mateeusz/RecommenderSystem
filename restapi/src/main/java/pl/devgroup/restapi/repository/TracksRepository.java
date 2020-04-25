package pl.devgroup.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.devgroup.restapi.model.Track;

import java.util.List;

@Repository
public interface TracksRepository extends JpaRepository<Track, String> {

    Track findByTrackId(String trackId);
    List<Track> findAll();

}
