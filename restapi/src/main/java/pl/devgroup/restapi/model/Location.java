package pl.devgroup.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "location_table")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    Integer locationId;

    @Column(name = "city_name")
    String cityName;

    @Column(name = "city_size")
    String citySize;

    @Column(name = "country_name")
    String countryName;

    @Column(name = "continent")
    String continent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
