package edu.internet_engineering.student_forum_api.model.entites;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "thread")
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long thread_id;

    @Column(name = "tree_node_id")
    private Long category_id;

    @Column(nullable = false)
    private String title;

    @Column
    private String subtitle;

    @JsonIgnore
    @Column(name = "owner_id", updatable = false)
    private Long owner_id;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creation_date;

    public Long getId() {
        return thread_id;
    }

    public void setId(Long id) {
        this.thread_id = id;
    }

    public Long getCategoryId() {
        return category_id;
    }

    public void setCategoryId(Long category) {
        this.category_id = category;
    }

    @JsonSetter("category_id")
    public void setCategoryId(Integer categoryId) {
        this.category_id = Long.valueOf(categoryId);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @JsonGetter("owner_id")
    public Long getOwnerId() {
        return owner_id;
    }

    public void setOwnerId(Long owner) {
        this.owner_id = owner;
    }

    @JsonGetter("creation_date")
    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public boolean hasRequiredFields() {
        return title != null && category_id != null && title.length() > 0;
    }
}
