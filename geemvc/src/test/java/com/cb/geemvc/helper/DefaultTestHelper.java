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

package com.cb.geemvc.helper;

import java.util.Map;

import javax.servlet.http.Cookie;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.mock.servlet.MockRequest;
import com.cb.geemvc.mock.servlet.MockResponse;
import com.cb.geemvc.mock.servlet.MockServletContext;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultTestHelper implements TestHelper {
    @Inject
    private Injector injector;

    @Override
    public RequestContext newRequestContext(String contextPath, String servletPath, String requestURI) {
        return injector.getInstance(RequestContext.class).build(new MockRequest(contextPath, servletPath, requestURI), new MockResponse(), new MockServletContext());
    }

    @Override
    public RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, Cookie[] cookies) {
        return injector.getInstance(RequestContext.class).build(new MockRequest(contextPath, servletPath, requestURI, cookies), new MockResponse(), new MockServletContext());
    }

    @Override
    public RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, Map<String, String[]> params) {
        return injector.getInstance(RequestContext.class).build(new MockRequest(contextPath, servletPath, requestURI, params), new MockResponse(), new MockServletContext());
    }

    @Override
    public RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, String method) {
        return injector.getInstance(RequestContext.class).build(new MockRequest(contextPath, servletPath, requestURI, method), new MockResponse(), new MockServletContext());
    }

    @Override
    public RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, String method, Cookie[] cookies) {
        return injector.getInstance(RequestContext.class).build(new MockRequest(contextPath, servletPath, requestURI, method, cookies), new MockResponse(), new MockServletContext());
    }

    @Override
    public RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, String method, Map<String, String[]> params) {
        return injector.getInstance(RequestContext.class).build(new MockRequest(contextPath, servletPath, requestURI, method, params), new MockResponse(), new MockServletContext());
    }

    @Override
    public RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, Map<String, String[]> params, Map<String, String[]> headers) {
        return injector.getInstance(RequestContext.class).build(new MockRequest(contextPath, servletPath, requestURI, params, headers), new MockResponse(), new MockServletContext());
    }

    @Override
    public RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, String method, Map<String, String[]> params, Map<String, String[]> headers) {
        return injector.getInstance(RequestContext.class).build(new MockRequest(contextPath, servletPath, requestURI, method, params, headers), new MockResponse(), new MockServletContext());
    }

    @Override
    public RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, String method, Map<String, String[]> params, Map<String, String[]> headers, Cookie[] cookies) {
        return injector.getInstance(RequestContext.class).build(new MockRequest(contextPath, servletPath, requestURI, method, params, headers, cookies), new MockResponse(), new MockServletContext());
    }
}
