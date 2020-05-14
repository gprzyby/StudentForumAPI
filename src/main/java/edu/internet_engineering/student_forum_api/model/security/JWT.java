package edu.internet_engineering.student_forum_api.model.security;

import edu.internet_engineering.student_forum_api.model.entites.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class JWT {
    private static final String SECRET_KEY = "7WyKOWI3258jzbFS";

    public static String generateJWTToken(User user) {
        Date actualDate = new Date(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTime(actualDate);
        cal.add(Calendar.DAY_OF_WEEK, 1);

        return  Jwts.builder()
                    .claim("user_id", user.getId())
                    .setIssuedAt(actualDate)
                    .setExpiration(cal.getTime())
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    .compact();
    }

    public static Long getUserId(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            return Long.valueOf(claims.get("user_id").toString());
        } catch(Exception exc) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to do that. Try login as proper user");
        }
    }

}
