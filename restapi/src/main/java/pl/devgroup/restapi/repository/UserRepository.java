package pl.devgroup.restapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.devgroup.restapi.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT email FROM user_table WHERE (email != :active_user) ;", nativeQuery = true)
    List<String> getAllEmails(@Param("active_user") String email);
}
