package edu.internet_engineering.student_forum_api.model.repo;

import edu.internet_engineering.student_forum_api.model.entites.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT * FROM post WHERE creation_date > :limitDate AND thread_id = :threadId\n" +
            "ORDER BY CASE WHEN :orderStr = \"asc\" THEN creation_date END ASC,\n" +
            "CASE WHEN :orderStr = \"desc\" THEN creation_date END DESC LIMIT :limitInt", nativeQuery = true)
    List<Post> getPostsWithThread(Date limitDate, Long threadId, String orderStr, Integer limitInt);

    @Query(value = "SELECT * FROM post WHERE creation_date > :dateSince\n" +
            "ORDER BY CASE WHEN :orderStr = \"asc\" THEN creation_date END ASC,\n" +
            "CASE WHEN :orderStr = \"desc\" THEN creation_date END DESC LIMIT :limitInt", nativeQuery = true)
    List<Post> getAllPosts(Date dateSince, String orderStr, Integer limitInt);

}
