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
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");

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
            String requestInfo = getRequestInfo(request, response);
            System.out.println(requestInfo);
            
            throw e;
        } catch (Exception e) {
            String requestInfo = getRequestInfo(request, response);
            System.out.println(requestInfo);

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

    protected String getRequestInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        HttpSession sess = httpRequest.getSession(false);

        Enumeration<String> headerNames = httpRequest.getHeaderNames();

        StringBuilder headers = new StringBuilder(Char.SQUARE_BRACKET_OPEN);

        if (headerNames != null) {
            int x = 0;
            while (headerNames.hasMoreElements()) {
                if (x > 0)
                    headers.append(Char.COMMA).append(Char.SPACE);

                String headerName = headerNames.nextElement();
                headers.append(headerName).append(Char.EQUALS).append(httpRequest.getHeader(headerName));

                x++;
            }
        }

        headers.append(Char.SQUARE_BRACKET_CLOSE);

        StringBuilder info = new StringBuilder();
        info.append(Char.NEWLINE).append("--------------------------------------------------------------------").append(Char.NEWLINE);
        info.append("Exception in DispatcherServlet on: ").append(dateFormat.format(new Date())).append(Char.NEWLINE);
        info.append("Request URL: ").append(httpRequest.getRequestURL()).append(Char.NEWLINE);
        info.append("Request URI: ").append(httpRequest.getRequestURI()).append(Char.NEWLINE);
        info.append("Request QueryString: ").append(httpRequest.getQueryString()).append(Char.NEWLINE);
        info.append("Method: ").append(httpRequest.getMethod()).append(Char.NEWLINE);
        info.append("Session Id: ").append(sess == null ? null : sess.getId()).append(Char.NEWLINE);
        info.append("Thread: ").append(Thread.currentThread().getName()).append(Char.NEWLINE);
        info.append("Headers: ").append(headers.toString()).append(Char.NEWLINE);
        info.append("Remote Addr: ").append(httpRequest.getRemoteAddr()).append(Char.NEWLINE);

        Collection<String> respHeaderNames = httpResponse.getHeaderNames();

        StringBuilder respHeaders = new StringBuilder(Char.SQUARE_BRACKET_OPEN);

        if (respHeaderNames != null) {
            int x = 0;
            for (String headerName : respHeaderNames) {
                if (x > 0)
                    headers.append(Char.COMMA).append(Char.SPACE);

                respHeaders.append(headerName).append(Char.EQUALS).append(httpResponse.getHeader(headerName));

                x++;
            }
        }

        respHeaders.append(Char.SQUARE_BRACKET_CLOSE);

        info.append("Response Locale: ").append(httpResponse.getLocale()).append(Char.NEWLINE);
        info.append("Response Encoding: ").append(httpResponse.getCharacterEncoding()).append(Char.NEWLINE);
        info.append("Response Content-Type: ").append(httpResponse.getContentType()).append(Char.NEWLINE);
        info.append("Response Status: ").append(httpResponse.getStatus()).append(Char.NEWLINE);
        info.append("Response Commited: ").append(httpResponse.isCommitted()).append(Char.NEWLINE);
        info.append("Response Headers: ").append(respHeaders).append(Char.NEWLINE);

        info.append("--------------------------------------------------------------------").append(Char.NEWLINE);

        return info.toString();
    }
}
