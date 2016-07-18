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

import java.util.Map;

import org.junit.Test;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.matcher.PathMatcherKey;
import com.cb.geemvc.mock.Id;
import com.cb.geemvc.mock.bean.RootBeanImpl;
import com.cb.geemvc.mock.controller.TestController19;
import com.cb.geemvc.test.BaseTest;
import com.cb.geemvc.view.bean.View;

public class InvokeHandlerTest extends BaseTest {
    @Test
    public void testFindController19a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller19/handler19a");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        Object result = requestHandler.invoke(null);

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController19.class);
        assertEquals("handler19a", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller19", controllers.values()));
        assertTrue(mappedPathExists("handler19a", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller19/handler19a", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());
        assertNotNull(result);
        assertEquals("forward/to", ((View) result).forward());
    }

    @Test
    public void testFindController19b() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller19/handler19b");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        Object result = requestHandler.invoke(null);

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController19.class);
        assertEquals("handler19b", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller19", controllers.values()));
        assertTrue(mappedPathExists("handler19b", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller19/handler19b", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());
        assertNotNull(result);
        assertEquals("redirect/to", ((View) result).redirect());
    }

    @Test
    public void testFindController19c() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller19/handler19c");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        Object result = requestHandler.invoke(null);

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController19.class);
        assertEquals("handler19c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller19", controllers.values()));
        assertTrue(mappedPathExists("handler19c", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller19/handler19c", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());
        assertNotNull(result);
        assertEquals("forward/to", ((View) result).forward());
        assertEquals(3, ((View) result).size());
        assertEquals("value1", ((View) result).get("var1"));
        assertEquals(Id.valueOf("2"), ((View) result).get("var2"));
        assertEquals(RootBeanImpl.class, ((View) result).get("var3").getClass());
    }

    @Test
    public void testFindController19d() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller19/handler19c");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);

        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());

        Object result = requestHandler.invoke(null);

        assertEquals(3, controllers.size());
        assertNotNull(requestHandler);
        assertNotNull(requestHandler.controllerClass());
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.controllerRequestMapping());
        assertNotNull(requestHandler.handlerRequestMapping());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController19.class);
        assertEquals("handler19c", requestHandler.handlerMethod().getName());
        assertTrue(controllerPathExists("/controller19", controllers.values()));
        assertTrue(mappedPathExists("handler19c", requestHandler.handlerRequestMapping()));
        assertTrue(mappedPathExists("/controller19/handler19c", requestHandler.pathMatcher()));
        assertNull(requestHandler.pathMatcher().getRegexPath());
        assertNotNull(result);
        assertEquals("forward/to", ((View) result).forward());
        assertEquals(3, ((View) result).size());
        assertEquals("value1", ((View) result).get("var1"));
        assertEquals(Id.valueOf("2"), ((View) result).get("var2"));
        assertEquals(RootBeanImpl.class, ((View) result).get("var3").getClass());
    }
}
