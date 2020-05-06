package edu.internet_engineering.student_forum_api.model.repo;

import edu.internet_engineering.student_forum_api.model.entites.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
    @Query(value = "SELECT * FROM thread WHERE creation_date > :limitDate AND tree_node_id = :categoryId\n" +
            "ORDER BY CASE WHEN :orderStr = \"asc\" THEN creation_date END ASC,\n" +
            "CASE WHEN :orderStr = \"desc\" THEN creation_date END DESC LIMIT :limitInt", nativeQuery = true)
    List<Thread> getThreadsWithCategory(Date limitDate, Long categoryId, String orderStr, Integer limitInt);

    @Query(value = "SELECT * FROM thread WHERE creation_date > :limitDate\n" +
            "ORDER BY CASE WHEN :orderStr = \"asc\" THEN creation_date END ASC,\n" +
            "CASE WHEN :orderStr = \"desc\" THEN creation_date END DESC LIMIT :limitInt", nativeQuery = true)
    List<Thread> getAllThreads(Date limitDate, String orderStr, Integer limitInt);

    @Query(value = "SELECT th FROM thread th WHERE th.category_id = :tree_id")
    Page<Thread> findAllByCategoryId(Long tree_id, Pageable pageFilter);
}
