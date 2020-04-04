package edu.internet_engineering.student_forum_api.controller;

import edu.internet_engineering.student_forum_api.model.entites.Category;
import edu.internet_engineering.student_forum_api.model.entites.Post;
import edu.internet_engineering.student_forum_api.model.entites.Thread;
import edu.internet_engineering.student_forum_api.model.repo.PostRepository;
import edu.internet_engineering.student_forum_api.model.repo.ThreadRepository;
import edu.internet_engineering.student_forum_api.model.security.JWT;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class PostController {
    private PostRepository postRepository;
    private ThreadRepository threadRepository;

    public PostController(PostRepository postRepository, ThreadRepository threadRepository) {
        this.postRepository = postRepository;
        this.threadRepository = threadRepository;
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestHeader String authorization, @RequestBody Post newPost) {
        if(!newPost.hasRequiredFields()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thread doesn't have title!");
        }

        Long userId = JWT.getUserId(authorization);
        newPost.setOwnerId(userId);

        Optional<Thread> threadParent = threadRepository.findById(newPost.getThreadId());
        if(!threadParent.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent category not found");
        }

        Post savedPost = postRepository.save(newPost);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam(name = "limit", required = false) Integer limit, @RequestParam(name = "since", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-SS") Date since, @RequestParam(value = "asc", required = false) String order) {
        //checking path variables
        if(limit == null) limit = 10;
        if(limit < 0) limit = 0;
        limit = Math.min(limit, 25);
        if(order == null || !(order.equals("asc") || order.equals("desc"))) {
            order = "asc";
        }

        if(since == null) {
            since = new Date(0);
        }

        //getting categories
        List<Post> responsePosts = postRepository.getAllPosts(since, order, limit);

        return new ResponseEntity<>(responsePosts, HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getThread(@PathVariable("id") Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(!post.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(post.get(), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@RequestHeader String authorization, @PathVariable Long id) {
        Optional<Post> dbPostOptional = postRepository.findById(id);
        Long userId = JWT.getUserId(authorization);

        if(!dbPostOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Post dbPost = dbPostOptional.get();

        if(!userId.equals(dbPost.getOwnerId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        postRepository.delete(dbPost);

        return new ResponseEntity<>(HttpStatus.valueOf(204));
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@RequestHeader String authorization, @PathVariable Long id, @RequestBody Post postBody) {
        Optional<Post> dbPostOptional = postRepository.findById(id);
        Long userId = JWT.getUserId(authorization);

        if(!dbPostOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Post dbPost = dbPostOptional.get();

        if(!userId.equals(dbPost.getOwnerId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        postBody.setOwnerId(dbPost.getOwnerId());
        postBody.setCreation_date(dbPost.getCreation_date());
        postBody.setId(dbPost.getId());
        if(postBody.getThreadId() == null) {
            postBody.setThreadId(dbPost.getThreadId());
        }


        Post savedPost = postRepository.save(postBody);

        return new ResponseEntity<>(savedPost, HttpStatus.OK);
    }
}
