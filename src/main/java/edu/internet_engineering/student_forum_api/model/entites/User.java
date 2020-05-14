package edu.internet_engineering.student_forum_api.model.entites;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;

@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    @Column(name = "username", unique = true, nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    public Long getId() {
        return user_id;
    }

    public void setId(Long id) { this.user_id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonSetter("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasRequiredFields() {
        return name != null && password != null && name.length() > 0 && password.length() > 0;
    }

    public boolean hasLongNameAndPassword() {
        return name.length() > 4 && password.length() > 4;
    }
}
