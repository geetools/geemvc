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

package com.geemvc.mock.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.geemvc.mock.bean.Person;
import com.google.inject.Inject;

public class Persons {
    @Inject
    protected EntityManager em;

    public List<Person> all() {
        return em.createQuery("select p from Person p", Person.class).getResultList();
    }

    public Person havingId(Long id) {
        return em.find(Person.class, id);
    }

    public Person add(Person person) {
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        em.persist(person);

        transaction.commit();

        return havingId(person.getId());
    }

    public void update(Person person) {
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        em.merge(person);

        transaction.commit();
    }

    public void remove(Person person) {
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        em.remove(person);

        transaction.commit();
    }
}
