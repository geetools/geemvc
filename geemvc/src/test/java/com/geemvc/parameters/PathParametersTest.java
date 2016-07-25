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

package com.geemvc.parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import com.geemvc.HttpMethod;
import com.geemvc.handler.CompositeControllerResolver;
import com.geemvc.handler.RequestHandler;
import org.junit.Assert;
import org.junit.Test;

import com.geemvc.RequestContext;
import com.geemvc.handler.CompositeHandlerResolver;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.mock.controller.TestController15;
import com.geemvc.test.BaseTest;

public class PathParametersTest extends BaseTest {
    @Test
    public void testPathParameters15a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/handler15");

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("/handler15", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller15/handler15", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());
    }

    @Test
    public void testPathParameters15b() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/handler15/someParam");

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("/handler15/{param1}", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/handler15/{param1}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/handler15/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 1);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("param1"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("param1")[0], "someParam");
    }

    @Test
    public void testPathParameters15c() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/handler15/someParam/andAnother");

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("/handler15/{param1}/{param2}", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/handler15/{param1}/{param2}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/handler15/([^\\/]+)/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 2);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("param1"));
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("param2"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("param1")[0], "someParam");
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("param2")[0], "andAnother");
    }

    @Test
    public void testPathParameters15cc() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/handler15/someParam-andAnother");

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15cc", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("/handler15/{param1}-{param2}", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/handler15/{param1}-{param2}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/handler15/([^\\/]+)-([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 2);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("param1"));
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("param2"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("param1")[0], "someParam");
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("param2")[0], "andAnother");
    }

    @Test
    public void testPathParameters15d() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/handler15/id/12345");

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("/handler15/id/{param3:^\\d+}", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/handler15/id/{param3:^\\d+}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/handler15/id/(\\d+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 1);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("param3"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("param3")[0], "12345");
    }

    @Test
    public void testPathParameters15e() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/handler15/id/12345/testme");

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("/handler15/id/{param3:^\\d+}/testme", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/handler15/id/{param3:^\\d+}/testme", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/handler15/id/(\\d+)/testme$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 1);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("param3"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("param3")[0], "12345");
    }

    @Test
    public void testPathParameters15f() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/handler15/id/12345/abcde/testme");

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15f", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("/handler15/id/{param3:^\\d+}/{param4}/testme", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/handler15/id/{param3:^\\d+}/{param4}/testme", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/handler15/id/(\\d+)/([^\\/]+)/testme$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 2);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("param3"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("param3")[0], "12345");
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("param4"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("param4")[0], "abcde");
    }

    @Test
    public void testPathParameters15g() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/12345");

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15g", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/{id}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 1);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("id"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("id")[0], "12345");
    }

    @Test
    public void testPathParameters15h() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/12345", "POST");

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15h", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/{id}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 1);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("id"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("id")[0], "12345");
        Assert.assertEquals(requestHandler.handlerRequestMapping().method()[0], HttpMethod.POST);
    }

    @Test
    public void testPathParameters15i() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/12345", "POST", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15i", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/json", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/{id}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 1);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("id"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("id")[0], "12345");
        Assert.assertEquals(requestHandler.handlerRequestMapping().method()[0], HttpMethod.POST);
    }

    @Test
    public void testPathParameters15j() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/xml"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/12345", "POST", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15j", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept  =  application/xml", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/{id}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 1);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("id"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("id")[0], "12345");
        Assert.assertEquals(requestHandler.handlerRequestMapping().method()[0], HttpMethod.POST);
    }

    @Test
    public void testPathParameters15k() {
        Map<String, String[]> params = new HashMap<>();
        params.put("param1", new String[]{"val1"});

        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/xml"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/12345", "POST", params, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15k", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParameterExists("param1=val1", requestHandler.handlerRequestMapping()));
        assertTrue(mappedHeaderExists("Accept=application/xml", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/{id}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 1);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("id"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("id")[0], "12345");
        Assert.assertEquals(requestHandler.handlerRequestMapping().method()[0], HttpMethod.POST);
    }

    @Test
    public void testPathParameters15l() {

        // headers.put("Accept", new String[] { "application/json" });
        //
        // RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/12345", "POST", (Map<String, String[]>)
        // null, headers);

        Map<String, String[]> params = new HashMap<>();
        params.put("param1", new String[]{"val1"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/12345", "POST", params);

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15l", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("{id}", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParameterExists("param1=val1", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/{id}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/([^\\/]+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 1);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("id"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("id")[0], "12345");
        Assert.assertEquals(requestHandler.handlerRequestMapping().method()[0], HttpMethod.POST);
    }

    @Test
    public void testPathParameters15m() {
        Map<String, String[]> params = new HashMap<>();
        params.put("regex-param", new String[]{"12345"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller15/handler15/test-regex/12345", params);

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
        assertEquals(requestHandler.controllerClass(), TestController15.class);
        assertEquals("handler15m", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller15", controllers.values()));
        assertTrue(mappedPathExists("/handler15/test-regex/{regex-param:\\d+}", requestHandler.handlerRequestMapping()));
        assertEquals("/controller15/handler15/test-regex/{regex-param:\\d+}", requestHandler.pathMatcher().getMappedPath());
        assertEquals("^/controller15/handler15/test-regex/(\\d+)$", requestHandler.pathMatcher().getRegexPath());
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).size(), 1);
        assertNotNull(requestHandler.pathMatcher().parameters(reqCtx).containsKey("regex-param"));
        assertEquals(requestHandler.pathMatcher().parameters(reqCtx).get("regex-param")[0], "12345");
    }
}
