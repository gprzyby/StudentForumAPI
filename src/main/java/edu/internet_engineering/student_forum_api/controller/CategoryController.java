package edu.internet_engineering.student_forum_api.controller;

import edu.internet_engineering.student_forum_api.model.entites.Category;
import edu.internet_engineering.student_forum_api.model.entites.Thread;
import edu.internet_engineering.student_forum_api.model.repo.CategoryRepository;
import edu.internet_engineering.student_forum_api.model.repo.ThreadRepository;
import edu.internet_engineering.student_forum_api.model.security.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ThreadRepository threadRepository;


    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(@RequestParam(name = "limit", required = false) Integer limit, @RequestParam(name = "since", required = false) Long sinceId, @RequestParam(value = "asc", required = false) String order) {
        //checking path variables
        if(limit == null) limit = 10;
        if(limit < 0) limit = 0;
        limit = Math.min(limit, 25);
        if(order == null || !(order.equals("asc") || order.equals("desc"))) {
            order = "asc";
        }

        if(sinceId == null) {
            sinceId = 0L;
        }

        //getting categories
        List<Category> responseCatories = categoryRepository.getAllCategories(sinceId, order, limit);
        return new ResponseEntity<>(responseCatories, HttpStatus.OK);
    }

    @GetMapping("/categories/pageable")
    public ResponseEntity<Page<Category>> getCategoriesTest(Pageable pageable) {
        return new ResponseEntity<Page<Category>>(categoryRepository.getCategoriesBy(pageable), HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestHeader String authorization, @RequestBody Category newCategory) {
        if(!newCategory.hasRequiredFields()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category doesn't have title!");
        }

        Long userId = JWT.getUserId(authorization);
        newCategory.setOwnerId(userId);

        if(newCategory.getParentId() != null) {
            Optional<Category> parentCategory = categoryRepository.findById(newCategory.getParentId());
            if(!parentCategory.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent category not found");
            }
        }

        Category savedCategory = categoryRepository.save(newCategory);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(!category.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(category.get(), HttpStatus.OK);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@RequestHeader String authorization, @PathVariable Long id) {
        Optional<Category> dbCategoryOptional = categoryRepository.findById(id);
        Long userId = JWT.getUserId(authorization);

        if(!dbCategoryOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Category dbCategory = dbCategoryOptional.get();

        if(!userId.equals(dbCategory.getOwnerId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        categoryRepository.delete(dbCategory);

        return new ResponseEntity<>(HttpStatus.valueOf(204));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(@RequestHeader String authorization, @PathVariable Long id, @RequestBody Category categoryBody) {
        Optional<Category> dbCategoryOptional = categoryRepository.findById(id);
        Long userId = JWT.getUserId(authorization);

        if(!dbCategoryOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Category dbCategory = dbCategoryOptional.get();

        if(!userId.equals(dbCategory.getOwnerId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        categoryBody.setOwnerId(dbCategory.getOwnerId());
        categoryBody.setCreation_date(dbCategory.getCreation_date());
        categoryBody.setId(dbCategory.getId());
        if(categoryBody.getTitle() == null) {
            categoryBody.setTitle(dbCategory.getTitle());
        }


        Category savedCategory = categoryRepository.save(categoryBody);

        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    @GetMapping("/categories/{id}/threads")
    public ResponseEntity<List<Thread>> getCategoryThreads(@PathVariable Long id, @RequestParam(name = "limit", required = false) Integer limit, @RequestParam(name = "since", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-SS") Date since, @RequestParam(value = "asc", required = false) String order) {
        if(limit == null) limit = 10;

        limit = Math.min(limit, 25);
        if(order == null || !(order.equals("asc") || order.equals("desc"))) {
            order = "asc";
        }

        if(since == null) {
            since = new Date(0);
        }

        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        List<Thread> responseThreads = threadRepository.getThreadsWithCategory(since, id, order, limit);

        return new ResponseEntity<>(responseThreads, HttpStatus.OK);
    }

    @GetMapping("/categories/{id}/threads/pageable")
    public ResponseEntity<Page<Thread>> getCategoryThreadsPageable(@PathVariable Long id, Pageable pageFilter) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        return new ResponseEntity<>(threadRepository.findAllByCategoryId(id, pageFilter), HttpStatus.OK);
    }
}
