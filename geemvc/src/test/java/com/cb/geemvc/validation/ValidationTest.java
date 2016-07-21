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

package com.cb.geemvc.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.ThreadStash;
import com.cb.geemvc.bind.MethodParam;
import com.cb.geemvc.bind.MethodParams;
import com.cb.geemvc.handler.CompositeControllerResolver;
import com.cb.geemvc.handler.CompositeHandlerResolver;
import com.cb.geemvc.handler.RequestHandler;
import com.cb.geemvc.matcher.PathMatcherKey;
import com.cb.geemvc.mock.bean.Person;
import com.cb.geemvc.mock.controller.TestController18;
import com.cb.geemvc.test.BaseTest;

public class ValidationTest extends BaseTest {
    @Test
    public void testFindController18a() {
        Errors e = instance(Errors.class);

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("person.forename", new String[]{"Michael"});
        requestParams.put("person.surname", new String[]{"Delamere"});
        requestParams.put("person.age", new String[]{"10"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/createPerson", "POST", requestParams);

        ThreadStash.prepare(reqCtx);
        ThreadStash.put(Errors.class, e);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        ValidationContext validationCtx = injector.getInstance(ValidationContext.class).build(reqCtx, typedValues);

        Object o = instance(Validator.class).validate(requestHandler, validationCtx, e);

        requestHandler.invoke(typedValues);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController18.class);
        assertEquals("createPerson", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(2, params.size());
        assertEquals(2, requestValues.size());
        assertEquals(2, typedValues.size());
        assertEquals(Person.class, typedValues.get("person").getClass());
        assertEquals("Michael", ((Person) typedValues.get("person")).getForename());
        assertEquals("Delamere", ((Person) typedValues.get("person")).getSurname());
        assertEquals(10, ((Person) typedValues.get("person")).getAge());
    }

}
