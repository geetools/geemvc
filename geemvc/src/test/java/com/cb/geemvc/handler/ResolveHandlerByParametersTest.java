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
import com.cb.geemvc.mock.controller.TestController9;
import com.cb.geemvc.test.BaseTest;

public class ResolveHandlerByParametersTest extends BaseTest {
    @Test
    public void testFindController9a() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"update"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParameterExists("cmd=update", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9b() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"delete"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParameterExists("cmd=delete", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9c() {
        Map<String, String[]> params = new HashMap<>();
        params.put("cmd", new String[]{"delete"});
        params.put("id", new String[]{"12345"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9/handler", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"cmd=delete", "id=^\\d+"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9d() {
        Map<String, String[]> params = new HashMap<>();
        params.put("param", new String[]{"one", "two", "three", "four",});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9/handler", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9d", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"param=one", "param=two", "param=^thr.+", "param=fou*"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9e() {
        Map<String, String[]> params = new HashMap<>();
        params.put("orParam", new String[]{"two"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9/handler", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9e", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("/handler", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"orParam=^(?iu:ONE|TWO|THREE|FOUR)$"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9/handler", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9f() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramExists", null);

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9f", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"paramExists"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9g() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramExists", null);
        params.put("paramOneExists", null);
        params.put("paramTwoExists", null);

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "PUT", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9g", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"paramOneExists", "paramTwoExists"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9gg() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramExists", null);
        params.put("paramOneExists", null);
        params.put("paramTwoExists", null);

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9gg", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"paramOneExists", "paramTwoExists"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9h() {
        Map<String, String[]> params = new HashMap<>();
        params.put("booleanParam", new String[]{"false"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9h", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"booleanParam!=true"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9i() {
        Map<String, String[]> params = new HashMap<>();
        params.put("booleanParamOne", new String[]{"true"});
        params.put("booleanParamTwo", new String[]{"false"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9i", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"booleanParamOne=true", "booleanParamTwo!=true"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9ii() {
        Map<String, String[]> params = new HashMap<>();
        params.put("booleanParamOne", new String[]{"true"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9ii", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"booleanParamOne=true"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9j() {
        Map<String, String[]> params = new HashMap<>();
        params.put("jParamOne", new String[]{"false"});
        params.put("jParamTwo", new String[]{"false"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9j", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"jParamOne != ^notTr[ue]+", "jParamTwo!=tru*"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9k() {
        Map<String, String[]> params = new HashMap<>();
        params.put("kParamOne", new String[]{"false"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9k", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"kParamOne != ^notTr[ue]+$", "kParamOne!=tru*"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9l() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramOne", new String[]{"101"});
        params.put("paramTwo", new String[]{"202"});
        params.put("paramThree", new String[]{"303"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9l", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"js: paramOne > 100", "groovy: (paramTwo as int) > 200", "mvel: paramThree > 300"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9m() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramOne", new String[]{"one"});
        params.put("paramTwo", new String[]{"two"});
        params.put("paramThree", new String[]{"three"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9m", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"js: paramOne == 'one'", "groovy: paramTwo == 'two'", "mvel: paramThree == 'three'"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9n() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramOne", new String[]{"not one"});
        params.put("paramTwo", new String[]{"not two"});
        params.put("paramThree", new String[]{"not three"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9n", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"js: paramOne != 'one' && paramOne != 101 && paramOne != 11", "groovy: paramTwo != 'two' && paramTwo != '202' && paramTwo != '22'",
                "mvel: paramThree != 'three' && paramThree != 303 && paramThree != 33"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9o() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramOne", new String[]{"11"});
        params.put("paramTwo", new String[]{"11"});
        params.put("paramThree", new String[]{"11"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9o", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"js: paramOne <= 100 && paramOne == paramTwo", "groovy: (paramTwo as int) <= 100 && paramTwo == paramThree", "mvel: paramThree <= 100 && paramThree == paramOne"},
                requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9p() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramOne", new String[]{"11"});
        params.put("paramTwo", new String[]{"22"});
        params.put("paramThree", new String[]{"33"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9p", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"js: paramOne == 100 || paramOne == 11", "groovy: (paramTwo as int) == 200 || (paramTwo as int) == 22", "mvel: paramThree == 300 || paramThree == 33"}, requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }

    @Test
    public void testFindController9q() {
        Map<String, String[]> params = new HashMap<>();
        params.put("paramOne", new String[]{"handlerQQ"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller9", "GET", params);

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
        assertNotNull(requestHandler.resolvedParameters());
        assertEquals(requestHandler.controllerClass(), TestController9.class);
        assertEquals("handler9q", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller9", controllers.values()));
        assertTrue(mappedPathExists("", requestHandler.handlerRequestMapping()));
        assertTrue(mappedParametersExists(new String[]{"js: 1 == 0 || (/handler[q]+/igm.test(paramOne))", "groovy: 1 == 0 || (paramOne ==~ /(?im)handler[q]+/)", "mvel: 1 == 0 || (paramOne ~= '(?im)handler[q]+')"},
                requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller9", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());

    }
}
