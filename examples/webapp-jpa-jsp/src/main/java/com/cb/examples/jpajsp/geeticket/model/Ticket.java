package com.cb.examples.jpajsp.geeticket.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cb.examples.jpajsp.geeticket.Priority;
import com.cb.examples.jpajsp.geeticket.Status;
import com.cb.examples.jpajsp.geeticket.Type;
import com.cb.examples.jpajsp.geeticket.repository.Users;
import com.cb.geemvc.inject.Injectors;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue
    private Long id = null;

    private String title = null;

    private String description = null;

    private Type type = null;

    private Priority priority = null;

    private Status status = null;

    @ElementCollection
    @CollectionTable(name = "tags")
    private Set<String> tags;

    @OneToMany
    private List<Comment> comments = null;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignee_id", nullable = false)
    private User assignee = null;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter = null;

    private Date createdOn = null;

    private Date modifiedOn = null;

    @Transient
    private final Users users;

    public Ticket() {
	this.users = Injectors.provide().getInstance(Users.class);
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Type getType() {
	return type;
    }

    public void setType(Type type) {
	this.type = type;
    }

    public Priority getPriority() {
	return priority;
    }

    public void setPriority(Priority priority) {
	this.priority = priority;
    }

    public Status getStatus() {
	return status;
    }

    public void setStatus(Status status) {
	this.status = status;
    }

    public Set<String> getTags() {
	return tags;
    }

    public void setTags(Set<String> tags) {
	this.tags = tags;
    }

    public void addTags(String... tags) {
	if (tags != null) {
	    for (String tag : tags) {
		addTag(tag);
	    }
	}
    }

    public void addTag(String tag) {
	if (tags == null)
	    tags = new LinkedHashSet<>();

	tags.add(tag);
    }

    public List<Comment> getComments() {
	return comments;
    }

    public void setComments(List<Comment> comments) {
	this.comments = comments;
    }

    public void addComment(Comment comment) {
	if (comments == null)
	    comments = new ArrayList<>();

	comments.add(comment);
    }

    public User getAssignee() {
	return assignee;
    }

    public void setAssignee(User assignee) {
	this.assignee = assignee;
    }

    public Long getAssigneeId() {
	return this.assignee == null ? null : this.assignee.getId();
    }

    public void setAssigneeId(Long userId) {
	this.assignee = users.havingId(userId);
    }

    public User getReporter() {
	return reporter;
    }

    public void setReporter(User reporter) {
	this.reporter = reporter;
    }

    public Long getReporterId() {
	return this.reporter == null ? null : this.reporter.getId();
    }

    public void setReporterId(Long userId) {
	this.reporter = users.havingId(userId);
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
	return "Ticket [id=" + id + ", title=" + title + ", description=" + description + ", type=" + type + ", priority=" + priority + ", status=" + status + ", comments=" + comments + ", assignee=" + assignee + ", reporter=" + reporter
		+ ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + "]";
    }

}
