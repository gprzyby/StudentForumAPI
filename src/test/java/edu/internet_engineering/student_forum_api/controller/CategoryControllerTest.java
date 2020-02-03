package edu.internet_engineering.student_forum_api.controller;

import edu.internet_engineering.student_forum_api.model.entites.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryControllerTest {

    private CategoryController categoryController;

    @BeforeAll
    public void initializeController() {
        categoryController = Mockito.mock(CategoryController.class);
    }

    @Test
    void getAllCategories_gettingListOfCategories_shouldReturnMoreThanOneCategory() {
        //Arrange
        List<Category> categoriesList = new LinkedList<>();
        categoriesList.add(new Category());
        categoriesList.add(new Category());

        Mockito.when(categoryController.getAllCategories(null,null,null)).thenReturn(new ResponseEntity<>(categoriesList, HttpStatus.OK));

        //Act
        ResponseEntity<List<Category>> response = categoryController.getAllCategories(null,null,null);

        //Assert
        Assertions.assertEquals(response.getBody().size(), 2);
    }

    @Test
    void createCategory_creatingTestCategory_shouldReturnOk() {
        //Arrange
        Mockito.when(categoryController.createCategory(Mockito.eq("JWT"), Mockito.any(Category.class))).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        //Act
        ResponseEntity<Category> response = categoryController.createCategory("JWT", new Category());

        //Assert
        Assertions.assertEquals(response.getStatusCodeValue(), 201);
    }

    @Test
    void getCategory_gettingNotExistingCategory_throwResponseStatusException() {
        //Arrange
        Mockito.when(categoryController.getCategory(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        //Act and assert
        Assertions.assertThrows(ResponseStatusException.class,() -> categoryController.getCategory(1L));
    }

    @Test
    void deleteCategory_deletingWithEmptyToken_throwResponseStatusException() {
        //Arrange
        Mockito.when(categoryController.deleteCategory(Mockito.eq(""), Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Assertions.assertThrows(ResponseStatusException.class, () -> categoryController.deleteCategory("", 1L));
    }

    @Test
    void updateCategory_modifyExistingCategory_shouldReturnOk() {
        //Arrange
        Category categoryToModifie = new Category();
        categoryToModifie.setTitle("Title");
        categoryToModifie.setSubtitle("empty");
        Mockito.when(categoryController.updateCategory("JWT", 0L, categoryToModifie)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Category> response = categoryController.updateCategory("JWT", 0L, categoryToModifie);

        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());

    }

    @Test
    void getCategoryThreads() {
    }
}