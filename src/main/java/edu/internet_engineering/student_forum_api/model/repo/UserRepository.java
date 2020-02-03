package edu.internet_engineering.student_forum_api.model.repo;

import edu.internet_engineering.student_forum_api.model.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query(value = "SELECT COUNT(*) FROM user usr WHERE usr.username = :name GROUP BY usr.username;", nativeQuery = true)
    Integer countUsersByName(String name);

    @Query(value = "SELECT * FROM user usr WHERE usr.username = :name", nativeQuery = true)
    User getUserByName(String name);

}
