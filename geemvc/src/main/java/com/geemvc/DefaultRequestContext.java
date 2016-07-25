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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.geemvc.handler.RequestHandler;
import com.geemvc.view.GeemvcKey;

public class DefaultRequestContext implements RequestContext {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected ServletContext servletContext;

    protected String acceptHeader = "Accept";
    protected String acceptLanguageHeader = "Accept-Language";
    protected String contentTypeHeader = "Content-Type";

    protected RequestHandler requestHandler;

    protected String requestURI = null;
    protected String servletPath = null;
    protected String contextPath = null;
    protected String pathInfo = null;

    protected boolean isInitialized = false;

    @Override
    public RequestContext build(ServletRequest request, ServletResponse response, ServletContext servletContext) {
        if (isInitialized)
            throw new IllegalStateException("RequestContext can only be initialized once");

        this.request = (HttpServletRequest) request;
        this.response = (HttpServletResponse) response;
        this.servletContext = servletContext;

        this.requestURI = this.request.getRequestURI();
        this.contextPath = this.request.getContextPath();
        this.servletPath = this.request.getServletPath();
        this.pathInfo = this.request.getPathInfo();

        this.isInitialized = true;

        return this;
    }

    @Override
    public RequestContext requestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        return this;
    }

    @Override
    public RequestHandler requestHandler() {
        return requestHandler;
    }

    @Override
    public String getPath() {

        String path = null;

        if (pathInfo == null) {
            path = requestURI;
        } else {
            path = pathInfo;
        }

        // String path = requestURI;
        //
        // if (contextPath != null)
        // path = path.substring(contextPath.length());
        //
        // if (servletPath != null)
        // path = path.substring(servletPath.length());

        return normalizePath(path);
    }

    public Map<String, String[]> getPathParameters() {
        return requestHandler.pathMatcher().parameters(this);
    }

    protected String normalizePath(String path) {
        path = path.trim().replaceAll("\\/+", Str.SLASH);

        if (path.endsWith(Str.SLASH))
            path = path.substring(0, path.length() - 1);

        return path.isEmpty() ? "/" : path;
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return request.getParameterMap();
    }

    @Override
    public Map<String, String[]> getHeaderMap() {
        Enumeration<String> headerNames = request.getHeaderNames();

        Map<String, String[]> headers = new LinkedHashMap<>();

        if (headerNames != null) {
            Map<String, List<String>> normalizedHeaders = new LinkedHashMap<>();

            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                Enumeration<String> headerValues = request.getHeaders(name);

                if (!normalizedHeaders.containsKey(name))
                    normalizedHeaders.put(name, new ArrayList<>());

                if (headerValues != null) {
                    while (headerValues.hasMoreElements()) {
                        String val = (String) headerValues.nextElement();

                        if (val.contains(Str.COMMA)) {
                            String[] spValues = val.split(Str.COMMA);

                            for (String spVal : spValues) {
                                if (acceptHeader.equalsIgnoreCase(name) || contentTypeHeader.equalsIgnoreCase(name)) {
                                    normalizedHeaders.get(name).add(spVal.trim().toLowerCase());
                                } else {
                                    normalizedHeaders.get(name).add(spVal.trim());
                                }
                            }
                        } else {
                            if (acceptHeader.equalsIgnoreCase(name) || contentTypeHeader.equalsIgnoreCase(name)) {
                                normalizedHeaders.get(name).add(val.trim().toLowerCase());
                            } else {
                                normalizedHeaders.get(name).add(val.trim());
                            }
                        }
                    }

                    Set<String> keys = normalizedHeaders.keySet();

                    for (String key : keys) {
                        List<String> values = normalizedHeaders.get(key);
                        headers.put(key, values.toArray(new String[values.size()]));
                    }
                } else {
                    headers.put(name, null);
                }
            }
        }

        return headers;
    }

    @Override
    public Map<String, String[]> getCookieMap() {
        Cookie[] cookies = request.getCookies();

        Map<String, String[]> cookieMap = new HashMap<>();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), new String[]{cookie.getValue()});
            }
        }

        return cookieMap;
    }

    @Override
    public Collection<String> getHeaderNames() {
        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            List<String> names = new ArrayList<>();

            while (headerNames.hasMoreElements()) {
                names.add((String) headerNames.nextElement());
            }

            return names;
        }

        return null;
    }

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        Enumeration<String> headerValues = request.getHeaders(name);

        if (headerValues != null) {
            List<String> values = new ArrayList<>();

            while (headerValues.hasMoreElements()) {
                values.add((String) headerValues.nextElement());
            }

            return values;
        }

        return null;
    }

    @Override
    public String contentType() {
        return request.getContentType();
    }

    @Override
    public Collection<String> accepts() {
        Map<String, String[]> headers = getHeaderMap();

        Collection<String> acceptContentTypes = new ArrayList<>();

        Set<String> headerNames = headers.keySet();

        for (String name : headerNames) {
            if (acceptHeader.equalsIgnoreCase(name)) {
                String[] accepts = headers.get(name);
                for (String accept : accepts) {
                    int pos = accept.indexOf(Char.SEMI_COLON);

                    if (pos != -1)
                        acceptContentTypes.add(accept.substring(0, pos).trim());
                    else
                        acceptContentTypes.add(accept);
                }
            }
        }

        if (acceptContentTypes.isEmpty()) {
            acceptContentTypes.add("text/html");
            acceptContentTypes.add("application/json");
            acceptContentTypes.add("text/*");
            acceptContentTypes.add("application/*");
            acceptContentTypes.add("*/*");
        }

        return acceptContentTypes;
    }

    @Override
    public Collection<String> getAttributeNames() {
        HttpSession session = request.getSession();

        if (session != null) {
            HashSet<String> names = new HashSet<>();

            Enumeration<String> sessionNames = session.getAttributeNames();

            while (sessionNames.hasMoreElements()) {
                names.add((String) sessionNames.nextElement());
            }

            return names;
        }

        return null;
    }

    @Override
    public Object getAttribute(String name) {
        HttpSession session = request.getSession();

        if (session != null) {
            return session.getAttribute(name);
        }

        return null;
    }

    @Override
    public Map<String, String> getCookies() {
        Cookie[] cookies = request.getCookies();

        Map<String, String> cookieMap = new LinkedHashMap<>();

        for (Cookie cookie : cookies) {
            cookieMap.put(cookie.getName(), cookie.getValue());
        }

        return cookieMap;
    }

    @Override
    public ServletRequest getRequest() {
        return request;
    }

    @Override
    public ServletResponse getResponse() {
        return response;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public HttpSession getSession() {
        return request.getSession();
    }

    @Override
    public HttpSession getSession(boolean create) {
        return request.getSession(create);
    }

    @Override
    public Enumeration<Locale> getLocales() {
        String acceptLangHeader = request.getHeader(acceptLanguageHeader);

        if (!Str.isEmpty(acceptLangHeader)) {
            return request.getLocales();
        }

        return null;
    }

    @Override
    public RequestContext currentLocale(Locale locale) {
        request.setAttribute(GeemvcKey.CURRENT_LOCALE, locale);
        return this;
    }

    @Override
    public Locale currentLocale() {
        return (Locale) request.getAttribute(GeemvcKey.CURRENT_LOCALE);
    }
}
