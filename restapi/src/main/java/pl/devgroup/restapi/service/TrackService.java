package pl.devgroup.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.devgroup.restapi.model.Track;
import pl.devgroup.restapi.repository.TracksRepository;

import java.util.List;

@Service
public class TrackService {

    private TracksRepository tracksRepository;

    @Autowired
    public TrackService(TracksRepository tracksRepository) {
        this.tracksRepository = tracksRepository;
    }

    public List<Track> getTracks() {
        return tracksRepository.findAll();
    }

}
