package pl.devgroup.restapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackDetails {

    @JsonProperty("artist")
    private String artist;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("similars")
    private String[][] similars;

    @JsonProperty("tags")
    private String[][] tags;

    @JsonProperty("track_id")
    private String trackId;

    @JsonProperty("title")
    private String title;

    private int points;
}
