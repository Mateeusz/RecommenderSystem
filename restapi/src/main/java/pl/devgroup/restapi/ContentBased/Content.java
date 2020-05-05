package pl.devgroup.restapi.ContentBased;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.devgroup.restapi.model.Rating;
import pl.devgroup.restapi.model.Track;
import pl.devgroup.restapi.model.TrackDetails;
import pl.devgroup.restapi.repository.RatingRepository;
import pl.devgroup.restapi.repository.UserRepository;
import pl.devgroup.restapi.service.TrackService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Content {

    private RatingRepository ratingRepository;
    private TrackService trackService;
    private UserRepository userRepository;

    private List<Track> recoomendedTracks = new ArrayList<>();
    private String[][] similars;

    public Content() {}

    @Autowired
    public Content(RatingRepository ratingRepository, TrackService trackService, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.trackService = trackService;
        this.userRepository = userRepository;
    }
    public List<Track> createRecommendedList(String email) throws IOException {
        Track t = findBestRatedTrack(email);
        recoomendedTracks.add(t);
        for (int i=0; i<2; i++){
            recoomendedTracks.add(getSimilarTrackNotInTheList(recoomendedTracks.get(i)));
        }
        return recoomendedTracks;
    }

    public Track getSimilarTrackNotInTheList(Track t) throws IOException {
        TrackDetails td = trackService.getTrackDetailByIdWithoutTrack(t.getTrackId());
        similars = td.getSimilars();
        int i = 0;
        boolean petla = true;

        while(petla) {
            if (recoomendedTracks.contains(similars[i][0])) {
                i++;
                if (i >= 1) petla = false;
            } else {

                TrackDetails next = trackService.getTrackDetailByIdWithoutTrack(similars[i][0]);
                petla = false;
            }
        }
        return trackService.getTrackById(similars[i][0]);
    }

    public Track findBestRatedTrack(String activeUserEmail){
        Track t = new Track();
//        List<Rating> ratings = ratingRepository.findAllByEmail(activeUserEmail);
        Track doWyszukania = new Track();
        doWyszukania.setTrackId("TRAAADZ128F9348C2E");
        Rating[] ratingsTable = new Rating[1];
        Rating ra = new Rating();
        ra.setPoints(5);
        ra.setTrack(doWyszukania);
        ratingsTable[0] = ra;
        List<Rating> ratings  = Arrays.asList(ratingsTable);
        int maxPoints=0;
        for (Rating r : ratings) {
            if (r.getPoints() > maxPoints)  t = r.getTrack();
        }

        return t;
    }
}
