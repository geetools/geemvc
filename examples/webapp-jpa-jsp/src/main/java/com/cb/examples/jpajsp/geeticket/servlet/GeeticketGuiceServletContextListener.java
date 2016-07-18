package com.cb.examples.jpajsp.geeticket.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import com.cb.examples.jpajsp.geeticket.inject.GeeticketModule;
import com.cb.geemvc.DispatcherServlet;
import com.cb.geemvc.config.Configuration;
import com.cb.geemvc.inject.GeemvcModule;
import com.cb.geemvc.inject.InjectorProvider;
import com.cb.geemvc.inject.Injectors;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

@WebListener
public class GeeticketGuiceServletContextListener extends GuiceServletContextListener {
    protected Injector injector;

    @Override
    protected Injector getInjector() {
	try {
	    injector = Guice.createInjector(new GeemvcModule(), new GeeticketModule(), new ServletModule() {
		@Override
		protected void configureServlets() {
		    install(new JpaPersistModule("geeticketPU"));

		    filter("/*").through(PersistFilter.class);

		    Map<String, String> params = new HashMap<String, String>();
		    params.put(Configuration.VIEW_PREFIX_KEY, "/jsp/pages");
		    params.put(Configuration.VIEW_SUFFIX_KEY, ".jsp");
		    params.put(Configuration.SUPPORTED_LOCALES_KEY, "en, de_DE, fr_FR, es_ES, ru_RU:UTF-8, ja_JP:Shift_JIS, zh:UTF-8");
		    params.put(Configuration.DEFAULT_CHARACTER_ENCODING_KEY, "UTF-8");
		    params.put(Configuration.DEFAULT_CONTENT_TYPE_KEY, "text/html");

		    serve("/geeticket/*").with(DispatcherServlet.class, params);
		}
	    });

	} catch (Throwable t) {
	    System.out.println("!! An error occured during initialization of the GeeticketGuiceServletContextListener.");

	    if (t instanceof com.google.inject.CreationException) {
		System.out.println("It seems that Guice was not able to create the injector due to problems with unregistered or incorrectly registered classes. Please check the Guice errors below!");
	    }

	    t.printStackTrace();
	}

	return injector;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
	super.contextInitialized(servletContextEvent);
	
	InjectorProvider ip = new InjectorProvider() {
	    @Override
	    public Injector provide() {
		return injector;
	    }
	};
	
	
	Injectors.set(ip);
	
	servletContextEvent.getServletContext().setAttribute(Configuration.INJECTOR_PROVIDER_KEY, ip);
    }
}
