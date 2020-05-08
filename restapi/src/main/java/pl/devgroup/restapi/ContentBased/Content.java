package pl.devgroup.restapi.ContentBased;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.devgroup.restapi.model.Rating;
import pl.devgroup.restapi.model.Track;
import pl.devgroup.restapi.model.TrackDetails;
import pl.devgroup.restapi.repository.RatingRepository;
import pl.devgroup.restapi.repository.UserRepository;
import pl.devgroup.restapi.service.TrackService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class Content {

    private RatingRepository ratingRepository;
    private TrackService trackService;
    private UserRepository userRepository;

    private List<TrackDetails> recoomendedTracks = new ArrayList<>();

    public Content() {}

    @Autowired
    public Content(RatingRepository ratingRepository, TrackService trackService, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.trackService = trackService;
        this.userRepository = userRepository;
    }
    public List<TrackDetails> createRecommendedList(String email) throws IOException {
        Track t = findBestRatedTrack(email);
       // recoomendedTracks.add(t); // dodanie 1 piosenki

        File[] files = new File("C:/Users/domin/OneDrive/Pulpit/Projekt/RecommenderSystem/restapi/src/main/resources/trackDetails").listFiles();
        TrackDetails bestSong = trackService.getTrackDetailById(t.getTrackId());
        List<TrackDetails> jsonList = getJsonFiles(files);
        getListOfSililarSong(bestSong, jsonList);

        System.out.println(recoomendedTracks);
        List<TrackDetails> newList = new ArrayList<>();
        for (int i=0; i<10 && i< recoomendedTracks.size(); i++)
        newList.add(recoomendedTracks.get(i));
        return newList;
    }

    public Track findBestRatedTrack(String activeUserEmail) throws IOException {
        Track t = new Track();
        List<Rating> ratings = ratingRepository.findAllByEmail(activeUserEmail);
        int maxPoints=0;
        for (Rating r : ratings) {
            if (r.getPoints() > maxPoints)  t = r.getTrack();
        }

        return t;
    }

    public void getListOfSililarSong(TrackDetails song, List<TrackDetails> listOfTrackDetails){
        String[][] tags = song.getTags();
        String bestTag = tags[0][0];
        String bestTag2;
        if (tags.length <= 1) {
            bestTag2 = bestTag;
        }
        else {
            bestTag2 = tags[1][0];
        }
        for (TrackDetails trackDetails : listOfTrackDetails) {
            String[][] newTags = trackDetails.getTags();
            for (int i=0; i<newTags.length; i++){
                if(bestTag.equals(newTags[i][0])){
                    for (int j=0; j <newTags.length; j++){
                        if (bestTag2.equals(newTags[j][0])) {
                            recoomendedTracks.add(trackDetails);
                        }
                    }
                }
            }
        }


    }

    public List<TrackDetails> getJsonFiles(File[] files) throws IOException {

        List<TrackDetails> list = new ArrayList<>();
        for (File file : files) {
            TrackDetails track = trackService.getTrackDetailByIdWithoutTrackWithOutJson(file.getName());
            list.add(track);
        }
        return list;
    }
}
