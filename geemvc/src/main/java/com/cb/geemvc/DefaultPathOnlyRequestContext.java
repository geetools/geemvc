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

package com.cb.geemvc;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import com.cb.geemvc.handler.RequestHandler;

public class DefaultPathOnlyRequestContext implements PathOnlyRequestContext {
    protected String requestURI = null;
    protected String servletPath = null;
    protected String method = null;

    protected RequestHandler requestHandler;

    private boolean isInitialized = false;

    @Override
    public RequestContext build(ServletRequest request, ServletResponse response, ServletContext servletContext) {
        throw new IllegalStateException("This builder method is only valid for the standard RequestContext");
    }

    @Override
    public PathOnlyRequestContext build(String requestURI) {
        if (isInitialized)
            throw new IllegalStateException("PathOnlyRequestContext can only be initialized once");

        this.requestURI = requestURI;

        this.isInitialized = true;

        return this;
    }

    @Override
    public PathOnlyRequestContext build(String requestURI, String method) {
        if (isInitialized)
            throw new IllegalStateException("PathOnlyRequestContext can only be initialized once");

        this.requestURI = requestURI;
        this.method = method;

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
        return null;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public Map<String, String[]> getHeaderMap() {
        return null;
    }

    @Override
    public Map<String, String[]> getCookieMap() {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public String contentType() {
        return null;
    }

    @Override
    public Collection<String> accepts() {
        return null;
    }

    @Override
    public Collection<String> getAttributeNames() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Map<String, String> getCookies() {
        return null;
    }

    @Override
    public ServletRequest getRequest() {
        return null;
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
        return null;
    }

    @Override
    public HttpSession getSession(boolean create) {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public RequestContext currentLocale(Locale locale) {
        return null;
    }

    @Override
    public Locale currentLocale() {
        return null;
    }
}
