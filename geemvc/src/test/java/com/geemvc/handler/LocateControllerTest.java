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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.test.BaseTest;

public class LocateControllerTest extends BaseTest {
    @Test
    public void testFindAllControllers() {
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.allControllers();

        assertTrue(controllers.size() > 5);
    }

    @Test
    public void testFindControllers1() {
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller1"));

        String regexPath = firstRegexPath(controllers);

        assertEquals(2, controllers.size());
        assertTrue("Exists: /controller1", mappedPathExists("/controller1", controllers));
        assertTrue("Exists: /", mappedPathExists("/", controllers));
        assertNull(regexPath);
    }

    @Test
    public void testFindControllers10() {
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller10"));

        assertEquals(3, controllers.size());

        assertTrue(controllerPathExists("/controller1", controllers.values()));
        assertTrue(mappedPathExists("/controller1", controllers));
        assertTrue(regexPathIsNull("/controller1", controllers));

        assertTrue(controllerPathExists("/controller10", controllers.values()));
        assertTrue(mappedPathExists("/controller10", controllers));
        assertTrue(regexPathIsNull("/controller10", controllers));
    }

    @Test
    public void testFindController2a() {
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller2/handler1"));

        assertEquals(2, controllers.size());

        assertTrue(controllerPathExists("/controller2", controllers.values()));
        assertTrue(mappedPathExists("/controller2", controllers));
        assertTrue(regexPathIsNull("/controller2", controllers));
    }

    @Test
    public void testFindController2b() {
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller2/handler1.html"));

        assertEquals(2, controllers.size());

        assertTrue(controllerPathExists("/controller2", controllers.values()));
        assertTrue(mappedPathExists("/controller2", controllers));
        assertTrue(regexPathIsNull("/controller2", controllers));
    }

    @Test
    public void testFindController3a() {
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller3/handler1"));

        assertEquals(2, controllers.size());

        assertTrue(controllerPathExists("/controller3", controllers.values()));
        assertTrue(mappedPathExists("/controller3", controllers));
        assertTrue(regexPathIsNull("/controller3", controllers));
    }

    @Test
    public void testFindController3b() {
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller3/handler2/a/b/c/d"));

        assertEquals(2, controllers.size());

        assertTrue(controllerPathExists("/controller3", controllers.values()));
        assertTrue(mappedPathExists("/controller3", controllers));
        assertTrue(regexPathIsNull("/controller3", controllers));
    }
}
