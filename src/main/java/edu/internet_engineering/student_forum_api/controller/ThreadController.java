package edu.internet_engineering.student_forum_api.controller;

import edu.internet_engineering.student_forum_api.model.entites.Category;
import edu.internet_engineering.student_forum_api.model.entites.Post;
import edu.internet_engineering.student_forum_api.model.repo.CategoryRepository;
import edu.internet_engineering.student_forum_api.model.repo.PostRepository;
import edu.internet_engineering.student_forum_api.model.repo.ThreadRepository;
import edu.internet_engineering.student_forum_api.model.entites.Thread;
import edu.internet_engineering.student_forum_api.model.security.JWT;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ThreadController {
    private CategoryRepository categoryRepository;
    private ThreadRepository threadRepository;
    private PostRepository postRepository;

    public ThreadController(ThreadRepository threadRepository, PostRepository postRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.threadRepository = threadRepository;
    }

    @PostMapping("/threads")
    public ResponseEntity<Thread> createThread(@CookieValue(value = "jwt", required = false) String authorization, @RequestBody Thread newThread) {
        if(!newThread.hasRequiredFields()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thread doesn't have required fields!");
        }

        Long userId = JWT.getUserId(authorization);
        newThread.setOwnerId(userId);

        Optional<Category> parentCategory = categoryRepository.findById(newThread.getCategoryId());
        if(!parentCategory.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent category not found");
        }

        Thread savedThread = threadRepository.save(newThread);
        return new ResponseEntity<>(savedThread, HttpStatus.CREATED);
    }

    @GetMapping("/threads")
    public ResponseEntity<List<Thread>> getAllThreads(@RequestParam(name = "limit", required = false) Integer limit, @RequestParam(name = "since", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-SS") Date since, @RequestParam(value = "asc", required = false) String order) {
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
        List<Thread> responseCatories = threadRepository.getAllThreads(since, order, limit);

        return new ResponseEntity<>(responseCatories, HttpStatus.OK);
    }

    @GetMapping("/threads/{id}")
    public ResponseEntity<Thread> getThread(@PathVariable("id") Long threadId) {
        Optional<Thread> thread = threadRepository.findById(threadId);
        if(!thread.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(thread.get(), HttpStatus.OK);
    }

    @DeleteMapping("/threads/{id}")
    public ResponseEntity<?> deleteThread(@CookieValue(value = "jwt", required = false) String authorization, @PathVariable Long id) {
        Optional<Thread> dbThreadOptional = threadRepository.findById(id);
        Long userId = JWT.getUserId(authorization);

        if(!dbThreadOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Thread dbThread = dbThreadOptional.get();

        if(!userId.equals(dbThread.getOwnerId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        threadRepository.delete(dbThread);

        return new ResponseEntity<>(HttpStatus.valueOf(204));
    }

    @PutMapping("/threads/{id}")
    public ResponseEntity<Thread> updateThread(@CookieValue(value = "jwt", required = false) String authorization, @PathVariable Long id, @RequestBody Thread threadBody) {
        Optional<Thread> dbThreadOptional = threadRepository.findById(id);
        Long userId = JWT.getUserId(authorization);

        if(!dbThreadOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Thread dbThread = dbThreadOptional.get();

        if(!userId.equals(dbThread.getOwnerId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        threadBody.setOwnerId(dbThread.getOwnerId());
        threadBody.setCreation_date(dbThread.getCreation_date());
        threadBody.setId(dbThread.getId());
        if(threadBody.getTitle() == null) {
            threadBody.setTitle(dbThread.getTitle());
        }


        Thread savedThread = threadRepository.save(threadBody);

        return new ResponseEntity<>(savedThread, HttpStatus.OK);
    }

    @GetMapping("/threads/{id}/posts")
    public ResponseEntity<List<Post>> getThreadPosts(@PathVariable Long id, @RequestParam(name = "limit", required = false) Integer limit, @RequestParam(name = "since", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-SS") Date since, @RequestParam(value = "asc", required = false) String order) {
        if(limit == null) limit = 10;

        limit = Math.min(limit, 25);
        if(order == null || !(order.equals("asc") || order.equals("desc"))) {
            order = "asc";
        }

        if(since == null) {
            since = new Date(0);
        }

        if (!threadRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        List<Post> responsePosts = postRepository.getPostsWithThread(since, id, order, limit);

        return new ResponseEntity<>(responsePosts, HttpStatus.OK);
    }

    @GetMapping("threads/{id}/posts/pageable")
    public ResponseEntity<Page<Post>> getThreadPostsPaged(@PathVariable Long id, Pageable pageFilter) {
        if(!threadRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found");
        }

        Page<Post> queriedData = postRepository.findAllByThread(id, pageFilter);

        return new ResponseEntity<>(queriedData, HttpStatus.OK);
    }

}
