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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String[][] getSimilars() {
        return similars;
    }

    public void setSimilars(String[][] similars) {
        this.similars = similars;
    }

    public String[][] getTags() {
        return tags;
    }

    public void setTags(String[][] tags) {
        this.tags = tags;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @JsonProperty("title")
    private String title;

    private int points;
}
