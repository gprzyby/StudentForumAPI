package edu.internet_engineering.student_forum_api.model.entites;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long category_id;

    @Column(nullable = false)
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "parent_id")
    private Long parent_id;

    @JsonIgnore
    @Column(name = "owner_id", updatable = false)
    private Long ownerId;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creation_date;

    public Long getId() {
        return category_id;
    }

    public void setId(Long id) {
        this.category_id = id;
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

    public Long getParentId() {
        return parent_id;
    }

    public void setParentId(long category) {
        this.parent_id = category;
    }

    @JsonGetter(value="owner_id")
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @JsonGetter(value="creation_date")
    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public boolean hasRequiredFields() {
        return title != null;
    }
}
