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

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.geemvc.RequestContext;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.mock.controller.TestControllerJSR311;
import com.geemvc.test.BaseTest;

public class ResolveHandlerByJSR311Test extends BaseTest {
    @Test
    public void testFindControllerJSR311a() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("accept", new String[] { MediaType.APPLICATION_JSON });
        headers.put("content-type", new String[] { MediaType.APPLICATION_JSON });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/jsr311-test/12345", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(4, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestControllerJSR311.class);
        assertEquals("handlerJSR311a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/jsr311-test", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestMapping(requestHandler.handlerMethod())));
        assertEquals("application/json", requestHandlers.consumes(requestHandler.handlerRequestMapping(), reqCtx));
        assertEquals("application/json", requestHandlers.produces(requestHandler.handlerRequestMapping(), reqCtx));

        assertTrue(mappedPathExists("/jsr311-test/{id}", requestHandler.pathMatcher()));
        assertEquals("^/jsr311-test/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
    }

    @Test
    public void testFindControllerJSR311b() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[] { MediaType.APPLICATION_XML });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/jsr311-test/12345", "DELETE", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(4, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestControllerJSR311.class);
        assertEquals("handlerJSR311b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/jsr311-test", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestMapping(requestHandler.handlerMethod())));
        assertEquals("application/xml", requestHandlers.produces(requestHandler.handlerRequestMapping(), reqCtx));

        assertTrue(mappedPathExists("/jsr311-test/{id}", requestHandler.pathMatcher()));
        assertEquals("^/jsr311-test/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
    }

    @Test
    public void testFindControllerJSR311c() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Content-Type", new String[] { MediaType.APPLICATION_JSON });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/jsr311-test/handlerC/12345", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(4, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestControllerJSR311.class);
        assertEquals("handlerJSR311c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/jsr311-test", controllers.values()));
        assertTrue(mappedPathExists("/handlerC/{id}", requestHandler.handlerRequestMapping()));
        assertEquals("application/json", requestHandlers.consumes(requestHandler.handlerRequestMapping(), reqCtx));

        assertTrue(mappedPathExists("/jsr311-test/handlerC/{id}", requestHandler.pathMatcher()));
    }

    @Test
    public void testFindControllerJSR311d() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Content-Type", new String[] { MediaType.APPLICATION_XML });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/jsr311-test/handlerC/12345", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(4, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestControllerJSR311.class);
        assertEquals("handlerJSR311d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/jsr311-test", controllers.values()));
        assertTrue(mappedPathExists("/handlerC/{id}", requestHandler.handlerRequestMapping()));
        assertEquals("application/xml", requestHandlers.consumes(requestHandler.handlerRequestMapping(), reqCtx));

        assertTrue(mappedPathExists("/jsr311-test/handlerC/{id}", requestHandler.pathMatcher()));
    }

    @Test
    public void testFindControllerJSR311e() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[] { MediaType.APPLICATION_JSON });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/jsr311-test/handlerE/12345", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(4, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestControllerJSR311.class);
        assertEquals("handlerJSR311e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/jsr311-test", controllers.values()));
        assertTrue(mappedPathExists("/handlerE/{id}", requestHandler.handlerRequestMapping()));
        assertEquals("application/json", requestHandlers.produces(requestHandler.handlerRequestMapping(), reqCtx));
        assertTrue(mappedPathExists("/jsr311-test/handlerE/{id}", requestHandler.pathMatcher()));
    }

    @Test
    public void testFindControllerJSR311f() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[] { MediaType.APPLICATION_XML });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/jsr311-test/handlerE/12345", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(4, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestControllerJSR311.class);
        assertEquals("handlerJSR311f", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/jsr311-test", controllers.values()));
        assertTrue(mappedPathExists("/handlerE/{id}", requestHandler.handlerRequestMapping()));
        assertEquals("application/xml", requestHandlers.produces(requestHandler.handlerRequestMapping(), reqCtx));

        assertTrue(mappedPathExists("/jsr311-test/handlerE/{id}", requestHandler.pathMatcher()));
    }

    @Test
    public void testFindControllerJSR311g() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("accept", new String[] { MediaType.APPLICATION_JSON });
        headers.put("content-type", new String[] { MediaType.APPLICATION_JSON });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/jsr311-test/12345", "POST", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        assertEquals(4, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertNotNull(reqCtx.handlerResolutionPlan(requestHandler));
        assertNull(reqCtx.handlerResolutionPlan(requestHandler).resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestControllerJSR311.class);
        assertEquals("handlerJSR311g", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/jsr311-test", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestMapping(requestHandler.handlerMethod())));
        assertEquals("application/json", requestHandlers.consumes(requestHandler.handlerRequestMapping(), reqCtx));
        assertEquals("application/json", requestHandlers.produces(requestHandler.handlerRequestMapping(), reqCtx));
        assertTrue(mappedPathExists("/jsr311-test/{id}", requestHandler.pathMatcher()));
        assertEquals("^/jsr311-test/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
    }
}
