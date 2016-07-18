package com.cb.examples.jpajsp.geeticket.service;

import java.util.List;

import com.cb.examples.jpajsp.geeticket.model.Ticket;
import com.cb.examples.jpajsp.geeticket.model.User;
import com.cb.examples.jpajsp.geeticket.repository.Tickets;
import com.cb.examples.jpajsp.geeticket.repository.Users;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
public class TicketService {
    private final Tickets tickets;
    private final Users users;

    @Inject
    public TicketService(Tickets tickets, Users users) {
        this.tickets = tickets;
        this.users = users;
    }

    public List<Ticket> getTickets() {
        return tickets.all();
    }

    public Ticket getTicket(Long id) {
        return tickets.havingId(id);
    }

    public Ticket save(Ticket ticket) {

        if (ticket.getId() == null) {
            return tickets.add(ticket);
        } else {
            return tickets.update(ticket);
        }
    }

    public void remove(Ticket ticket) {
        tickets.remove(ticket);
    }
}
