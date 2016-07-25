package com.cb.examples.jpajsp.geeticket;

import java.util.Date;

import com.cb.examples.jpajsp.geeticket.model.Comment;
import com.cb.examples.jpajsp.geeticket.model.Ticket;
import com.cb.examples.jpajsp.geeticket.model.User;
import com.cb.examples.jpajsp.geeticket.repository.Comments;
import com.cb.examples.jpajsp.geeticket.repository.Tickets;
import com.cb.examples.jpajsp.geeticket.repository.Users;
import com.geemvc.inject.Injectors;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
public class App {
    private final Users users;
    private final Tickets tickets;
    private final Comments comments;

    @Inject
    protected App(Users users, Tickets tickets, Comments comments) {
        this.users = users;
        this.tickets = tickets;
        this.comments = comments;

        setupData();
    }

    @Transactional
    public void setupData() {
        Injector injector = Injectors.provide();

        User u1 = injector.getInstance(User.class);
        u1.setForename("Tom");
        u1.setSurname("Checker");
        u1.setUsername("tom.checker");
        u1.setPassword("test");
        users.add(u1);

        User u2 = injector.getInstance(User.class);
        u2.setForename("Marc");
        u2.setSurname("Dude");
        u2.setUsername("marc.dude");
        u2.setPassword("test");
        users.add(u2);

        User u3 = injector.getInstance(User.class);
        u3.setForename("Lea");
        u3.setSurname("Cool");
        u3.setUsername("lea.cool");
        u3.setPassword("test");
        users.add(u3);

        Ticket t1 = injector.getInstance(Ticket.class);
        t1.setTitle("New super feature");
        t1.setDescription("Please implement the new super feature.");
        t1.setType(Type.FEATURE);
        t1.setPriority(Priority.HIGH);
        t1.setStatus(Status.NEW);
        t1.addTags("super", "feature", "usability", "mobile");
        t1.setReporter(u1);
        t1.setAssignee(u2);
        t1.setCreatedOn(new Date());

        Comment c1 = injector.getInstance(Comment.class);
        c1.setComment("I have a question. How should I implement this super feature?");
        c1.setUser(u2);
        c1.setCreatedOn(new Date());
        t1.addComment(c1);
        Comment c2 = injector.getInstance(Comment.class);
        c2.setComment("Don't ask me, just do it my friend!");
        c2.setUser(u1);
        c2.setCreatedOn(new Date());
        t1.addComment(c2);

        Ticket t2 = injector.getInstance(Ticket.class);
        t2.setTitle("Nasty bug");
        t2.setDescription("Please fix this nasty bug. It has caused a conversion drop of 80%!");
        t2.setType(Type.BUG);
        t2.setPriority(Priority.HIGHEST);
        t2.setStatus(Status.IN_PROGRESS);
        t2.addTags("nasty", "bug", "ticket", "conversion");
        t2.setReporter(u1);
        t2.setAssignee(u3);
        t2.setCreatedOn(new Date());

        Comment c3 = injector.getInstance(Comment.class);
        c3.setComment("Hi, I cannot reproduce the bug - it works on my local PC. Could you please describe it in more detail?");
        c3.setUser(u3);
        c3.setCreatedOn(new Date());
        t2.addComment(c3);
        Comment c4 = injector.getInstance(Comment.class);
        c4.setComment("Yes, just open up the homepage in your browser. All I get is a white page!");
        c4.setUser(u1);
        c4.setCreatedOn(new Date());
        t2.addComment(c4);

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);
        comments.add(c4);

        tickets.add(t1);
        tickets.add(t2);
    }
}
