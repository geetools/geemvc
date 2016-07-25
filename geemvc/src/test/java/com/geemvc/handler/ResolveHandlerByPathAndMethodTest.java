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

import java.util.Map;

import com.geemvc.mock.controller.TestController2;
import com.geemvc.mock.controller.TestController4;
import org.junit.Test;

import com.geemvc.RequestContext;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.mock.controller.TestController3;
import com.geemvc.mock.controller.TestController5;
import com.geemvc.mock.controller.TestController6;
import com.geemvc.mock.controller.TestController7a;
import com.geemvc.mock.controller.TestController7b;
import com.geemvc.mock.controller.TestController8;
import com.geemvc.test.BaseTest;

public class ResolveHandlerByPathAndMethodTest extends BaseTest {
    @Test
    public void testFindController1a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller1");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNull(requestHandler);
    }

    @Test
    public void testFindController1b() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller1/handler1");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNull(requestHandler);
    }

    @Test
    public void testFindController2a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller2/handler1");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController2.class);
        assertEquals("handler1a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller2", controllers.values()));
        assertTrue(mappedPathExists("/handler1", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller2/handler1", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());
    }

    @Test
    public void testFindController2b() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller2/handler1.html");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController2.class);
        assertEquals("handler1b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller2", controllers.values()));
        assertTrue(mappedPathExists("handler1.html", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller2/handler1.html", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());
    }

    @Test
    public void testFindController2c() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller2/handler2");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController2.class);
        assertEquals("handler2", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller2", controllers.values()));
        assertTrue(mappedPathExists("/handler2", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller2/handler2", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());
    }

    @Test
    public void testFindController3a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller3/handler1/a/b");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController3.class);
        assertEquals("handler3a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller3", controllers.values()));
        assertTrue(mappedPathExists("/handler1/*/b", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller3/handler1/*/b", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller3/handler1/[^\\/]*/b$");

    }

    @Test
    public void testFindController3b() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller3/handler1/a/b/c/d");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController3.class);
        assertEquals("handler3b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller3", controllers.values()));
        assertTrue(mappedPathExists("/handler1/**/c/d", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller3/handler1/**/c/d", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller3/handler1/.*/c/d$");

    }

    @Test
    public void testFindController3c() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller3/handler1/a/b/c/d");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController3.class);
        assertEquals("handler3b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller3", controllers.values()));
        assertTrue(mappedPathExists("/handler1/**/c/d", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller3/handler1/**/c/d", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller3/handler1/.*/c/d$");

    }

    @Test
    public void testFindController3d() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller3/handler2/a/b/c/d");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController3.class);
        assertEquals("handler3c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller3", controllers.values()));
        assertTrue(mappedPathExists("/handler2/**/c/d", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller3/handler2/**/c/d", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller3/handler2/.*/c/d$");

    }

    @Test
    public void testFindController3e() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller3/handler1/a/b/c/d/e");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController3.class);
        assertEquals("handler3d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller3", controllers.values()));
        assertTrue(mappedPathExists("/handler1/**", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller3/handler1/**", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller3/handler1/.*$");

    }

    @Test
    public void testFindController3f() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller3/handler2/a/b/c");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNull(requestHandler);
        assertTrue(controllerPathExists("/controller3", controllers.values()));
    }

    @Test
    public void testFindController3g() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller3/handlerA1/a/b/c");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController3.class);
        assertEquals("handler3f", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller3", controllers.values()));
        assertTrue(mappedPathExists("/handlerA*/**", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller3/handlerA*/**", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller3/handlerA[^\\/]*/.*$");

    }

    @Test
    public void testFindController4a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller4/handler1/12345");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController4.class);
        assertEquals("handler4a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller4", controllers.values()));
        assertTrue(mappedPathExists("^/handler1/[0-9]+", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller4/handler1/[0-9]+", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller4/handler1/[0-9]+$");

    }

    @Test
    public void testFindController4b() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller4/handler1/2");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController4.class);
        assertEquals("handler4b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller4", controllers.values()));
        assertTrue(mappedPathExists("^/handler1/[0-9]{1}", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller4/handler1/[0-9]{1}", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller4/handler1/[0-9]{1}$");

    }

    @Test
    public void testFindController4c() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller4/api/v1/handler1");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController4.class);
        assertEquals("handler4c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller4", controllers.values()));
        assertTrue(mappedPathExists("^/api/v[0-9]+/handler1", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller4/api/v[0-9]+/handler1", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller4/api/v[0-9]+/handler1$");

    }

    @Test
    public void testFindController4d() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller4/api/v1/handler1/abc/123");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController4.class);
        assertEquals("handler4d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller4", controllers.values()));
        assertTrue(mappedPathExists("^/api/v[0-9]+/handler1/[a-z]+/[\\d]+", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller4/api/v[0-9]+/handler1/[a-z]+/[\\d]+", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller4/api/v[0-9]+/handler1/[a-z]+/[\\d]+$");

    }

    @Test
    public void testFindController4e() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller4/api/v1/handler1/abc/123/xyz");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController4.class);
        assertEquals("handler4e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller4", controllers.values()));
        assertTrue(mappedPathExists("^/api/v[0-9]+/handler1/[a-z]+/[\\d]+/xyz", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller4/api/v[0-9]+/handler1/[a-z]+/[\\d]+/xyz", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller4/api/v[0-9]+/handler1/[a-z]+/[\\d]+/xyz$");

    }

    @Test
    public void testFindController4f() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller4/api/v1/handler1/abc/123/xy");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController4.class);
        assertEquals("handler4f", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller4", controllers.values()));
        assertTrue(mappedPathExists("^/api/v[0-9]+/handler1/[a-z]+/[\\d]+/xy$", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller4/api/v[0-9]+/handler1/[a-z]+/[\\d]+/xy", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller4/api/v[0-9]+/handler1/[a-z]+/[\\d]+/xy$");

    }

    @Test
    public void testFindController4g() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller4/api/v1/handler2/a/b/c");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController4.class);
        assertEquals("handler4g", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller4", controllers.values()));
        assertTrue(mappedPathExists("^/api/v[0-9]+/handler2/.+", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller4/api/v[0-9]+/handler2/.+", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller4/api/v[0-9]+/handler2/.+$");

    }

    @Test
    public void testFindController4h() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller4/a/b/c");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController4.class);
        assertEquals("handler4h", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller4", controllers.values()));
        assertTrue(mappedPathExists("^.*", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller4/.*", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals(requestHandler.pathMatcher().getRegexPath(), "^/controller4/.*$");

    }

    @Test
    public void testFindController5a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller5/handler1/a/b/c");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController5.class);
        assertEquals("handler5a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("", controllers.values()));
        assertTrue(mappedPathExists("/controller5/handler1/**", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller5/handler1/**", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals("^/controller5/handler1/.*$", requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController5b() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller5/handler2/a/b/c");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController5.class);
        assertEquals("handler5b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("", controllers.values()));
        assertTrue(mappedPathExists("^/controller5/handler2/.*", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller5/handler2/.*", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals("^/controller5/handler2/.*$", requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController5c() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller5/handler2/a/b");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController5.class);
        assertEquals("handler5c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("", controllers.values()));
        assertTrue(mappedPathExists("/controller5/handler2/a/b", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller5/handler2/a/b", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController5d() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/");

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
        assertEquals(TestController5.class, requestHandler.controllerClass());
        assertEquals("handler5d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("", controllers.values()));
        assertTrue(mappedPathExists("/", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController5e() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/catch/all");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController5.class);
        assertEquals("handler5e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("", controllers.values()));
        assertTrue(mappedPathExists("^.*", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/.*", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals("^/.*$", requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController6a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/another/catch/all");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController6.class);
        assertEquals("handler6a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("", controllers.values()));
        assertTrue(mappedPathExists("**", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/**", requestHandler.pathMatcher()));
        assertNotNull(requestHandler.pathMatcher().getRegexPath());
        assertEquals("^/.*$", requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController7a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/shared/path/handler/a");

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
        assertEquals(requestHandler.controllerClass(), TestController7a.class);
        assertEquals("handler", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/shared/path", controllers.values()));
        assertTrue(mappedPathExists("/handler/a", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/shared/path/handler/a", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController7b() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/shared/path/handler/b");

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
        assertEquals(requestHandler.controllerClass(), TestController7b.class);
        assertEquals("handler", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/shared/path", controllers.values()));
        assertTrue(mappedPathExists("/handler/b", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/shared/path/handler/b", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController8a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller8/handler/get-and-post");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController8.class);
        assertEquals("handler8a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller8", controllers.values()));
        assertTrue(mappedPathExists("/handler/get-and-post", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller8/handler/get-and-post", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController8b() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller8/handler", "GET");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController8.class);
        assertEquals("handler8b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller8", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller8/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController8c() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller8/handler", "POST");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController8.class);
        assertEquals("handler8c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller8", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller8/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController8d() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller8/handler", "PUT");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController8.class);
        assertEquals("handler8d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller8", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller8/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController8e() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller8/handler", "DELETE");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController8.class);
        assertEquals("handler8e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller8", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller8/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController8f() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller8/handler", "OPTIONS");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController8.class);
        assertEquals("handler8f", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller8", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller8/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController8g() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller8/handler", "HEAD");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController8.class);
        assertEquals("handler8g", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller8", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller8/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController8h() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller8/handler", "TRACE");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(2, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController8.class);
        assertEquals("handler8h", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller8", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller8/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }
}
