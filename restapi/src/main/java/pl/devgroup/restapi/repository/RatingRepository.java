package pl.devgroup.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.devgroup.restapi.model.Rating;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query(value = "SELECT * FROM rating WHERE track_id = :track AND user_id = (SELECT  user_id FROM user_table WHERE email = :user_email);", nativeQuery = true)
    Rating getRating(@Param("track") String trackId, @Param("user_email") String userEmail);

    @Modifying
    @Transactional
    @Query(value = "UPDATE rating SET points = :points WHERE track_id = :track AND user_id = (SELECT  user_id FROM user_table WHERE email = :user_email);", nativeQuery = true)
    void updateRating(@Param("points") int points, @Param("track") String trackId, @Param("user_email") String userEmail);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO rating(points, user_id, track_id) VALUES (:points, (SELECT  user_id FROM user_table WHERE email = :user_email), :track);", nativeQuery = true)
    void insertRating(@Param("points") int points, @Param("track") String trackId, @Param("user_email") String userEmail);

    @Transactional
    @Query(value = "SELECT * FROM rating WHERE user_id = (SELECT  user_id FROM user_table WHERE email = :user_email LIMIT 1)", nativeQuery = true)
    List<Rating> findAllByEmail(@Param("user_email") String email);
}
