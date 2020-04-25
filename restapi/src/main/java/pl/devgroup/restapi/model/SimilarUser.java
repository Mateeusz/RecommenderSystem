package pl.devgroup.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimilarUser {

    private String email;
    private double similarity;
    private HashMap<CharSequence, Integer> matrix;

    public SimilarUser(String userId) {
        this.email = userId;
    }
}
