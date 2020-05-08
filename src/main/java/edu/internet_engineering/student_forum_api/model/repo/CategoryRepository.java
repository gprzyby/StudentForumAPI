package edu.internet_engineering.student_forum_api.model.repo;

import edu.internet_engineering.student_forum_api.model.entites.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query(value = "SELECT * FROM category WHERE category_id > :sinceId\n" +
            "ORDER BY CASE WHEN :orderStr = \"asc\" THEN category_id END ASC,\n" +
            "CASE WHEN :orderStr = \"desc\" THEN category_id END DESC LIMIT :limitInt", nativeQuery = true)
    List<Category> getAllCategories(Long sinceId, String orderStr, Integer limitInt);

    @Query("SELECT c FROM category c WHERE c.parent_id IS NULL")
    Page<Category> getCategoriesBy(Pageable pageRequest);

    @Query("SELECT c FROM category c WHERE c.parent_id = :parentId")
    Page<Category> getCategoiresChildrens(Long parentId, Pageable pageLimit);
}
