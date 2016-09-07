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

package com.geemvc;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.geemvc.config.Configuration;
import com.geemvc.config.Configurations;
import com.geemvc.converter.adapter.DateConverterAdapter;
import com.geemvc.inject.DefaultInjectorProvider;
import com.geemvc.inject.InjectorProvider;
import com.geemvc.inject.Injectors;
import com.geemvc.matcher.PathMatcher;
import com.geemvc.reflect.ReflectionsStash;
import com.geemvc.reflect.ReflectionsWrapper;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import jodd.typeconverter.TypeConverterManager;

@Singleton
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 6824931404770992086L;

    protected InjectorProvider ínjectorProvider;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Configuration configuration = Configurations.builder().build(config);
        config.getServletContext().setAttribute(Configuration.class.getName(), configuration);

        // Register the Jodd type converter.
        TypeConverterManager.register(Date.class, new DateConverterAdapter());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ServletConfig servletConfig = getServletConfig();

            // Get built configuration object from ServletContext and add it to the current ThreadLocal context.
            Configurations.copyFrom(servletConfig.getServletContext());

            ThreadStash.prepare(request);

            // Add request objects to thread local stash for the rare case where these cannot be injected.
            ThreadStash.put(ServletConfig.class, servletConfig);
            ThreadStash.put(ServletRequest.class, request);
            ThreadStash.put(ServletResponse.class, response);

            // AsyncContext asyncContext = request.startAsync(request,
            // response);
            // asyncContext.setTimeout(0);
            // Dispatcher dispatcher = new Dispatcher(asyncContext);
            // request.getInputStream().setReadListener(dispatcher);
            // response.getOutputStream().setWriteListener(dispatcher);
            Injector injector = injector(servletConfig.getServletContext());

            RequestContext requestCtx = injector.getInstance(RequestContext.class).build(request, response, getServletContext());

            injector.getInstance(ReflectionsWrapper.class).configure();

            // Executor executor = (Executor)
            // request.getServletContext().getAttribute("executor");
            // executor.execute(injector.getInstance(RequestProcessor.class).build(asyncContext,
            // requestCtx));

            Set<String> excudePathMappings = Configurations.get().excludePathMappinig();
            if (excudePathMappings != null && !excudePathMappings.isEmpty() && ignore(request.getRequestURI(), excudePathMappings))
                return;

            RequestRunner requestRunner = injector.getInstance(RequestRunner.class);
            requestRunner.process(requestCtx);
        } catch (IOException | ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            Injectors.clear();
            ReflectionsStash.clear();
            ThreadStash.cleanup();
        }
    }

    protected boolean ignore(String requestURI, Set<String> excudePathMappings) {
        Injector injector = Injectors.provide();

        for (String excludePath : excudePathMappings) {
            PathMatcher pathMatcher = injector.getInstance(PathMatcher.class).build(excludePath);

            if (pathMatcher.matches(injector.getInstance(InternalRequestContext.class).build(requestURI), null)) {
                return true;
            }
        }

        return false;
    }

    protected Injector injector(ServletContext servletCtx) {
        if (ínjectorProvider == null) {

            // First we check to see if an injector provider "instance" has
            // already been added to the servlet-context.
            ínjectorProvider = (InjectorProvider) servletCtx.getAttribute(Configuration.INJECTOR_PROVIDER_KEY);

            // If not then we check to see if a custom provider class has been
            // configured.
            if (ínjectorProvider == null) {
                String configuredInjectorProvider = getServletConfig().getInitParameter(Configuration.INJECTOR_PROVIDER_KEY);

                try {
                    if (!Str.isEmpty(configuredInjectorProvider)) {
                        ínjectorProvider = (InjectorProvider) Class.forName(configuredInjectorProvider).newInstance();
                    } else {
                        // If still no provider has been resolved, simply use
                        // the default.
                        ínjectorProvider = new DefaultInjectorProvider();
                    }
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        // Add injector-provider to the threadLocal variable.
        Injectors.set(ínjectorProvider);

        return ínjectorProvider.provide();
    }
}
