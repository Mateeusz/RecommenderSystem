package pl.devgroup.restapi.ColaborativeFiltering;

import com.google.common.collect.MinMaxPriorityQueue;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.devgroup.restapi.model.Rating;
import pl.devgroup.restapi.model.SimilarUser;
import pl.devgroup.restapi.model.Track;
import pl.devgroup.restapi.model.TrackDetails;
import pl.devgroup.restapi.repository.RatingRepository;
import pl.devgroup.restapi.repository.UserRepository;
import pl.devgroup.restapi.service.TrackService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

@Service
public class Matrix {

    private RatingRepository ratingRepository;
    private TrackService trackService;
    private UserRepository userRepository;

    private final static long LIST_LIMIT = 10;
    private final static int MATRIX_LIMIT = 10;


    @Autowired
    public Matrix(RatingRepository ratingRepository, TrackService trackService, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.trackService = trackService;
        this.userRepository = userRepository;
    }

    public List<TrackDetails> getRecommendations (String activeUserEmail) throws IOException {
        HashMap<CharSequence, Integer> userMap = (HashMap<CharSequence, Integer>) this.getMatrix(activeUserEmail);
        List<SimilarUser> list = this.getSimilarUsers(userMap, activeUserEmail);

        List<TrackDetails> recommendedTracks = new ArrayList<>();

        HashMap<CharSequence, Integer> recommendation = (HashMap<CharSequence, Integer>) this.createRecommendation(activeUserEmail, list);

        for(Map.Entry<CharSequence, Integer> entry : recommendation.entrySet()) {
            recommendedTracks.add(trackService.getTrackDetailById(String.valueOf(entry.getKey())));
        }

        return recommendedTracks;
    }

    private Map<CharSequence, Integer> getMatrix(String email) throws IOException {
        List<Rating> ratings = ratingRepository.findAllByEmail(email);
        List<List<String>> tags = trackService.getTags(ratings);

        HashMap<CharSequence, Integer> matrix = new HashMap<>();

        for (int i=0; i<ratings.size(); i++) {
            for (String tagName : tags.get(i)) {
                matrix.merge(tagName, ratings.get(i).getPoints(), Integer::sum);
            }
        }

        return matrix;
    }

    private List<SimilarUser> getSimilarUsers(HashMap<CharSequence, Integer> appUserMatrix, String activeUserEmail) throws IOException {
        List<String> emails = userRepository.getAllEmails(activeUserEmail);
        List<SimilarUser> similarUsers = new ArrayList<>();

        CosineSimilarity cosineSimilarity = new CosineSimilarity();

        for(String email : emails) {
            similarUsers.add(new SimilarUser(email));
        }

        for(SimilarUser similarUser : similarUsers) {
            similarUser.setMatrix((HashMap<CharSequence, Integer>) getMatrix(similarUser.getEmail()));
        }

        for(SimilarUser similarUser : similarUsers) {
            similarUser.setSimilarity(cosineSimilarity.cosineSimilarity(appUserMatrix, similarUser.getMatrix()));
        }

        return similarUsers;
    }

    private Map<CharSequence, Integer> createRecommendation(String activeUserEmail, List<SimilarUser> similarUsers) {
        List<Rating> userRatings = ratingRepository.findAllByEmail(activeUserEmail);
        List<Track> usersTracks = userRatings.stream().map(Rating::getTrack).collect(Collectors.toList());
        List<String> usersTrackIds = usersTracks.stream().map(Track::getTrackId).collect(Collectors.toList());
        List<Rating> mergeList = new ArrayList<>();

        for(SimilarUser similarUser : similarUsers) {
            similarUser.setRatingList(ratingRepository.findAllByEmail(similarUser.getEmail()));
            similarUser.getRatingList().removeIf(x -> usersTrackIds.contains(x.getTrack().getTrackId()));
            similarUser.getRatingList().sort(Comparator.comparing(Rating::getPoints));
            similarUser.getRatingList().stream().limit(LIST_LIMIT).collect(Collectors.toList());
            mergeList.addAll(similarUser.getRatingList());
        }

        HashMap<CharSequence, Integer> matrix = new HashMap<>();

        for (Rating rating : mergeList) {
            matrix.merge(rating.getTrack().getTrackId(), rating.getPoints(), Integer::sum);
        }
        Comparator<Map.Entry<CharSequence, Integer>> comparator = Map.Entry.comparingByValue(reverseOrder());

        MinMaxPriorityQueue<Map.Entry<CharSequence, Integer>> tops = MinMaxPriorityQueue.orderedBy(comparator)
                .maximumSize(MATRIX_LIMIT)
                .create(matrix.entrySet());

        Map<CharSequence, Integer> sorted = tops.stream()
                .sorted(comparator)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (m1, m2) -> m1,
                        LinkedHashMap::new));

        return sorted;
    }
}
