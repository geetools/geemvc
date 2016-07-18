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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.matcher.PathMatcherKey;
import com.cb.geemvc.mock.controller.TestController10;
import com.cb.geemvc.test.BaseTest;

public class ResolveHandlerByHeadersTest extends BaseTest {
    @Test
    public void testFindController10a() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/json", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10b() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/xml"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/xml", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10c() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});
        headers.put("version", new String[]{"1"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10/handler", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"Accept=application/json", "version=^\\d+"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10d() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json", "application/xml", "text/plain", "image/jpeg"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10/handler", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"Accept=application/json", "Accept=application/xml", "Accept=^text\\/.*", "Accept=image/*"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10e() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("orHeader", new String[]{"two"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10/handler", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"orHeader=^(?iu:ONE|TWO|THREE|FOUR)$"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10f() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerExists", null);

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10f", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"headerExists"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10g() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerExists", null);
        headers.put("headerOneExists", null);
        headers.put("headerTwoExists", null);

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "PUT", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10g", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"headerOneExists", "headerTwoExists"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10gg() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerExists", null);
        headers.put("headerOneExists", null);
        headers.put("headerTwoExists", null);

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10gg", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"headerOneExists", "headerTwoExists"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10h() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("booleanHeader", new String[]{"false"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10h", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"booleanHeader!=true"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10i() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("booleanHeaderOne", new String[]{"true"});
        headers.put("booleanHeaderTwo", new String[]{"false"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10i", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"booleanHeaderOne=true", "booleanHeaderTwo!=true"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10ii() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("booleanHeaderOne", new String[]{"true"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10ii", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"booleanHeaderOne=true"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10j() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("jHeaderOne", new String[]{"false"});
        headers.put("jHeaderTwo", new String[]{"false"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10j", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"jHeaderOne != ^notTr[ue]+", "jHeaderTwo!=tru*"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10k() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("kHeaderOne", new String[]{"false"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10k", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"kHeaderOne != ^notTr[ue]+$", "kHeaderOne!=tru*"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10l() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerOne", new String[]{"101"});
        headers.put("headerTwo", new String[]{"202"});
        headers.put("headerThree", new String[]{"303"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10l", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"js: headerOne > 100", "groovy: (headerTwo as int) > 200", "mvel: headerThree > 300"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10m() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerOne", new String[]{"one"});
        headers.put("headerTwo", new String[]{"two"});
        headers.put("headerThree", new String[]{"three"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10m", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"js: headerOne == 'one'", "groovy: headerTwo == 'two'", "mvel: headerThree == 'three'"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10n() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerOne", new String[]{"not one"});
        headers.put("headerTwo", new String[]{"not two"});
        headers.put("headerThree", new String[]{"not three"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10n", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"js: headerOne != 'one' && headerOne != 101 && headerOne != 11", "groovy: headerTwo != 'two' && headerTwo != '202' && headerTwo != '22'",
                "mvel: headerThree != 'three' && headerThree != 303 && headerThree != 33"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10o() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerOne", new String[]{"11"});
        headers.put("headerTwo", new String[]{"11"});
        headers.put("headerThree", new String[]{"11"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10o", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"js: headerOne <= 100 && headerOne == headerTwo", "groovy: (headerTwo as int) <= 100 && headerTwo == headerThree", "mvel: headerThree <= 100 && headerThree == headerOne"},
                requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10p() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerOne", new String[]{"11"});
        headers.put("headerTwo", new String[]{"22"});
        headers.put("headerThree", new String[]{"33"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10p", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"js: headerOne == 100 || headerOne == 11", "groovy: (headerTwo as int) == 200 || (headerTwo as int) == 22", "mvel: headerThree == 300 || headerThree == 33"},
                requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController10q() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerOne", new String[]{"handlerQQ"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10", "GET", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController10.class);
        assertEquals("handler10q", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeadersExists(new String[]{"js: 1 == 0 || (/handler[q]+/igm.test(headerOne))", "groovy: 1 == 0 || (headerOne ==~ /(?im)handler[q]+/)", "mvel: 1 == 0 || (headerOne ~= '(?im)handler[q]+')"},
                requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller10", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }
}
