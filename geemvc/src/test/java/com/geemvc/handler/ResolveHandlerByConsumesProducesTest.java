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
import com.geemvc.mock.controller.TestController16;
import com.geemvc.test.BaseTest;

public class ResolveHandlerByConsumesProducesTest extends BaseTest {
    @Test
    public void testFindController16a() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("accept", new String[] { MediaType.APPLICATION_JSON });
        headers.put("content-type", new String[] { MediaType.APPLICATION_JSON });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller16/12345", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

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
        assertEquals(requestHandler.controllerClass(), TestController16.class);
        assertEquals("handler16a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller16", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestHandler.handlerRequestMapping()));
        assertEquals("application/json", requestHandlers.consumes(requestHandler.handlerRequestMapping(), reqCtx));
        assertEquals("application/json", requestHandlers.produces(requestHandler.handlerRequestMapping(), reqCtx));
        assertTrue(mappedPathExists("/controller16/{id}", requestHandler.pathMatcher()));
        assertEquals("^/controller16/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
    }

    @Test
    public void testFindController16b() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[] { MediaType.APPLICATION_XML });
        headers.put("Content-Type", new String[] { MediaType.APPLICATION_XML });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller16/12345", "POST", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

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
        assertEquals(requestHandler.controllerClass(), TestController16.class);
        assertEquals("handler16b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller16", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestHandler.handlerRequestMapping()));
        assertEquals("application/xml", requestHandlers.consumes(requestHandler.handlerRequestMapping(), reqCtx));
        assertEquals("application/xml", requestHandlers.produces(requestHandler.handlerRequestMapping(), reqCtx));
        assertTrue(mappedPathExists("/controller16/{id}", requestHandler.pathMatcher()));
        assertEquals("^/controller16/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
    }

    @Test
    public void testFindController16c() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[] { MediaType.APPLICATION_JSON });
        headers.put("Content-Type", new String[] { MediaType.APPLICATION_XML });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller16/handler16", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

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
        assertEquals(requestHandler.controllerClass(), TestController16.class);
        assertEquals("handler16c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller16", controllers.values()));
        assertTrue(mappedPathExists("handler16", requestHandler.handlerRequestMapping()));
        assertEquals("application/xml", requestHandlers.consumes(requestHandler.handlerRequestMapping(), reqCtx));
        assertEquals("application/json", requestHandlers.produces(requestHandler.handlerRequestMapping(), reqCtx));
        assertTrue(mappedPathExists("/controller16/handler16", requestHandler.pathMatcher()));
    }

    @Test
    public void testFindController16d() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[] { MediaType.APPLICATION_XML });
        headers.put("Content-Type", new String[] { MediaType.APPLICATION_JSON });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller16/handler16", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

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
        assertEquals(requestHandler.controllerClass(), TestController16.class);
        assertEquals("handler16d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller16", controllers.values()));
        assertTrue(mappedPathExists("handler16", requestHandler.handlerRequestMapping()));
        assertEquals("application/json", requestHandlers.consumes(requestHandler.handlerRequestMapping(), reqCtx));
        assertEquals("application/xml", requestHandlers.produces(requestHandler.handlerRequestMapping(), reqCtx));
        assertTrue(mappedPathExists("/controller16/handler16", requestHandler.pathMatcher()));
    }

    @Test
    public void testFindController16e() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put(
                "Accept",
                new String[] {
                        "image/gif, image/jpeg, image/pjpeg, application/x-ms-application,         application/vnd.ms-xpsdocument, application/xaml+xml,         application/x-ms-xbap, application/x-shockwave-flash,         application/x-silverlight-2-b2, application/x-silverlight,         application/vnd.ms-excel, application/vnd.ms-powerpoint,         application/msword, text/html, application/xml, */*" });
        headers.put("Content-Type", new String[] { MediaType.APPLICATION_JSON });

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller16/handler16", (Map<String, String[]>) null, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        RequestHandlers requestHandlers = instance(RequestHandlers.class);

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
        assertEquals(requestHandler.controllerClass(), TestController16.class);
        assertEquals("handler16e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller16", controllers.values()));
        assertTrue(mappedPathExists("handler16", requestHandler.handlerRequestMapping()));
        assertEquals("application/json", requestHandlers.consumes(requestHandler.handlerRequestMapping(), reqCtx));
        assertEquals("text/html", requestHandlers.produces(requestHandler.handlerRequestMapping(), reqCtx));
        assertTrue(mappedPathExists("/controller16/handler16", requestHandler.pathMatcher()));
    }
}
