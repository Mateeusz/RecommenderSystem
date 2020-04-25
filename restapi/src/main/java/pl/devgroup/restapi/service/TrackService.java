package pl.devgroup.restapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.devgroup.restapi.model.Track;
import pl.devgroup.restapi.model.TrackDetails;
import pl.devgroup.restapi.repository.TracksRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class TrackService {

    private TracksRepository tracksRepository;
    protected ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_PATH = "src/main/resources/trackDetails/";

    @Autowired
    public TrackService(TracksRepository tracksRepository) {
        this.tracksRepository = tracksRepository;
    }

    public List<Track> getTracks() {
        return tracksRepository.findAll();
    }

    public TrackDetails getTrack(String trackId) throws IOException {

        Track track = tracksRepository.findByTrackId(trackId);
        TrackDetails trackDetails = mapper.readValue(new File(RESOURCE_PATH + track.getTrackId() + ".json"), TrackDetails.class);

        return trackDetails;
    }

}
