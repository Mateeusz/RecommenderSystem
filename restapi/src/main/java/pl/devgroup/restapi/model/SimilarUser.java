package pl.devgroup.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimilarUser {

    private String email;
    private double similarity;
    private HashMap<CharSequence, Integer> matrix;
    private List<Rating> ratingList;

    public SimilarUser(String userId) {
        this.email = userId;
    }
}
