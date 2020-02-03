package edu.internet_engineering.student_forum_api.controller;

import edu.internet_engineering.student_forum_api.model.entites.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    private UserController userController;

    @BeforeAll
    public void initializeUserController() {
        userController = Mockito.mock(UserController.class);
    }

    @Test
    public void login_createNewUser_shouldReturnTokenWithUserName() {
        //Arrange
        User userToCreate = new User();
        userToCreate.setPassword("Password");
        userToCreate.setName("User");

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", "JWT");
        responseBody.put("name", "User");
        ResponseEntity<Map<String, String>> response = new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        Mockito.when(userController.loginUser(userToCreate)).thenReturn(response);

        //Act
        ResponseEntity<Map<String, String>> testedResponse = userController.loginUser(userToCreate);

        //Assert
        Assertions.assertEquals(testedResponse.getStatusCodeValue(), 201);
        Assertions.assertTrue(testedResponse.getBody().containsKey("name"));
        Assertions.assertTrue(testedResponse.getBody().containsKey("token"));
    }

}