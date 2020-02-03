package edu.internet_engineering.student_forum_api.model.repo;

import edu.internet_engineering.student_forum_api.model.entites.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query(value = "SELECT * FROM category WHERE creation_date > :dateSince\n" +
            "ORDER BY CASE WHEN :orderStr = \"asc\" THEN creation_date END ASC,\n" +
            "CASE WHEN :orderStr = \"desc\" THEN creation_date END DESC LIMIT :limitInt", nativeQuery = true)
    List<Category> getAllCategories(Date dateSince, String orderStr, Integer limitInt);
}
