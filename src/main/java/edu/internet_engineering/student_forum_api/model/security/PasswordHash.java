package edu.internet_engineering.student_forum_api.model.security;

import edu.internet_engineering.student_forum_api.model.entites.User;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, User user) {
        return BCrypt.checkpw(password, user.getPassword());
    }

}
