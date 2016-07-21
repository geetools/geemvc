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

package com.cb.geemvc.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jodd.typeconverter.TypeConverterManager;

import org.junit.Test;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.ThreadStash;
import com.cb.geemvc.handler.CompositeControllerResolver;
import com.cb.geemvc.handler.CompositeHandlerResolver;
import com.cb.geemvc.handler.RequestHandler;
import com.cb.geemvc.matcher.PathMatcherKey;
import com.cb.geemvc.mock.Id;
import com.cb.geemvc.mock.bean.Address;
import com.cb.geemvc.mock.bean.AddressType;
import com.cb.geemvc.mock.bean.Person;
import com.cb.geemvc.mock.controller.TestController18;
import com.cb.geemvc.mock.converter.IdConverter;
import com.cb.geemvc.test.BaseTest;
import com.cb.geemvc.validation.Errors;
import com.cb.geemvc.validation.ValidationContext;
import com.cb.geemvc.validation.Validator;

public class ModelParamBindingTest extends BaseTest {
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

    @Test
    public void testFindController18b() {
        TypeConverterManager.register(Id.class, new IdConverter());

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("person.id", new String[]{"1234567890"});
        requestParams.put("person.forename", new String[]{"Michael"});
        requestParams.put("person.surname", new String[]{"Delamere"});
        requestParams.put("person.age", new String[]{"10"});
        requestParams.put("person.locale", new String[]{"en_US"});
        requestParams.put("person.addresses[0].id", new String[]{"234567890"});
        requestParams.put("person.addresses[0].streetLines[]", new String[]{"Test Street 1", "c/o Tom Checker"});
        requestParams.put("person.addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("person.addresses[0].zip", new String[]{"12345"});
        requestParams.put("person.addresses[0].city", new String[]{"Washington"});
        requestParams.put("person.addresses[0].countryCode", new String[]{"us"});

        requestParams.put("person.addresses[1].id", new String[]{"34567890"});
        requestParams.put("person.addresses[1].streetLines[]", new String[]{"Test Street 2", "c/o Marc Checker"});
        requestParams.put("person.addresses[1].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("person.addresses[1].zip", new String[]{"67890"});
        requestParams.put("person.addresses[1].city", new String[]{"New York"});
        requestParams.put("person.addresses[1].countryCode", new String[]{"us"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/createPerson", requestParams);
        ThreadStash.prepare(reqCtx);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

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
        assertEquals(Long.valueOf("1234567890"), ((Person) typedValues.get("person")).getId());
        assertEquals("Michael", ((Person) typedValues.get("person")).getForename());
        assertEquals("Delamere", ((Person) typedValues.get("person")).getSurname());
        assertEquals(10, ((Person) typedValues.get("person")).getAge());
        assertEquals(Locale.US, ((Person) typedValues.get("person")).getLocale());
        assertNotNull(((Person) typedValues.get("person")).getAddresses());
        assertEquals(2, ((Person) typedValues.get("person")).getAddresses().size());

        Address shippingAddress = ((Person) typedValues.get("person")).getAddresses().stream().filter(a -> a.getAddressTypes().get(0) == AddressType.SHIPPING).findFirst().get();

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("234567890"), shippingAddress.getId());
        assertEquals("Test Street 1", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Tom Checker", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("12345", shippingAddress.getZip());
        assertEquals("Washington", shippingAddress.getCity());
        assertEquals("us", shippingAddress.getCountryCode());

        Address invoiceAddress = ((Person) typedValues.get("person")).getAddresses().stream().filter(a -> a.getAddressTypes().get(0) == AddressType.INVOICE).findFirst().get();

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("34567890"), invoiceAddress.getId());
        assertEquals("Test Street 2", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Checker", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("67890", invoiceAddress.getZip());
        assertEquals("New York", invoiceAddress.getCity());
        assertEquals("us", invoiceAddress.getCountryCode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController18c() {
        TypeConverterManager.register(Id.class, new IdConverter());

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("persons[0].forename", new String[]{"Michael"});
        requestParams.put("persons[0].surname", new String[]{"Delamere"});
        requestParams.put("persons[0].age", new String[]{"10"});
        requestParams.put("persons[0].locale", new String[]{"en_US"});
        requestParams.put("persons[0].addresses[0].id", new String[]{"234567890"});
        requestParams.put("persons[0].addresses[0].streetLines[]", new String[]{"Test Street 1", "c/o Tom Checker"});
        requestParams.put("persons[0].addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("persons[0].addresses[0].zip", new String[]{"12345"});
        requestParams.put("persons[0].addresses[0].city", new String[]{"Washington"});
        requestParams.put("persons[0].addresses[0].countryCode", new String[]{"us"});

        requestParams.put("persons[1].forename", new String[]{"Tom"});
        requestParams.put("persons[1].surname", new String[]{"Checker"});
        requestParams.put("persons[1].age", new String[]{"1"});
        requestParams.put("persons[1].locale", new String[]{"de_DE"});
        requestParams.put("persons[1].addresses[0].id", new String[]{"34567890"});
        requestParams.put("persons[1].addresses[0].streetLines[]", new String[]{"Test Street 2", "c/o Marc Checker"});
        requestParams.put("persons[1].addresses[0].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("persons[1].addresses[0].zip", new String[]{"67890"});
        requestParams.put("persons[1].addresses[0].city", new String[]{"New York"});
        requestParams.put("persons[1].addresses[0].countryCode", new String[]{"us"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/createPersons", requestParams);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController18.class);
        assertEquals("createPersons", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(ArrayList.class, typedValues.get("persons").getClass());
        assertEquals("Michael", ((List<Person>) typedValues.get("persons")).get(0).getForename());
        assertEquals("Delamere", ((List<Person>) typedValues.get("persons")).get(0).getSurname());
        assertEquals(10, ((List<Person>) typedValues.get("persons")).get(0).getAge());

        assertEquals(Locale.US, ((List<Person>) typedValues.get("persons")).get(0).getLocale());
        assertNotNull(((List<Person>) typedValues.get("persons")).get(0).getAddresses());
        assertEquals(1, ((List<Person>) typedValues.get("persons")).get(0).getAddresses().size());

        Address shippingAddress = ((List<Person>) typedValues.get("persons")).get(0).getAddresses().get(0);

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("234567890"), shippingAddress.getId());
        assertEquals(AddressType.SHIPPING, shippingAddress.getAddressTypes().get(0));
        assertEquals("Test Street 1", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Tom Checker", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("12345", shippingAddress.getZip());
        assertEquals("Washington", shippingAddress.getCity());
        assertEquals("us", shippingAddress.getCountryCode());

        assertEquals(Locale.GERMANY, ((List<Person>) typedValues.get("persons")).get(1).getLocale());
        assertNotNull(((List<Person>) typedValues.get("persons")).get(1).getAddresses());
        assertEquals(1, ((List<Person>) typedValues.get("persons")).get(1).getAddresses().size());

        Address invoiceAddress = ((List<Person>) typedValues.get("persons")).get(1).getAddresses().get(0);

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("34567890"), invoiceAddress.getId());
        assertEquals(AddressType.INVOICE, invoiceAddress.getAddressTypes().get(0));
        assertEquals("Test Street 2", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Checker", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("67890", invoiceAddress.getZip());
        assertEquals("New York", invoiceAddress.getCity());
        assertEquals("us", invoiceAddress.getCountryCode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController18d() {
        TypeConverterManager.register(Id.class, new IdConverter());

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("persons[customerA].forename", new String[]{"Michael"});
        requestParams.put("persons[customerA].surname", new String[]{"Delamere"});
        requestParams.put("persons[customerA].age", new String[]{"10"});
        requestParams.put("persons[customerA].locale", new String[]{"en_US"});
        requestParams.put("persons[customerA].addresses[0].id", new String[]{"234567890"});
        requestParams.put("persons[customerA].addresses[0].streetLines[]", new String[]{"Test Street 1", "c/o Tom Checker"});
        requestParams.put("persons[customerA].addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("persons[customerA].addresses[0].zip", new String[]{"12345"});
        requestParams.put("persons[customerA].addresses[0].city", new String[]{"Washington"});
        requestParams.put("persons[customerA].addresses[0].countryCode", new String[]{"us"});

        requestParams.put("persons[customerB].forename", new String[]{"Tom"});
        requestParams.put("persons[customerB].surname", new String[]{"Checker"});
        requestParams.put("persons[customerB].age", new String[]{"1"});
        requestParams.put("persons[customerB].locale", new String[]{"de_DE"});
        requestParams.put("persons[customerB].addresses[0].id", new String[]{"34567890"});
        requestParams.put("persons[customerB].addresses[0].streetLines[]", new String[]{"Test Street 2", "c/o Marc Checker"});
        requestParams.put("persons[customerB].addresses[0].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("persons[customerB].addresses[0].zip", new String[]{"67890"});
        requestParams.put("persons[customerB].addresses[0].city", new String[]{"New York"});
        requestParams.put("persons[customerB].addresses[0].countryCode", new String[]{"us"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/updatePersons", requestParams);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController18.class);
        assertEquals("updatePersons", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(LinkedHashMap.class, typedValues.get("persons").getClass());
        assertEquals("Michael", ((Map<String, Person>) typedValues.get("persons")).get("customerA").getForename());
        assertEquals("Delamere", ((Map<String, Person>) typedValues.get("persons")).get("customerA").getSurname());
        assertEquals(10, ((Map<String, Person>) typedValues.get("persons")).get("customerA").getAge());

        assertEquals(Locale.US, ((Map<String, Person>) typedValues.get("persons")).get("customerA").getLocale());
        assertNotNull(((Map<String, Person>) typedValues.get("persons")).get("customerA").getAddresses());
        assertEquals(1, ((Map<String, Person>) typedValues.get("persons")).get("customerA").getAddresses().size());

        Address shippingAddress = ((Map<String, Person>) typedValues.get("persons")).get("customerA").getAddresses().get(0);

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("234567890"), shippingAddress.getId());
        assertEquals(AddressType.SHIPPING, shippingAddress.getAddressTypes().get(0));
        assertEquals("Test Street 1", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Tom Checker", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("12345", shippingAddress.getZip());
        assertEquals("Washington", shippingAddress.getCity());
        assertEquals("us", shippingAddress.getCountryCode());

        assertEquals(Locale.GERMANY, ((Map<String, Person>) typedValues.get("persons")).get("customerB").getLocale());
        assertNotNull(((Map<String, Person>) typedValues.get("persons")).get("customerB").getAddresses());
        assertEquals(1, ((Map<String, Person>) typedValues.get("persons")).get("customerB").getAddresses().size());

        Address invoiceAddress = ((Map<String, Person>) typedValues.get("persons")).get("customerB").getAddresses().get(0);

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("34567890"), invoiceAddress.getId());
        assertEquals(AddressType.INVOICE, invoiceAddress.getAddressTypes().get(0));
        assertEquals("Test Street 2", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Checker", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("67890", invoiceAddress.getZip());
        assertEquals("New York", invoiceAddress.getCity());
        assertEquals("us", invoiceAddress.getCountryCode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController18e() {
        TypeConverterManager.register(Id.class, new IdConverter());

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("persons[1234567890].forename", new String[]{"Michael"});
        requestParams.put("persons[1234567890].surname", new String[]{"Delamere"});
        requestParams.put("persons[1234567890].age", new String[]{"10"});
        requestParams.put("persons[1234567890].locale", new String[]{"en_US"});
        requestParams.put("persons[1234567890].addresses[0].id", new String[]{"234567890"});
        requestParams.put("persons[1234567890].addresses[0].streetLines[]", new String[]{"Test Street 1", "c/o Tom Checker"});
        requestParams.put("persons[1234567890].addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("persons[1234567890].addresses[0].zip", new String[]{"12345"});
        requestParams.put("persons[1234567890].addresses[0].city", new String[]{"Washington"});
        requestParams.put("persons[1234567890].addresses[0].countryCode", new String[]{"us"});

        requestParams.put("persons[987654321].forename", new String[]{"Tom"});
        requestParams.put("persons[987654321].surname", new String[]{"Checker"});
        requestParams.put("persons[987654321].age", new String[]{"1"});
        requestParams.put("persons[987654321].locale", new String[]{"de_DE"});
        requestParams.put("persons[987654321].addresses[0].id", new String[]{"34567890"});
        requestParams.put("persons[987654321].addresses[0].streetLines[]", new String[]{"Test Street 2", "c/o Marc Checker"});
        requestParams.put("persons[987654321].addresses[0].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("persons[987654321].addresses[0].zip", new String[]{"67890"});
        requestParams.put("persons[987654321].addresses[0].city", new String[]{"New York"});
        requestParams.put("persons[987654321].addresses[0].countryCode", new String[]{"us"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/updatePersons2", requestParams);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController18.class);
        assertEquals("updatePersons2", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(LinkedHashMap.class, typedValues.get("persons").getClass());
        assertEquals("Michael", ((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("1234567890")).getForename());
        assertEquals("Delamere", ((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("1234567890")).getSurname());
        assertEquals(10, ((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("1234567890")).getAge());

        assertEquals(Locale.US, ((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("1234567890")).getLocale());
        assertNotNull(((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("1234567890")).getAddresses());
        assertEquals(1, ((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("1234567890")).getAddresses().size());

        Address shippingAddress = ((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("1234567890")).getAddresses().get(0);

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("234567890"), shippingAddress.getId());
        assertEquals(AddressType.SHIPPING, shippingAddress.getAddressTypes().get(0));
        assertEquals("Test Street 1", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Tom Checker", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("12345", shippingAddress.getZip());
        assertEquals("Washington", shippingAddress.getCity());
        assertEquals("us", shippingAddress.getCountryCode());

        assertEquals(Locale.GERMANY, ((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("987654321")).getLocale());
        assertNotNull(((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("987654321")).getAddresses());
        assertEquals(1, ((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("987654321")).getAddresses().size());

        Address invoiceAddress = ((Map<Id, Person>) typedValues.get("persons")).get(Long.valueOf("987654321")).getAddresses().get(0);

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("34567890"), invoiceAddress.getId());
        assertEquals(AddressType.INVOICE, invoiceAddress.getAddressTypes().get(0));
        assertEquals("Test Street 2", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Checker", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("67890", invoiceAddress.getZip());
        assertEquals("New York", invoiceAddress.getCity());
        assertEquals("us", invoiceAddress.getCountryCode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController18f() {
        TypeConverterManager.register(Id.class, new IdConverter());

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("persons[groupA][0].forename", new String[]{"Michael"});
        requestParams.put("persons[groupA][0].surname", new String[]{"Delamere"});
        requestParams.put("persons[groupA][0].age", new String[]{"10"});
        requestParams.put("persons[groupA][0].locale", new String[]{"en_US"});
        requestParams.put("persons[groupA][0].addresses[0].id", new String[]{"234567890"});
        requestParams.put("persons[groupA][0].addresses[0].streetLines[]", new String[]{"Test Street 1", "c/o Tom Checker"});
        requestParams.put("persons[groupA][0].addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("persons[groupA][0].addresses[0].zip", new String[]{"12345"});
        requestParams.put("persons[groupA][0].addresses[0].city", new String[]{"Washington"});
        requestParams.put("persons[groupA][0].addresses[0].countryCode", new String[]{"US"});

        requestParams.put("persons[groupA][1].forename", new String[]{"Tom"});
        requestParams.put("persons[groupA][1].surname", new String[]{"Checker"});
        requestParams.put("persons[groupA][1].age", new String[]{"1"});
        requestParams.put("persons[groupA][1].locale", new String[]{"de_DE"});
        requestParams.put("persons[groupA][1].addresses[0].id", new String[]{"34567890"});
        requestParams.put("persons[groupA][1].addresses[0].streetLines[]", new String[]{"Test Street 2", "c/o Marc Checker"});
        requestParams.put("persons[groupA][1].addresses[0].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("persons[groupA][1].addresses[0].zip", new String[]{"67890"});
        requestParams.put("persons[groupA][1].addresses[0].city", new String[]{"New York"});
        requestParams.put("persons[groupA][1].addresses[0].countryCode", new String[]{"US"});

        requestParams.put("persons[groupB][0].forename", new String[]{"Lea"});
        requestParams.put("persons[groupB][0].surname", new String[]{"Cool"});
        requestParams.put("persons[groupB][0].age", new String[]{"8"});
        requestParams.put("persons[groupB][0].locale", new String[]{"de_DE"});
        requestParams.put("persons[groupB][0].addresses[0].id", new String[]{"88888888"});
        requestParams.put("persons[groupB][0].addresses[0].streetLines[]", new String[]{"Test Straße 8", "c/o Marc Dude"});
        requestParams.put("persons[groupB][0].addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("persons[groupB][0].addresses[0].zip", new String[]{"88888"});
        requestParams.put("persons[groupB][0].addresses[0].city", new String[]{"Berlin"});
        requestParams.put("persons[groupB][0].addresses[0].countryCode", new String[]{"DE"});

        requestParams.put("persons[groupB][1].forename", new String[]{"Marc"});
        requestParams.put("persons[groupB][1].surname", new String[]{"Dude"});
        requestParams.put("persons[groupB][1].age", new String[]{"4"});
        requestParams.put("persons[groupB][1].locale", new String[]{"en_GB"});
        requestParams.put("persons[groupB][1].addresses[0].id", new String[]{"4444"});
        requestParams.put("persons[groupB][1].addresses[0].streetLines[]", new String[]{"Test Avenue 4", "c/o Lea Cool"});
        requestParams.put("persons[groupB][1].addresses[0].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("persons[groupB][1].addresses[0].zip", new String[]{"KY11 9UH"});
        requestParams.put("persons[groupB][1].addresses[0].city", new String[]{"Dalgety Bay"});
        requestParams.put("persons[groupB][1].addresses[0].countryCode", new String[]{"GB"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/updatePersons3", requestParams);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController18.class);
        assertEquals("updatePersons3", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(LinkedHashMap.class, typedValues.get("persons").getClass());
        assertEquals("Michael", ((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(0).getForename());
        assertEquals("Delamere", ((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(0).getSurname());
        assertEquals(10, ((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(0).getAge());

        assertEquals(Locale.US, ((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(0).getLocale());
        assertNotNull(((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(0).getAddresses());
        assertEquals(1, ((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(0).getAddresses().size());

        Address shippingAddress = ((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(0).getAddresses().get(0);

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("234567890"), shippingAddress.getId());
        assertEquals(AddressType.SHIPPING, shippingAddress.getAddressTypes().get(0));
        assertEquals("Test Street 1", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Tom Checker", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("12345", shippingAddress.getZip());
        assertEquals("Washington", shippingAddress.getCity());
        assertEquals("US", shippingAddress.getCountryCode());

        assertEquals(Locale.GERMANY, ((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(1).getLocale());
        assertNotNull(((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(1).getAddresses());
        assertEquals(1, ((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(1).getAddresses().size());

        Address invoiceAddress = ((Map<String, List<Person>>) typedValues.get("persons")).get("groupA").get(1).getAddresses().get(0);

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("34567890"), invoiceAddress.getId());
        assertEquals(AddressType.INVOICE, invoiceAddress.getAddressTypes().get(0));
        assertEquals("Test Street 2", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Checker", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("67890", invoiceAddress.getZip());
        assertEquals("New York", invoiceAddress.getCity());
        assertEquals("US", invoiceAddress.getCountryCode());

        assertEquals("Lea", ((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(0).getForename());
        assertEquals("Cool", ((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(0).getSurname());
        assertEquals(8, ((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(0).getAge());

        assertEquals(Locale.GERMANY, ((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(0).getLocale());
        assertNotNull(((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(0).getAddresses());
        assertEquals(1, ((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(0).getAddresses().size());

        shippingAddress = ((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(0).getAddresses().get(0);

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("88888888"), shippingAddress.getId());
        assertEquals(AddressType.SHIPPING, shippingAddress.getAddressTypes().get(0));
        assertEquals("Test Straße 8", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Dude", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("88888", shippingAddress.getZip());
        assertEquals("Berlin", shippingAddress.getCity());
        assertEquals("DE", shippingAddress.getCountryCode());

        assertEquals(Locale.UK, ((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(1).getLocale());
        assertNotNull(((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(1).getAddresses());
        assertEquals(1, ((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(1).getAddresses().size());

        invoiceAddress = ((Map<String, List<Person>>) typedValues.get("persons")).get("groupB").get(1).getAddresses().get(0);

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("4444"), invoiceAddress.getId());
        assertEquals(AddressType.INVOICE, invoiceAddress.getAddressTypes().get(0));
        assertEquals("Test Avenue 4", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Lea Cool", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("KY11 9UH", invoiceAddress.getZip());
        assertEquals("Dalgety Bay", invoiceAddress.getCity());
        assertEquals("GB", invoiceAddress.getCountryCode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController18g() {
        TypeConverterManager.register(Id.class, new IdConverter());

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("persons[groupA][0].forename", new String[]{"Michael"});
        requestParams.put("persons[groupA][0].surname", new String[]{"Delamere"});
        requestParams.put("persons[groupA][0].age", new String[]{"10"});
        requestParams.put("persons[groupA][0].locale", new String[]{"en_US"});
        requestParams.put("persons[groupA][0].addresses[0].id", new String[]{"234567890"});
        requestParams.put("persons[groupA][0].addresses[0].streetLines[]", new String[]{"Test Street 1", "c/o Tom Checker"});
        requestParams.put("persons[groupA][0].addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("persons[groupA][0].addresses[0].zip", new String[]{"12345"});
        requestParams.put("persons[groupA][0].addresses[0].city", new String[]{"Washington"});
        requestParams.put("persons[groupA][0].addresses[0].countryCode", new String[]{"US"});

        requestParams.put("persons[groupA][1].forename", new String[]{"Tom"});
        requestParams.put("persons[groupA][1].surname", new String[]{"Checker"});
        requestParams.put("persons[groupA][1].age", new String[]{"1"});
        requestParams.put("persons[groupA][1].locale", new String[]{"de_DE"});
        requestParams.put("persons[groupA][1].addresses[0].id", new String[]{"34567890"});
        requestParams.put("persons[groupA][1].addresses[0].streetLines[]", new String[]{"Test Street 2", "c/o Marc Checker"});
        requestParams.put("persons[groupA][1].addresses[0].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("persons[groupA][1].addresses[0].zip", new String[]{"67890"});
        requestParams.put("persons[groupA][1].addresses[0].city", new String[]{"New York"});
        requestParams.put("persons[groupA][1].addresses[0].countryCode", new String[]{"US"});

        requestParams.put("persons[groupB][0].forename", new String[]{"Lea"});
        requestParams.put("persons[groupB][0].surname", new String[]{"Cool"});
        requestParams.put("persons[groupB][0].age", new String[]{"8"});
        requestParams.put("persons[groupB][0].locale", new String[]{"de_DE"});
        requestParams.put("persons[groupB][0].addresses[0].id", new String[]{"88888888"});
        requestParams.put("persons[groupB][0].addresses[0].streetLines[]", new String[]{"Test Straße 8", "c/o Marc Dude"});
        requestParams.put("persons[groupB][0].addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("persons[groupB][0].addresses[0].zip", new String[]{"88888"});
        requestParams.put("persons[groupB][0].addresses[0].city", new String[]{"Berlin"});
        requestParams.put("persons[groupB][0].addresses[0].countryCode", new String[]{"DE"});

        requestParams.put("persons[groupB][1].forename", new String[]{"Marc"});
        requestParams.put("persons[groupB][1].surname", new String[]{"Dude"});
        requestParams.put("persons[groupB][1].age", new String[]{"4"});
        requestParams.put("persons[groupB][1].locale", new String[]{"en_GB"});
        requestParams.put("persons[groupB][1].addresses[0].id", new String[]{"4444"});
        requestParams.put("persons[groupB][1].addresses[0].streetLines[]", new String[]{"Test Avenue 4", "c/o Lea Cool"});
        requestParams.put("persons[groupB][1].addresses[0].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("persons[groupB][1].addresses[0].zip", new String[]{"KY11 9UH"});
        requestParams.put("persons[groupB][1].addresses[0].city", new String[]{"Dalgety Bay"});
        requestParams.put("persons[groupB][1].addresses[0].countryCode", new String[]{"GB"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/updatePersons4", requestParams);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController18.class);
        assertEquals("updatePersons4", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(LinkedHashMap.class, typedValues.get("persons").getClass());
        assertEquals("Michael", ((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[0].getForename());
        assertEquals("Delamere", ((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[0].getSurname());
        assertEquals(10, ((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[0].getAge());

        assertEquals(Locale.US, ((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[0].getLocale());
        assertNotNull(((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[0].getAddresses());
        assertEquals(1, ((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[0].getAddresses().size());

        Address shippingAddress = ((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[0].getAddresses().get(0);

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("234567890"), shippingAddress.getId());
        assertEquals(AddressType.SHIPPING, shippingAddress.getAddressTypes().get(0));
        assertEquals("Test Street 1", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Tom Checker", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("12345", shippingAddress.getZip());
        assertEquals("Washington", shippingAddress.getCity());
        assertEquals("US", shippingAddress.getCountryCode());

        assertEquals(Locale.GERMANY, ((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[1].getLocale());
        assertNotNull(((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[1].getAddresses());
        assertEquals(1, ((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[1].getAddresses().size());

        Address invoiceAddress = ((Map<String, Person[]>) typedValues.get("persons")).get("groupA")[1].getAddresses().get(0);

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("34567890"), invoiceAddress.getId());
        assertEquals(AddressType.INVOICE, invoiceAddress.getAddressTypes().get(0));
        assertEquals("Test Street 2", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Checker", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("67890", invoiceAddress.getZip());
        assertEquals("New York", invoiceAddress.getCity());
        assertEquals("US", invoiceAddress.getCountryCode());

        assertEquals("Lea", ((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[0].getForename());
        assertEquals("Cool", ((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[0].getSurname());
        assertEquals(8, ((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[0].getAge());

        assertEquals(Locale.GERMANY, ((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[0].getLocale());
        assertNotNull(((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[0].getAddresses());
        assertEquals(1, ((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[0].getAddresses().size());

        shippingAddress = ((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[0].getAddresses().get(0);

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("88888888"), shippingAddress.getId());
        assertEquals(AddressType.SHIPPING, shippingAddress.getAddressTypes().get(0));
        assertEquals("Test Straße 8", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Dude", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("88888", shippingAddress.getZip());
        assertEquals("Berlin", shippingAddress.getCity());
        assertEquals("DE", shippingAddress.getCountryCode());

        assertEquals(Locale.UK, ((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[1].getLocale());
        assertNotNull(((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[1].getAddresses());
        assertEquals(1, ((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[1].getAddresses().size());

        invoiceAddress = ((Map<String, Person[]>) typedValues.get("persons")).get("groupB")[1].getAddresses().get(0);

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("4444"), invoiceAddress.getId());
        assertEquals(AddressType.INVOICE, invoiceAddress.getAddressTypes().get(0));
        assertEquals("Test Avenue 4", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Lea Cool", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("KY11 9UH", invoiceAddress.getZip());
        assertEquals("Dalgety Bay", invoiceAddress.getCity());
        assertEquals("GB", invoiceAddress.getCountryCode());
    }

    @Test
    public void testFindController18h() {
        TypeConverterManager.register(Id.class, new IdConverter());

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("persons[0].forename", new String[]{"Michael"});
        requestParams.put("persons[0].surname", new String[]{"Delamere"});
        requestParams.put("persons[0].age", new String[]{"10"});
        requestParams.put("persons[0].locale", new String[]{"en_US"});
        requestParams.put("persons[0].addresses[0].id", new String[]{"234567890"});
        requestParams.put("persons[0].addresses[0].streetLines[]", new String[]{"Test Street 1", "c/o Tom Checker"});
        requestParams.put("persons[0].addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("persons[0].addresses[0].zip", new String[]{"12345"});
        requestParams.put("persons[0].addresses[0].city", new String[]{"Washington"});
        requestParams.put("persons[0].addresses[0].countryCode", new String[]{"us"});

        requestParams.put("persons[1].forename", new String[]{"Tom"});
        requestParams.put("persons[1].surname", new String[]{"Checker"});
        requestParams.put("persons[1].age", new String[]{"1"});
        requestParams.put("persons[1].locale", new String[]{"de_DE"});
        requestParams.put("persons[1].addresses[0].id", new String[]{"34567890"});
        requestParams.put("persons[1].addresses[0].streetLines[]", new String[]{"Test Street 2", "c/o Marc Checker"});
        requestParams.put("persons[1].addresses[0].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("persons[1].addresses[0].zip", new String[]{"67890"});
        requestParams.put("persons[1].addresses[0].city", new String[]{"New York"});
        requestParams.put("persons[1].addresses[0].countryCode", new String[]{"us"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/updatePersons5", requestParams);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController18.class);
        assertEquals("updatePersons5", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(Person[].class, typedValues.get("persons").getClass());
        assertTrue("isArray", typedValues.get("persons").getClass().isArray());
        assertEquals("Michael", ((Person[]) typedValues.get("persons"))[0].getForename());
        assertEquals("Delamere", ((Person[]) typedValues.get("persons"))[0].getSurname());
        assertEquals(10, ((Person[]) typedValues.get("persons"))[0].getAge());

        assertEquals(Locale.US, ((Person[]) typedValues.get("persons"))[0].getLocale());
        assertNotNull(((Person[]) typedValues.get("persons"))[0].getAddresses());
        assertEquals(1, ((Person[]) typedValues.get("persons"))[0].getAddresses().size());

        Address shippingAddress = ((Person[]) typedValues.get("persons"))[0].getAddresses().get(0);

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("234567890"), shippingAddress.getId());
        assertEquals(AddressType.SHIPPING, shippingAddress.getAddressTypes().get(0));
        assertEquals("Test Street 1", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Tom Checker", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("12345", shippingAddress.getZip());
        assertEquals("Washington", shippingAddress.getCity());
        assertEquals("us", shippingAddress.getCountryCode());

        assertEquals(Locale.GERMANY, ((Person[]) typedValues.get("persons"))[1].getLocale());
        assertNotNull(((Person[]) typedValues.get("persons"))[1].getAddresses());
        assertEquals(1, ((Person[]) typedValues.get("persons"))[1].getAddresses().size());

        Address invoiceAddress = ((Person[]) typedValues.get("persons"))[1].getAddresses().get(0);

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("34567890"), invoiceAddress.getId());
        assertEquals(AddressType.INVOICE, invoiceAddress.getAddressTypes().get(0));
        assertEquals("Test Street 2", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Checker", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("67890", invoiceAddress.getZip());
        assertEquals("New York", invoiceAddress.getCity());
        assertEquals("us", invoiceAddress.getCountryCode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController18i() {
        TypeConverterManager.register(Id.class, new IdConverter());

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("persons[0][groupA].forename", new String[]{"Michael"});
        requestParams.put("persons[0][groupA].surname", new String[]{"Delamere"});
        requestParams.put("persons[0][groupA].age", new String[]{"10"});
        requestParams.put("persons[0][groupA].locale", new String[]{"en_US"});
        requestParams.put("persons[0][groupA].addresses[0].id", new String[]{"234567890"});
        requestParams.put("persons[0][groupA].addresses[0].streetLines[]", new String[]{"Test Street 1", "c/o Tom Checker"});
        requestParams.put("persons[0][groupA].addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("persons[0][groupA].addresses[0].zip", new String[]{"12345"});
        requestParams.put("persons[0][groupA].addresses[0].city", new String[]{"Washington"});
        requestParams.put("persons[0][groupA].addresses[0].countryCode", new String[]{"US"});

        requestParams.put("persons[1][groupA].forename", new String[]{"Tom"});
        requestParams.put("persons[1][groupA].surname", new String[]{"Checker"});
        requestParams.put("persons[1][groupA].age", new String[]{"1"});
        requestParams.put("persons[1][groupA].locale", new String[]{"de_DE"});
        requestParams.put("persons[1][groupA].addresses[0].id", new String[]{"34567890"});
        requestParams.put("persons[1][groupA].addresses[0].streetLines[]", new String[]{"Test Street 2", "c/o Marc Checker"});
        requestParams.put("persons[1][groupA].addresses[0].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("persons[1][groupA].addresses[0].zip", new String[]{"67890"});
        requestParams.put("persons[1][groupA].addresses[0].city", new String[]{"New York"});
        requestParams.put("persons[1][groupA].addresses[0].countryCode", new String[]{"US"});

        requestParams.put("persons[0][groupB].forename", new String[]{"Lea"});
        requestParams.put("persons[0][groupB].surname", new String[]{"Cool"});
        requestParams.put("persons[0][groupB].age", new String[]{"8"});
        requestParams.put("persons[0][groupB].locale", new String[]{"de_DE"});
        requestParams.put("persons[0][groupB].addresses[0].id", new String[]{"88888888"});
        requestParams.put("persons[0][groupB].addresses[0].streetLines[]", new String[]{"Test Straße 8", "c/o Marc Dude"});
        requestParams.put("persons[0][groupB].addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("persons[0][groupB].addresses[0].zip", new String[]{"88888"});
        requestParams.put("persons[0][groupB].addresses[0].city", new String[]{"Berlin"});
        requestParams.put("persons[0][groupB].addresses[0].countryCode", new String[]{"DE"});

        requestParams.put("persons[1][groupB].forename", new String[]{"Marc"});
        requestParams.put("persons[1][groupB].surname", new String[]{"Dude"});
        requestParams.put("persons[1][groupB].age", new String[]{"4"});
        requestParams.put("persons[1][groupB].locale", new String[]{"en_GB"});
        requestParams.put("persons[1][groupB].addresses[0].id", new String[]{"4444"});
        requestParams.put("persons[1][groupB].addresses[0].streetLines[]", new String[]{"Test Avenue 4", "c/o Lea Cool"});
        requestParams.put("persons[1][groupB].addresses[0].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("persons[1][groupB].addresses[0].zip", new String[]{"KY11 9UH"});
        requestParams.put("persons[1][groupB].addresses[0].city", new String[]{"Dalgety Bay"});
        requestParams.put("persons[1][groupB].addresses[0].countryCode", new String[]{"GB"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/updatePersons6", requestParams);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController18.class);
        assertEquals("updatePersons6", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(ArrayList.class, typedValues.get("persons").getClass());
        assertEquals("Michael", ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupA").getForename());
        assertEquals("Delamere", ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupA").getSurname());
        assertEquals(10, ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupA").getAge());

        assertEquals(Locale.US, ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupA").getLocale());
        assertNotNull(((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupA").getAddresses());
        assertEquals(1, ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupA").getAddresses().size());

        Address shippingAddress = ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupA").getAddresses().get(0);

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("234567890"), shippingAddress.getId());
        assertEquals(AddressType.SHIPPING, shippingAddress.getAddressTypes().get(0));
        assertEquals("Test Street 1", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Tom Checker", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("12345", shippingAddress.getZip());
        assertEquals("Washington", shippingAddress.getCity());
        assertEquals("US", shippingAddress.getCountryCode());

        assertEquals(Locale.GERMANY, ((List<Map<String, Person>>) typedValues.get("persons")).get(1).get("groupA").getLocale());
        assertNotNull(((List<Map<String, Person>>) typedValues.get("persons")).get(1).get("groupA").getAddresses());
        assertEquals(1, ((List<Map<String, Person>>) typedValues.get("persons")).get(1).get("groupA").getAddresses().size());

        Address invoiceAddress = ((List<Map<String, Person>>) typedValues.get("persons")).get(1).get("groupA").getAddresses().get(0);

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("34567890"), invoiceAddress.getId());
        assertEquals(AddressType.INVOICE, invoiceAddress.getAddressTypes().get(0));
        assertEquals("Test Street 2", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Checker", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("67890", invoiceAddress.getZip());
        assertEquals("New York", invoiceAddress.getCity());
        assertEquals("US", invoiceAddress.getCountryCode());

        assertEquals("Lea", ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupB").getForename());
        assertEquals("Cool", ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupB").getSurname());
        assertEquals(8, ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupB").getAge());

        assertEquals(Locale.GERMANY, ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupB").getLocale());
        assertNotNull(((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupB").getAddresses());
        assertEquals(1, ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupB").getAddresses().size());

        shippingAddress = ((List<Map<String, Person>>) typedValues.get("persons")).get(0).get("groupB").getAddresses().get(0);

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("88888888"), shippingAddress.getId());
        assertEquals(AddressType.SHIPPING, shippingAddress.getAddressTypes().get(0));
        assertEquals("Test Straße 8", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Dude", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("88888", shippingAddress.getZip());
        assertEquals("Berlin", shippingAddress.getCity());
        assertEquals("DE", shippingAddress.getCountryCode());

        assertEquals(Locale.UK, ((List<Map<String, Person>>) typedValues.get("persons")).get(1).get("groupB").getLocale());
        assertNotNull(((List<Map<String, Person>>) typedValues.get("persons")).get(1).get("groupB").getAddresses());
        assertEquals(1, ((List<Map<String, Person>>) typedValues.get("persons")).get(1).get("groupB").getAddresses().size());

        invoiceAddress = ((List<Map<String, Person>>) typedValues.get("persons")).get(1).get("groupB").getAddresses().get(0);

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("4444"), invoiceAddress.getId());
        assertEquals(AddressType.INVOICE, invoiceAddress.getAddressTypes().get(0));
        assertEquals("Test Avenue 4", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Lea Cool", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("KY11 9UH", invoiceAddress.getZip());
        assertEquals("Dalgety Bay", invoiceAddress.getCity());
        assertEquals("GB", invoiceAddress.getCountryCode());
    }

    @Test
    public void testFindController18j() {
        TypeConverterManager.register(Id.class, new IdConverter());

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("person.forename", new String[]{"Michael"});
        requestParams.put("person.surname", new String[]{"Delamere"});
        requestParams.put("person.age", new String[]{"10"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/personInSession", requestParams);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler);
        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController18.class);
        assertEquals("sessionPerson", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(Person.class, typedValues.get("person").getClass());
        assertEquals("Michael", ((Person) typedValues.get("person")).getForename());
        assertEquals("Delamere", ((Person) typedValues.get("person")).getSurname());
        assertEquals(10, ((Person) typedValues.get("person")).getAge());

        // ----------------------------------------------------------------------------
        // Second request with same session
        // ----------------------------------------------------------------------------

        requestParams = new HashMap<>();
        requestParams.put("person.locale", new String[]{"en_US"});
        requestParams.put("person.addresses[0].id", new String[]{"234567890"});
        requestParams.put("person.addresses[0].streetLines[]", new String[]{"Test Street 1", "c/o Tom Checker"});
        requestParams.put("person.addresses[0].addressTypes[]", new String[]{"SHIPPING"});
        requestParams.put("person.addresses[0].zip", new String[]{"12345"});
        requestParams.put("person.addresses[0].city", new String[]{"Washington"});
        requestParams.put("person.addresses[0].countryCode", new String[]{"us"});

        reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/personInSession", requestParams);

        params = methodParams.get(requestHandler, reqCtx);
        requestValues = methodParams.values(params, reqCtx);
        typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(Person.class, typedValues.get("person").getClass());
        assertEquals("Michael", ((Person) typedValues.get("person")).getForename());
        assertEquals("Delamere", ((Person) typedValues.get("person")).getSurname());
        assertEquals(10, ((Person) typedValues.get("person")).getAge());

        assertEquals(Locale.US, ((Person) typedValues.get("person")).getLocale());
        assertNotNull(((Person) typedValues.get("person")).getAddresses());
        assertEquals(1, ((Person) typedValues.get("person")).getAddresses().size());

        Address shippingAddress = ((Person) typedValues.get("person")).getAddresses().stream().filter(a -> a.getAddressTypes().get(0) == AddressType.SHIPPING).findFirst().get();

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("234567890"), shippingAddress.getId());
        assertEquals("Test Street 1", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Tom Checker", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("12345", shippingAddress.getZip());
        assertEquals("Washington", shippingAddress.getCity());
        assertEquals("us", shippingAddress.getCountryCode());

        // ----------------------------------------------------------------------------
        // Third request with same session
        // ----------------------------------------------------------------------------

        requestParams = new HashMap<>();
        requestParams.put("person.addresses[1].id", new String[]{"34567890"});
        requestParams.put("person.addresses[1].streetLines[]", new String[]{"Test Street 2", "c/o Marc Checker"});
        requestParams.put("person.addresses[1].addressTypes[]", new String[]{"INVOICE"});
        requestParams.put("person.addresses[1].zip", new String[]{"67890"});
        requestParams.put("person.addresses[1].city", new String[]{"New York"});
        requestParams.put("person.addresses[1].countryCode", new String[]{"us"});

        reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller18/personInSession", requestParams);

        params = methodParams.get(requestHandler, reqCtx);
        requestValues = methodParams.values(params, reqCtx);
        typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertEquals(Person.class, typedValues.get("person").getClass());
        assertEquals("Michael", ((Person) typedValues.get("person")).getForename());
        assertEquals("Delamere", ((Person) typedValues.get("person")).getSurname());
        assertEquals(10, ((Person) typedValues.get("person")).getAge());

        assertEquals(Locale.US, ((Person) typedValues.get("person")).getLocale());
        assertNotNull(((Person) typedValues.get("person")).getAddresses());
        assertEquals(2, ((Person) typedValues.get("person")).getAddresses().size());

        shippingAddress = ((Person) typedValues.get("person")).getAddresses().stream().filter(a -> a.getAddressTypes().get(0) == AddressType.SHIPPING).findFirst().get();

        assertNotNull(shippingAddress);
        assertEquals(2, shippingAddress.getStreetLines().length);
        assertEquals(Long.valueOf("234567890"), shippingAddress.getId());
        assertEquals("Test Street 1", Array.get(shippingAddress.getStreetLines(), 0));
        assertEquals("c/o Tom Checker", Array.get(shippingAddress.getStreetLines(), 1));
        assertEquals("12345", shippingAddress.getZip());
        assertEquals("Washington", shippingAddress.getCity());
        assertEquals("us", shippingAddress.getCountryCode());

        Address invoiceAddress = ((Person) typedValues.get("person")).getAddresses().stream().filter(a -> a.getAddressTypes().get(0) == AddressType.INVOICE).findFirst().get();

        assertNotNull(invoiceAddress);

        assertEquals(2, invoiceAddress.getStreetLines().length);
        assertEquals(Long.valueOf("34567890"), invoiceAddress.getId());
        assertEquals("Test Street 2", Array.get(invoiceAddress.getStreetLines(), 0));
        assertEquals("c/o Marc Checker", Array.get(invoiceAddress.getStreetLines(), 1));
        assertEquals("67890", invoiceAddress.getZip());
        assertEquals("New York", invoiceAddress.getCity());
        assertEquals("us", invoiceAddress.getCountryCode());
    }
}
