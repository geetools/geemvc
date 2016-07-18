package com.cb.examples.jpajsp.geeticket.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.cb.examples.jpajsp.geeticket.model.User;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
@Transactional
public class Users {
    @Inject
    private Provider<EntityManager> entityManager;

    public User havingId(Long id) {
        return entityManager().find(User.class, id);
    }

    public List<User> all() {
        return entityManager().createQuery("select u from User u", User.class).getResultList();
    }


    public User add(User user) {
        entityManager().persist(user);

        return havingId(user.getId());
    }

    public User update(User user) {
        entityManager().merge(user);

        return havingId(user.getId());
    }

    public void remove(User user) {
        entityManager().remove(user);
    }

    protected EntityManager entityManager() {
        return entityManager.get();
    }
}
