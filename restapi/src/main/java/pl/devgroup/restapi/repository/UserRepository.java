package pl.devgroup.restapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.devgroup.restapi.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Query(value = "SELECT email FROM user_table ;", nativeQuery = true)
    List<String> getAllEmails();
}
