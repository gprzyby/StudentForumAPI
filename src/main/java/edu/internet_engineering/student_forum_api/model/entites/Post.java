package edu.internet_engineering.student_forum_api.model.entites;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long post_id;

    @JsonIgnore
    @Column(name = "owner_id", nullable = false)
    private Long owner_id;

    @Column(nullable = false)
    private String body;

    @Column(name = "thread_id", nullable = false)
    private Long thread;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creation_date;

    public Long getId() {
        return post_id;
    }

    public void setId(Long id) {
        this.post_id = id;
    }

    @JsonGetter("owner_id")
    public Long getOwnerId() { return owner_id; }

    public void setOwnerId(Long owner) {
        this.owner_id = owner;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getThreadId() {
        return thread;
    }

    public void setThreadId(Long thread) {
        this.thread = thread;
    }

    @JsonSetter("thread_id")
    public void setThreadId(Integer threadId) { this.thread = threadId.longValue(); }

    @JsonGetter("creation_date")
    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public boolean hasRequiredFields() {
        return body != null && thread != null;
    }
}
