package com.cb.examples.jpajsp.geeticket.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.geemvc.Char;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id = null;

    private String username = null;

    private String password = null;

    private String forename = null;

    private String surname = null;

    private Date createdOn = null;

    private Date modifiedOn = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return new StringBuilder(forename).append(Char.SPACE).append(surname).toString();
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
        return "User [id=" + id + ", username=" + username + ", password=" + (password == null ? null : password.substring(0, 2) + password.substring(3).replaceAll(".", "*")) + ", forename=" + forename + ", surname=" + surname + ", createdOn="
                + createdOn + ", modifiedOn=" + modifiedOn + "]";
    }
}
