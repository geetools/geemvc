package com.cb.examples.jpajsp.geeticket.controller;

import com.cb.examples.jpajsp.geeticket.App;
import com.cb.geemvc.Views;
import com.cb.geemvc.annotation.Controller;
import com.cb.geemvc.annotation.Request;
import com.cb.geemvc.view.bean.View;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
@Controller
@Request("/layouts")
public class LayoutPlaygroundController {
    private final App app;

    @Inject
    private Injector injector;

    @Inject
    private LayoutPlaygroundController(App app) {
	this.app = app;
    }

    @Request("/playground")
    public View view() {

	return Views.forward("playground/layout_playground");
    }
}
