package com.cb.examples.jpajsp.geeticket.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cb.examples.jpajsp.geeticket.App;
import com.cb.examples.jpajsp.geeticket.Type;
import com.cb.examples.jpajsp.geeticket.model.TestBean;
import com.cb.examples.jpajsp.geeticket.model.Ticket;
import com.cb.examples.jpajsp.geeticket.model.User;
import com.cb.examples.jpajsp.geeticket.repository.Users;
import com.cb.examples.jpajsp.geeticket.service.TicketService;
import com.geemvc.HttpMethod;
import com.geemvc.Views;
import com.geemvc.annotation.Controller;
import com.geemvc.annotation.Request;
import com.geemvc.bind.param.annotation.PathParam;
import com.geemvc.view.bean.View;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
@Controller
@Request("/forms")
public class FormPlaygroundController {
    private final App app;
    private final TicketService ticketService;
    private final Users users;

    @Inject
    private Injector injector;

    @Inject
    private FormPlaygroundController(App app, TicketService ticketService, Users users) {
        this.app = app;
        this.ticketService = ticketService;
        this.users = users;
    }

    @Request("/playground")
    public View viewTicketDetails() {
        List<Ticket> tickets = ticketService.getTickets();

        Type[] selectedTypes = new Type[]{Type.FEATURE, Type.TASK};

        List<User> userList = users.all();
        Enumeration<User> userEnumeration = Collections.enumeration(userList);

        String[] allTags = new String[]{"super", "nasty", "feature", "bug", "usability", "ticket", "mobile", "conversion"};

        List<String> stringValues = Arrays.asList("one", "two", "three");
        List<Integer> integerValues = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        String[] stringArrayValues = new String[]{"one", "two", "three"};
        Integer[] integerArrayValues = new Integer[]{1, 2, 3};

        Enumeration<String> stringEnumerationValues = Collections.enumeration(stringValues);
        Enumeration<Integer> integerEnumerationValues = Collections.enumeration(integerValues);

        Map<String, String> stringMapValues = new LinkedHashMap<>();
        stringMapValues.put("one", "One");
        stringMapValues.put("two", "Two");
        stringMapValues.put("three", "Three");

        Map<Integer, Integer> integerMapValues = new LinkedHashMap<>();
        integerMapValues.put(1, 100);
        integerMapValues.put(2, 200);
        integerMapValues.put(3, 300);

        Map<Long, User> userMapValues = new LinkedHashMap<>();
        userMapValues.put(1l, users.havingId(1l));
        userMapValues.put(2l, users.havingId(2l));
        userMapValues.put(3l, users.havingId(3l));

        return Views.forward("playground/form_playground").bind("strVal", "two").bind("intVal", 2).bind("ticket", tickets.get(0)).bind("selectedTypes", selectedTypes).bind("allTags", allTags).bind("testBean", new TestBean("two", 2, 1, 3, 5, 7, 9))
                .bind("stringValues", stringValues).bind("integerValues", integerValues).bind("stringArrayValues", stringArrayValues).bind("integerArrayValues", integerArrayValues).bind("stringEnumerationValues", stringEnumerationValues)
                .bind("integerEnumerationValues", integerEnumerationValues).bind("stringMapValues", stringMapValues).bind("integerMapValues", integerMapValues).bind("userMapValues", userMapValues).bind("userList", userList)
                .bind("userEnumeration", userEnumeration);
    }

    @Request(path = "/save/{id:[\\d]+}", name = "unique-name-1", method = HttpMethod.POST)
    public View saveForm(@PathParam("id") Long id, Ticket ticket, TestBean testBean) {
        return Views.redirect("/forms/playground");
    }

    @Request(path = "/save/{id:[\\d]+}", name = "unique-name-2", method = HttpMethod.PUT)
    public View saveForm(@PathParam("id") Long id, Ticket ticket) {
        return Views.redirect("/forms/playground");
    }

    @Request(path = "/save", name = "unique-name-3")
    public View saveForm(Ticket ticket) {
        return Views.redirect("/forms/playground");
    }

    @Request(path = "/save", name = "unique-name-4")
    public View saveForm3(Ticket ticket) {
        return Views.redirect("/forms/playground");
    }
}
