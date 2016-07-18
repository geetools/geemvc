package com.cb.examples.jpajsp.geeticket.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.cb.examples.jpajsp.geeticket.model.Comment;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
@Transactional
public class Comments {
    @Inject
    private Provider<EntityManager> entityManager;

    public Comment havingId(Long id) {
        return entityManager().find(Comment.class, id);
    }

    public List<Comment> all() {
        return entityManager().createQuery("select c from Comment c", Comment.class).getResultList();
    }

    public Comment add(Comment comment) {
        entityManager().persist(comment);

        return havingId(comment.getId());
    }

    public Comment update(Comment comment) {
        entityManager().merge(comment);

        return havingId(comment.getId());
    }

    public void remove(Comment comment) {
        entityManager().remove(comment);
    }

    protected EntityManager entityManager() {
        return entityManager.get();
    }
}
