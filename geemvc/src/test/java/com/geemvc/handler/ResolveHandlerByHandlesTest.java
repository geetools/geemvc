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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.junit.Test;

import com.geemvc.RequestContext;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.mock.controller.TestController12;
import com.geemvc.test.BaseTest;

public class ResolveHandlerByHandlesTest extends BaseTest {
    @Test
    public void testFindController12a() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieOne", "handlerA"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller12", "GET", cookies);

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
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController12.class);
        assertEquals("handler12a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller12", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHandlesExists("js: 1 == 0 || ( /handler[a]+/igm.test( Java.from( req.cookies ).filter( function(c) c.name == 'cookieOne' )[0].value ) )", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller12", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController12b() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieOne", "handlerB"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller12", "GET", cookies);

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
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController12.class);
        assertEquals("handler12b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller12", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHandlesExists("groovy: 1 == 0 || ( req.cookies.findAll({ c -> c.name == 'cookieOne' })[0].value ==~ /(?im)handler[b]+/ )", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller12", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController12c() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("cookieOne", "handlerC"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller12", "GET", cookies);

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
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController12.class);
        assertEquals("handler12c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller12", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHandlesExists("mvel: 1 == 0 || (($ in Arrays.asList(req.cookies) if $.name == 'cookieOne')[0].value ~= '(?im)handler[c]+')", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller12", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController12d() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramOne", new String[] { "handlerD" });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller12", "GET", params);

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
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController12.class);
        assertEquals("handler12d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller12", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHandlesExists("js: 1 == 0 || (/handler[d]+/igm.test(req.parameterMap['paramOne'][0]))", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller12", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController12e() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramOne", new String[] { "handlerE" });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller12", "GET", params);

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
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController12.class);
        assertEquals("handler12e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller12", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHandlesExists("groovy: 1 == 0 || (req.parameterMap['paramOne'][0] ==~ /(?im)handler[e]+/)", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller12", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController12f() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramOne", new String[] { "handlerF" });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller12", "GET", params);

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
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController12.class);
        assertEquals("handler12f", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller12", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHandlesExists("mvel: 1 == 0 || (req.parameterMap['paramOne'][0] ~= '(?im)handler[f]+')", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller12", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController12g() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerOne", new String[] { "handlerG" });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller12", "GET", (Map<String, String[]>) null, headers);

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
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController12.class);
        assertEquals("handler12g", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller12", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHandlesExists("js: 1 == 0 || (/handler[g]+/igm.test(req.getHeader('headerOne')))", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller12", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController12h() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerOne", new String[] { "handlerH" });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller12", "GET", (Map<String, String[]>) null, headers);

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
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController12.class);
        assertEquals("handler12h", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller12", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHandlesExists("groovy: 1 == 0 || (req.getHeader('headerOne') ==~ /(?im)handler[h]+/)", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller12", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController12i() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("headerOne", new String[] { "handlerI" });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller12", "GET", (Map<String, String[]>) null, headers);

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
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController12.class);
        assertEquals("handler12i", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller12", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHandlesExists("mvel: 1 == 0 || (req.getHeader('headerOne') ~= '(?im)handler[i]+')", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller12", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

}
