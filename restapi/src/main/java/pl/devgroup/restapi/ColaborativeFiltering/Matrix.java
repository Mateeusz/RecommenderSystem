package pl.devgroup.restapi.ColaborativeFiltering;

import org.springframework.beans.factory.annotation.Autowired;
import pl.devgroup.restapi.model.Rating;
import pl.devgroup.restapi.repository.RatingRepository;
import pl.devgroup.restapi.service.TrackService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Matrix {

    private RatingRepository ratingRepository;
    private TrackService trackService;

    @Autowired
    public Matrix(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }


//FIXME
    private Map<CharSequence, Integer> getMatrix(String email) throws IOException {
        List<Rating> ratings = ratingRepository.findAllByEmail(email);
        List<String[][]> tags = trackService.getTags(ratings);



        return
    }
}
