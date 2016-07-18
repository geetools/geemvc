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

package com.cb.geemvc.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.junit.Test;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.matcher.PathMatcherKey;
import com.cb.geemvc.mock.controller.TestController13;
import com.cb.geemvc.test.BaseTest;

public class ResolveHandlerTest extends BaseTest {
    @Test
    public void testFindcontroller13a() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"update"});

        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("group", "one"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13", "GET", params, headers, cookies);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(requestHandler.resolvedParameters());
        assertNotNull(requestHandler.resolvedCookies());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParameterExists("cmd=update", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/json", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookieExists("group=one", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13b() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"update"});

        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/xml"});

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("group", "one"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13", "GET", params, headers, cookies);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParameterExists("cmd=update", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/xml", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookieExists("group=one", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13c() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"update"});

        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("group", "two"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13", "GET", params, headers, cookies);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParameterExists("cmd=update", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/json", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookieExists("group=two", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13d() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"delete"});

        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("group", "one"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13", "GET", params, headers, cookies);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParameterExists("cmd=delete", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/json", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookieExists("group=one", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13e() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"update"});

        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("group", "one"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13", "PUT", params, headers, cookies);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParameterExists("cmd=update", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/json", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookieExists("group=one", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13f() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("group", "one"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13", "GET", (Map<String, String[]>) null, headers, cookies);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13f", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/json", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookieExists("group=one", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13g() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13", "GET", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13g", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/json", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13h() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13", "GET");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13h", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13i() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13", "POST");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13i", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13j() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13j", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13k() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13/specific/path");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13kA", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13/specific/path", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13/specific/path", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13l() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"update"});

        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13/same/path", params, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13lB", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13/same/path", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13/same/path", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13m() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"update"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13/same/path2", params);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13mB", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13/same/path2", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13/same/path2", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller13n() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"test123"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller13/handler13/same/path3", params);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController13.class);
        assertEquals("handler13nA", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller13", controllers.values()));
        assertTrue(mappedPathExists("/handler13/same/path3", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller13/handler13/same/path3", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }
}
