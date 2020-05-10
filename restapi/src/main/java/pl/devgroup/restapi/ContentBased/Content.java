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
import java.util.*;
import java.util.stream.Collectors;

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

        File[] files = new File("src/main/resources/trackDetails").listFiles();
        TrackDetails bestSong = trackService.getTrackDetailById(t.getTrackId());
        List<TrackDetails> jsonList = getJsonFiles(files);
        getListOfSililarSong(bestSong, jsonList);

//        System.out.println(recoomendedTracks);
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
        HashMap<TrackDetails, Integer> trackDetailsMap = new HashMap<>();
        String[][] tags = song.getTags();
        HashMap<String, Integer> mapOfTags = new HashMap<>();
        for (int i=0; i<tags.length; i++){
            mapOfTags.put(tags[i][0], Integer.valueOf(tags[i][1]));
        }

        int tagWeight = 0;
        for (TrackDetails trackDetails : listOfTrackDetails) {
            String[][] newTags = trackDetails.getTags();
            for (int i = 0; i<mapOfTags.size(); i++){
                for (int j = 0; j<newTags.length; j++){
                    if(mapOfTags.keySet().toArray()[i].equals(newTags[j][0])) {
                         tagWeight = tagWeight + Math.min(Integer.valueOf(newTags[j][1]),Integer.valueOf(mapOfTags.values().toArray()[i].toString()));

                    }
                }
            }
            trackDetailsMap.put(trackDetails, tagWeight);
            tagWeight=0;
        }
        trackDetailsMap = sortByValue(trackDetailsMap);
        for (TrackDetails track:trackDetailsMap.keySet()  ) {
            recoomendedTracks.add(track);
        }
    }

    public static HashMap<TrackDetails, Integer> sortByValue(HashMap<TrackDetails, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<TrackDetails, Integer> > list =
                new LinkedList<Map.Entry<TrackDetails, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<TrackDetails, Integer> >() {
            public int compare(Map.Entry<TrackDetails, Integer> o1,
                               Map.Entry<TrackDetails, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<TrackDetails, Integer> temp = new LinkedHashMap<TrackDetails, Integer>();
        for (Map.Entry<TrackDetails, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
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
