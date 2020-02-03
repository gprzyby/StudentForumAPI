package edu.internet_engineering.student_forum_api.controller;

import edu.internet_engineering.student_forum_api.model.entites.User;
import edu.internet_engineering.student_forum_api.model.repo.UserRepository;
import edu.internet_engineering.student_forum_api.model.security.JWT;
import edu.internet_engineering.student_forum_api.model.security.PasswordHash;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>>  loginUser(@RequestBody User user) {
        if(!user.hasRequiredFields()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No username or password");
        } else if(!user.hasLongNameAndPassword()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password is to short. Min 4 characters");
        }

        //checking if user exists
        User existingUser = userRepository.getUserByName(user.getName());
        if(existingUser == null) {
            String hashedPassword = PasswordHash.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

            userRepository.save(user);
        } else {
            if(!PasswordHash.checkPassword(user.getPassword(), existingUser)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad username or password");
            }
        }

        String token = JWT.generateJWTToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("name", user.getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
