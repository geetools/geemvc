package com.cb.examples.jpajsp.geeticket.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue
    private Long id = null;

    private String comment = null;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false, name = "user_id")
    private User user = null;

    private Date createdOn = null;

    private Date modifiedOn = null;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Date getCreatedOn() {
	return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
	this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
	return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
	this.modifiedOn = modifiedOn;
    }

    @Override
    public String toString() {
	return "Comment [id=" + id + ", comment=" + comment + ", user=" + user + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + "]";
    }


}
