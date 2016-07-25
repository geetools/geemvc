package com.cb.examples.jpajsp.geeticket.controller;

import com.cb.examples.jpajsp.geeticket.App;
import com.cb.examples.jpajsp.geeticket.model.Ticket;
import com.cb.examples.jpajsp.geeticket.repository.Users;
import com.cb.examples.jpajsp.geeticket.service.TicketService;
import com.geemvc.Views;
import com.geemvc.annotation.Controller;
import com.geemvc.annotation.Request;
import com.geemvc.view.bean.View;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
@Controller
@Request("/forms-2")
public class FormPlaygroundController2 {
    private final App app;
    private final TicketService ticketService;
    private final Users users;

    @Inject
    private Injector injector;

    @Inject
    private FormPlaygroundController2(App app, TicketService ticketService, Users users) {
	this.app = app;
	this.ticketService = ticketService;
	this.users = users;
    }

    @Request("/playground")
    public String playground() {
	return "redirect: /forms/playground";
    }

    @Request("/save")
    public View saveForm(Ticket ticket) {
	return Views.redirect("/forms/playground");
    }
}
