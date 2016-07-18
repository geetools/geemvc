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

package com.cb.geemvc.mock.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

public class MockRequest implements HttpServletRequest {
    private String contextPath = null;
    private String servletPath = null;
    private String requestURI = null;
    private String method = null;
    private Map<String, String[]> params = null;
    private Map<String, String[]> headers = null;
    private Cookie[] cookies = null;
    private static final ThreadLocal<HttpSession> sessionLocal = new ThreadLocal<HttpSession>();

    public MockRequest(String contextPath, String servletPath, String requestURI) {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.requestURI = requestURI;
        this.method = "GET";
        this.params = new HashMap<>();
        this.headers = new HashMap<>();
    }

    public MockRequest(String contextPath, String servletPath, String requestURI, Cookie[] cookies) {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.requestURI = requestURI;
        this.method = "GET";
        this.params = new HashMap<>();
        this.headers = new HashMap<>();
        this.cookies = cookies;
    }

    public MockRequest(String contextPath, String servletPath, String requestURI, Map<String, String[]> params) {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.requestURI = requestURI;
        this.method = "GET";
        this.params = params;
        this.headers = new HashMap<>();
    }

    public MockRequest(String contextPath, String servletPath, String requestURI, String method) {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.requestURI = requestURI;
        this.method = method;
        this.params = new HashMap<>();
        this.headers = new HashMap<>();
    }

    public MockRequest(String contextPath, String servletPath, String requestURI, String method, Cookie[] cookies) {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.requestURI = requestURI;
        this.method = method;
        this.params = new HashMap<>();
        this.headers = new HashMap<>();
        this.cookies = cookies;
    }

    public MockRequest(String contextPath, String servletPath, String requestURI, String method, Map<String, String[]> params) {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.requestURI = requestURI;
        this.method = method;
        this.params = params;
        this.headers = new HashMap<>();
    }

    public MockRequest(String contextPath, String servletPath, String requestURI, Map<String, String[]> params, Map<String, String[]> headers) {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.requestURI = requestURI;
        this.method = "GET";
        this.params = params;
        this.headers = headers;
    }

    public MockRequest(String contextPath, String servletPath, String requestURI, String method, Map<String, String[]> params, Map<String, String[]> headers) {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.requestURI = requestURI;
        this.method = method;
        this.params = params;
        this.headers = headers;
    }

    public MockRequest(String contextPath, String servletPath, String requestURI, String method, Map<String, String[]> params, Map<String, String[]> headers, Cookie[] cookies) {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.requestURI = requestURI;
        this.method = method;
        this.params = params;
        this.headers = headers;
        this.cookies = cookies;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {

        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {

        return 0;
    }

    @Override
    public long getContentLengthLong() {

        return 0;
    }

    @Override
    public String getContentType() {
        Enumeration<String> names = getHeaderNames();

        if (names != null) {
            while (names.hasMoreElements()) {
                String name = names.nextElement();

                if ("content-type".equalsIgnoreCase(name.toLowerCase().trim())) {
                    return getHeader(name);
                }
            }
        }

        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        return null;
    }

    @Override
    public String getParameter(String name) {
        String[] value = this.params.get(name);

        return value == null ? null : value[0];
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(this.params.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {

        return this.params.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return this.params;
    }

    @Override
    public String getProtocol() {

        return null;
    }

    @Override
    public String getScheme() {

        return null;
    }

    @Override
    public String getServerName() {

        return null;
    }

    @Override
    public int getServerPort() {

        return 8080;
    }

    @Override
    public BufferedReader getReader() throws IOException {

        return null;
    }

    @Override
    public String getRemoteAddr() {

        return null;
    }

    @Override
    public String getRemoteHost() {

        return null;
    }

    @Override
    public void setAttribute(String name, Object o) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public Locale getLocale() {

        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {

        return null;
    }

    @Override
    public boolean isSecure() {

        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {

        return null;
    }

    @Override
    public String getRealPath(String path) {

        return null;
    }

    @Override
    public int getRemotePort() {

        return 0;
    }

    @Override
    public String getLocalName() {

        return null;
    }

    @Override
    public String getLocalAddr() {

        return null;
    }

    @Override
    public int getLocalPort() {

        return 0;
    }

    @Override
    public ServletContext getServletContext() {

        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {

        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {

        return null;
    }

    @Override
    public boolean isAsyncStarted() {

        return false;
    }

    @Override
    public boolean isAsyncSupported() {

        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {

        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {

        return null;
    }

    @Override
    public String getAuthType() {

        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return cookies;
    }

    @Override
    public long getDateHeader(String name) {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        String[] values = this.headers.get(name);
        return values == null || values.length == 0 ? null : values[0];
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        String[] values = this.headers.get(name);
        return values == null ? null : Collections.enumeration(Arrays.asList(values));
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return this.headers == null ? null : Collections.enumeration(this.headers.keySet());
    }

    @Override
    public int getIntHeader(String name) {
        String[] values = this.headers.get(name);
        return values == null || values.length == 0 ? 0 : Integer.parseInt(values[0]);
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return requestURI.replace(getContextPath(), "").replace(getServletPath(), "");
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public HttpSession getSession(boolean create) {
        HttpSession session = sessionLocal.get();

        if (session == null && create) {
            sessionLocal.set(new MockSession());
            session = sessionLocal.get();
        }

        return session;
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public String changeSessionId() {

        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {

        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {

        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {

        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {

        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {

        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {

        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {

        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {

        return null;
    }
}
