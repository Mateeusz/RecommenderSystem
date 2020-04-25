package pl.devgroup.restapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.devgroup.restapi.model.Rating;
import pl.devgroup.restapi.model.Track;
import pl.devgroup.restapi.model.TrackDetails;
import pl.devgroup.restapi.repository.RatingRepository;
import pl.devgroup.restapi.repository.TracksRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class TrackService {

    private TracksRepository tracksRepository;
    private RatingRepository ratingRepository;
    protected ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_PATH = "src/main/resources/trackDetails/";

    @Autowired
    public TrackService(TracksRepository tracksRepository, RatingRepository ratingRepository) {
        this.tracksRepository = tracksRepository;
        this.ratingRepository = ratingRepository;
    }

    public List<Track> getTracks() {
        return tracksRepository.findAll();
    }

    public TrackDetails getTrack(String trackId, String userEmail) throws IOException {

        Track track = tracksRepository.findByTrackId(trackId);
        TrackDetails trackDetails = mapper.readValue(new File(RESOURCE_PATH + track.getTrackId() + ".json"), TrackDetails.class);

        Rating rating = ratingRepository.getRating(trackId, userEmail);
        if(rating != null) {
            trackDetails.setPoints(rating.getPoints());
        }

        return trackDetails;
    }

    public void saveRating(TrackDetails trackDetails, String userEmail) {
        String trackId = trackDetails.getTrackId();
        if(ratingRepository.getRating(trackId, userEmail)==null) {
            ratingRepository.insertRating(trackDetails.getPoints(), trackId, userEmail);
        } else {
            ratingRepository.updateRating(trackDetails.getPoints(), trackId, userEmail);
        }
    }
}
