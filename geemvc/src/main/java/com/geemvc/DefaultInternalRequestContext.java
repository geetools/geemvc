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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.geemvc.handler.RequestHandler;

public class DefaultInternalRequestContext extends DefaultRequestContext implements InternalRequestContext {
    protected String requestURI = null;
    protected String servletPath = null;
    protected String method = null;
    protected Map<String, String[]> parameters = null;

    @Override
    public RequestContext build(ServletRequest request, ServletResponse response, ServletContext servletContext) {
        throw new IllegalStateException("This builder method is only valid for the standard RequestContext");
    }

    @Override
    public InternalRequestContext build(String requestURI) {
        if (isInitialized)
            throw new IllegalStateException("PathOnlyRequestContext can only be initialized once");

        this.requestURI = requestURI;

        this.isInitialized = true;

        return this;
    }

    @Override
    public InternalRequestContext build(String requestURI, String method) {
        if (isInitialized)
            throw new IllegalStateException("PathOnlyRequestContext can only be initialized once");

        this.requestURI = requestURI;
        this.method = method;

        this.isInitialized = true;

        return this;
    }

    @Override
    public InternalRequestContext build(String requestURI, String method, Map<String, String[]> parameters) {
        if (isInitialized)
            throw new IllegalStateException("PathOnlyRequestContext can only be initialized once");

        this.requestURI = requestURI;
        this.method = method;
        this.parameters = parameters;

        this.isInitialized = true;

        return this;
    }

    @Override
    public InternalRequestContext build(String requestURI, String method, Map<String, String[]> parameters, ServletRequest request, ServletResponse response, ServletContext servletContext) {
        if (isInitialized)
            throw new IllegalStateException("PathOnlyRequestContext can only be initialized once");

        this.requestURI = requestURI;
        this.method = method;
        this.parameters = parameters;
        this.request = (HttpServletRequest) request;
        this.response = (HttpServletResponse) response;
        this.servletContext = servletContext;

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
        return requestURI;
    }

    public Map<String, String[]> getPathParameters() {
        return requestHandler == null || requestHandler.pathMatcher() == null ? null : requestHandler.pathMatcher().parameters(requestURI);
    }

    @Override
    public String getMethod() {
        return method == null ? super.getMethod() : method;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    @Override
    public Map<String, String[]> getHeaderMap() {
        return super.getHeaderMap();
    }

    @Override
    public Map<String, String[]> getCookieMap() {
        return super.getCookieMap();
    }

    @Override
    public Collection<String> getHeaderNames() {
        return super.getHeaderNames();
    }

    @Override
    public String getHeader(String name) {
        return super.getHeader(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return super.getHeaders(name);
    }

    @Override
    public String contentType() {
        return super.contentType();
    }

    @Override
    public Collection<String> accepts() {
        return super.accepts();
    }

    @Override
    public Collection<String> getAttributeNames() {
        return super.getAttributeNames();
    }

    @Override
    public Object getAttribute(String name) {
        return super.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        super.setAttribute(name, value);
    }

    @Override
    public Map<String, String> getCookies() {
        return super.getCookies();
    }

    @Override
    public ServletRequest getRequest() {
        return super.getRequest();
    }

    @Override
    public ServletResponse getResponse() {
        return null;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return super.getSession();
    }

    @Override
    public HttpSession getSession(boolean create) {
        return super.getSession();
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return super.getLocales();
    }

    @Override
    public RequestContext currentLocale(Locale locale) {
        return super.currentLocale(locale);
    }

    @Override
    public Locale currentLocale() {
        return super.currentLocale();
    }
}
