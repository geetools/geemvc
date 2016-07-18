package com.cb.examples.jpajsp.geeticket.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.cb.examples.jpajsp.geeticket.model.Ticket;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
@Transactional
public class Tickets {

    @Inject
    private Provider<EntityManager> entityManager;

    public Ticket havingId(Long id) {
        return entityManager().find(Ticket.class, id);
    }

    public List<Ticket> all() {
        return entityManager().createQuery("select t from Ticket t", Ticket.class).getResultList();
    }

    public Ticket add(Ticket ticket) {
        entityManager().persist(ticket);

        return havingId(ticket.getId());
    }

    public Ticket update(Ticket ticket) {
        entityManager().merge(ticket);

        return havingId(ticket.getId());
    }

    public void remove(Ticket ticket) {
        entityManager().remove(ticket);
    }

    protected EntityManager entityManager() {
        return entityManager.get();
    }
}
