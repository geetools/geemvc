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

package com.geemvc.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.geemvc.RequestContext;
import com.geemvc.matcher.PathMatcherKey;
import org.junit.Test;

import com.geemvc.mock.controller.TestController11;
import com.geemvc.test.BaseTest;

public class ResolveHandlerByCookiesTest extends BaseTest {
    @Test
    public void testFindcontroller11a() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("group", "one"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookieExists("group=one", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11b() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("group", "two"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookieExists("group=two", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11c() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("group", "one"));
        cookies.add(new Cookie("rememberMe", "1"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11/handler", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"group=one", "rememberMe=^(0|1)"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11e() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("rememberMe", "0"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11/handler", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"rememberMe=^(?iu:0|1)$"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11f() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieExists", ""));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11f", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"cookieExists"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11g() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieExists", ""));
        cookies.add(new Cookie("cookieOneExists", ""));
        cookies.add(new Cookie("cookieTwoExists", ""));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "PUT", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11g", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"cookieOneExists", "cookieTwoExists"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11gg() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieExists", ""));
        cookies.add(new Cookie("cookieOneExists", ""));
        cookies.add(new Cookie("cookieTwoExists", ""));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11gg", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"cookieOneExists", "cookieTwoExists"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11h() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("booleanCookie", "false"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11h", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"booleanCookie!=true"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11i() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("booleanCookieOne", "true"));
        cookies.add(new Cookie("booleanCookieTwo", "false"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11i", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"booleanCookieOne=true", "booleanCookieTwo!=true"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11ii() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("booleanCookieOne", "true"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11ii", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"booleanCookieOne=true"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11j() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("jCookieOne", "false"));
        cookies.add(new Cookie("jCookieTwo", "false"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11j", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"jCookieOne != ^notTr[ue]+", "jCookieTwo!=tru*"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11k() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("kCookieOne", "false"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11k", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"kCookieOne != ^notTr[ue]+$", "kCookieOne!=tru*"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11l() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieOne", "101"));
        cookies.add(new Cookie("cookieTwo", "202"));
        cookies.add(new Cookie("cookieThree", "303"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11l", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"js: cookieOne > 100", "groovy: (cookieTwo as int) > 200", "mvel: cookieThree > 300"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11m() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieOne", "one"));
        cookies.add(new Cookie("cookieTwo", "two"));
        cookies.add(new Cookie("cookieThree", "three"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11m", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"js: cookieOne == 'one'", "groovy: cookieTwo == 'two'", "mvel: cookieThree == 'three'"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11n() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieOne", "not one"));
        cookies.add(new Cookie("cookieTwo", "not two"));
        cookies.add(new Cookie("cookieThree", "not three"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11n", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"js: cookieOne != 'one' && cookieOne != 101 && cookieOne != 11", "groovy: cookieTwo != 'two' && cookieTwo != '202' && cookieTwo != '22'",
                "mvel: cookieThree != 'three' && cookieThree != 303 && cookieThree != 33"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11o() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieOne", "11"));
        cookies.add(new Cookie("cookieTwo", "11"));
        cookies.add(new Cookie("cookieThree", "11"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11o", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"js: cookieOne <= 100 && cookieOne == cookieTwo", "groovy: (cookieTwo as int) <= 100 && cookieTwo == cookieThree", "mvel: cookieThree <= 100 && cookieThree == cookieOne"},
                requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11p() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieOne", "11"));
        cookies.add(new Cookie("cookieTwo", "22"));
        cookies.add(new Cookie("cookieThree", "33"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11p", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"js: cookieOne == 100 || cookieOne == 11", "groovy: (cookieTwo as int) == 200 || (cookieTwo as int) == 22", "mvel: cookieThree == 300 || cookieThree == 33"},
                requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindcontroller11q() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieOne", "handlerQQ"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller11", "GET", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController11.class);
        assertEquals("handler11q", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller11", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedCookiesExists(new String[]{"js: 1 == 0 || (/handler[q]+/igm.test(cookieOne))", "groovy: 1 == 0 || (cookieOne ==~ /(?im)handler[q]+/)", "mvel: 1 == 0 || (cookieOne ~= '(?im)handler[q]+')"},
                requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller11", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }
}
