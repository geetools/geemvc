/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.geemvc.mock.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "person")
public class Person extends AbstractModel implements Model, Serializable {
    private static final long serialVersionUID = 1770546091807001672L;

    @NotNull
    @Id
    private Long id = null;

    @NotNull
    @Size(min = 3, max = 30)
    @Column(length = 30)
    private String forename = null;

    @NotNull
    @Size(min = 3, max = 30)
    @Column(length = 30)
    private String surname = null;

    @NotNull
    @Min(18)
    @Column
    private int age;

    @Transient
    private List<Address> addresses = null;

    @Transient
    private Locale locale = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", forename=" + forename + ", surname=" + surname + ", age=" + age + ", addresses=" + addresses + ", locale=" + locale + "]";
    }
}
