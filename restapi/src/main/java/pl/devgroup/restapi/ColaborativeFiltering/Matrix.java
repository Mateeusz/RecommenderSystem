package pl.devgroup.restapi.ColaborativeFiltering;

import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.devgroup.restapi.model.Rating;
import pl.devgroup.restapi.model.SimilarUser;
import pl.devgroup.restapi.repository.RatingRepository;
import pl.devgroup.restapi.repository.UserRepository;
import pl.devgroup.restapi.service.TrackService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Matrix {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private TrackService trackService;
    @Autowired
    private UserRepository userRepository;


    public Map<CharSequence, Integer> getMatrix(String email) throws IOException {
        List<Rating> ratings = ratingRepository.findAllByEmail(email);
        List<List<String>> tags = trackService.getTags(ratings);

        HashMap<CharSequence, Integer> matrix = new HashMap();

        for (int i=0; i<ratings.size(); i++) {
            for (String tagName : tags.get(i)) {
                matrix.merge(tagName, ratings.get(i).getPoints(), Integer::sum);
            }
        }
//        for (List<String> tag : tags) {
//            for (String tagName : tag) {
//                System.out.println(tagName);
//            }
//        }

        return matrix;
    }

    public List<SimilarUser> getSimilarUsers(HashMap<CharSequence, Integer> appUserMatrix) throws IOException {

        List<String> emails = userRepository.getAllEmails();
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


}
