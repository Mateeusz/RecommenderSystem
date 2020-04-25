package pl.devgroup.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tracks")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Track {

    @Id
    @Column(name = "track_id")
    String trackId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "track")
    private Set<Rating> ratings;
}
