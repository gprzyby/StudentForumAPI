package edu.internet_engineering.student_forum_api.controller;

import edu.internet_engineering.student_forum_api.model.entites.User;
import edu.internet_engineering.student_forum_api.model.repo.UserRepository;
import edu.internet_engineering.student_forum_api.model.security.JWT;
import edu.internet_engineering.student_forum_api.model.security.PasswordHash;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(HttpServletResponse response, @RequestBody User user) {
        if(!user.hasRequiredFields()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No username or password");
        } else if(!user.hasLongNameAndPassword()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password is to short. Min 4 characters");
        }

        //checking if user exists
        User existingUser = userRepository.getUserByName(user.getName());
        if(existingUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with that name exists!");
        }

        String hashedPassword = PasswordHash.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        String token = JWT.generateJWTToken(user);
        generateCookie(response, token, 24 * 60 * 60);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    @PostMapping("/login")
    public ResponseEntity<?> loginUser(HttpServletResponse response, @RequestBody User user) {
        if(!user.hasRequiredFields()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No username or password");
        }

        //checking if user exists
        User existingUser = userRepository.getUserByName(user.getName());
        if(existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            if(!PasswordHash.checkPassword(user.getPassword(), existingUser)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad username or password");
            }
            user.setId(existingUser.getId());
        }

        String token = JWT.generateJWTToken(user);

        generateCookie(response, token, 24 * 60 * 60);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        generateCookie(response, "", 1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static void generateCookie(HttpServletResponse response, String token, int timeout) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setMaxAge(timeout);     //one day age
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
