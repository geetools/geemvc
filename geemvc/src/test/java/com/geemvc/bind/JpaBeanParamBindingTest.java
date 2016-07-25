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

package com.geemvc.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.geemvc.RequestContext;
import com.geemvc.handler.CompositeControllerResolver;
import com.geemvc.handler.CompositeHandlerResolver;
import com.geemvc.handler.RequestHandler;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.mock.bean.Person;
import com.geemvc.mock.controller.TestController20;
import com.geemvc.mock.repository.Persons;
import com.geemvc.test.BaseTest;
import com.geemvc.view.bean.View;

public class JpaBeanParamBindingTest extends BaseTest {
    @SuppressWarnings("unchecked")
    @Test
    public void testGetAll() throws Exception {
        setupDatasource();
        initTestData();

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/persons");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        View view = (View) requestHandler.invoke(null);
        List<Person> persons = (List<Person>) view.get("persons");

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController20.class);
        assertEquals("getAll", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertNotNull(view);
        assertNotNull(persons);
        assertEquals(4, persons.size());
        assertEquals(0, params.size());
        assertEquals(0, requestValues.size());
        assertEquals(0, typedValues.size());
        assertEquals("Michael", persons.get(0).getForename());
        assertEquals("Tom", persons.get(1).getForename());
        assertEquals("Marc", persons.get(2).getForename());
        assertEquals("Lea", persons.get(3).getForename());
    }

    @Test
    public void testGetById() throws Exception {
        setupDatasource();
        initTestData();

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/persons/1");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        View view = (View) requestHandler.invoke(typedValues);
        Person person = (Person) view.get("person");

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController20.class);
        assertEquals("get", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertNotNull(view);
        assertNotNull(view.forward());
        assertNotNull(person);
        assertEquals("person/details", view.forward());
        assertEquals(Person.class, typedValues.get("person").getClass());
        assertEquals("Michael", person.getForename());
        assertEquals("Delamere", person.getSurname());
        assertEquals(10, person.getAge());
    }

    @Test
    public void testCreate() throws Exception {
        setupDatasource();
        initTestData();

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("person.id", new String[]{"5"});
        requestParams.put("person.forename", new String[]{"Yulia"});
        requestParams.put("person.surname", new String[]{"Lady"});
        requestParams.put("person.age", new String[]{"9"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/persons", "POST", requestParams);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        View view = (View) requestHandler.invoke(typedValues);
        Person person = (Person) view.get("person");

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController20.class);
        assertEquals("create", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertNotNull(person);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(Person.class, typedValues.get("person").getClass());
        assertEquals(new Long(5), person.getId());
        assertEquals("Yulia", person.getForename());
        assertEquals("Lady", person.getSurname());
        assertEquals(9, person.getAge());
    }

    @Test
    public void testUpdate() throws Exception {
        setupDatasource();
        initTestData();

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("person.age", new String[]{"99"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/persons/4", "PUT", requestParams);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        String redirect = (String) requestHandler.invoke(typedValues);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController20.class);
        assertEquals("update", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertNotNull(redirect);
        assertEquals(2, params.size());
        assertEquals(2, requestValues.size());
        assertEquals(2, typedValues.size());
        assertEquals(Person.class, typedValues.get("person").getClass());
        assertEquals("redirect:/persons/details/4", redirect);

        Persons persons = instance(Persons.class);
        Person person = persons.havingId(4l);

        assertNotNull(person);
        assertEquals(new Long(4), person.getId());
        assertEquals("Lea", person.getForename());
        assertEquals("Cool", person.getSurname());
        assertEquals(99, person.getAge());
    }

    @Test
    public void testDelete() throws Exception {
        setupDatasource();
        initTestData();

        Persons persons = instance(Persons.class);
        Person person = persons.havingId(3l);

        assertNotNull(person);
        assertEquals(new Long(3), person.getId());
        assertEquals("Marc", person.getForename());
        assertEquals("Dude", person.getSurname());
        assertEquals(4, person.getAge());

        List<Person> personList = persons.all();
        assertEquals(4, personList.size());

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/persons/3", "DELETE");

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        String redirect = (String) requestHandler.invoke(typedValues);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController20.class);
        assertEquals("delete", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertNotNull(redirect);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(Person.class, typedValues.get("person").getClass());
        assertEquals("redirect:/persons", redirect);

        // Should be null after delete.
        person = persons.havingId(3l);
        assertNull(person);

        // Should be 1 less now.
        personList = persons.all();
        assertEquals(3, personList.size());
    }
}
