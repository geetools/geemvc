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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.geemvc.handler.CompositeControllerResolver;
import com.geemvc.handler.CompositeHandlerResolver;
import com.geemvc.handler.RequestHandler;
import com.geemvc.mock.controller.TestController17;
import com.geemvc.mock.converter.IdConverter;
import com.geemvc.mock.type.StringConstructorType;
import jodd.typeconverter.TypeConverterManager;

import org.junit.Assert;
import org.junit.Test;

import com.geemvc.RequestContext;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.mock.Id;
import com.geemvc.mock.type.FromStringType;
import com.geemvc.mock.type.ValueOfType;
import com.geemvc.test.BaseTest;

public class ParamBindingTest extends BaseTest {
    @Test
    public void testFindController17a() {
        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17a/12345");

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17a", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("id", requestValues.containsKey("id"));
        assertTrue("id", typedValues.containsKey("id"));
        assertEquals("12345", requestValues.get("id").get(0));
        assertEquals(12345L, typedValues.get("id"));
        assertEquals(Long.class, typedValues.get("id").getClass());
    }

    @Test
    public void testFindController17b() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("id", new String[]{"12345"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17b", requestParams);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17b", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("id", requestValues.containsKey("id"));
        assertTrue("id", typedValues.containsKey("id"));
        assertEquals("12345", requestValues.get("id").get(0));
        assertEquals(12345L, typedValues.get("id"));
        assertEquals(Long.class, typedValues.get("id").getClass());
    }

    @Test
    public void testFindController17c() {
        Map<String, String[]> headers = new HashMap<>();
        headers.put("id", new String[]{"12345"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17c", (Map<String, String[]>) null, headers);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17c", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("id", requestValues.containsKey("id"));
        assertTrue("id", typedValues.containsKey("id"));
        assertEquals("12345", requestValues.get("id").get(0));
        assertEquals(12345L, typedValues.get("id"));
        assertEquals(Long.class, typedValues.get("id").getClass());
    }

    @Test
    public void testFindController17d() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("id", "12345"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17d", cookies);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17d", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("id", requestValues.containsKey("id"));
        assertTrue("id", typedValues.containsKey("id"));
        assertEquals("12345", requestValues.get("id").get(0));
        assertEquals(12345L, typedValues.get("id"));
        assertEquals(Long.class, typedValues.get("id").getClass());
    }

    @Test
    public void testFindController17e() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("price", new String[]{"19.99"});

        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"application/json"});

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("rememberMe", "true"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17e/12345", "GET", requestParams, headers, cookies);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        assertNotNull(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17e", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(4, params.size());
        assertEquals(4, requestValues.size());
        assertEquals(4, typedValues.size());
        assertTrue("id", requestValues.containsKey("id"));
        assertTrue("price", requestValues.containsKey("price"));
        assertTrue("Accept", requestValues.containsKey("Accept"));
        assertTrue("rememberMe", requestValues.containsKey("rememberMe"));
        assertTrue("id", typedValues.containsKey("id"));
        assertTrue("price", typedValues.containsKey("price"));
        assertTrue("Accept", typedValues.containsKey("Accept"));
        assertTrue("rememberMe", typedValues.containsKey("rememberMe"));
        assertEquals("12345", requestValues.get("id").get(0));
        assertEquals("19.99", requestValues.get("price").get(0));
        assertEquals("application/json", requestValues.get("Accept").get(0));
        assertEquals("true", requestValues.get("rememberMe").get(0));
        assertEquals(12345, typedValues.get("id"));
        assertEquals(BigDecimal.valueOf(19.99d), typedValues.get("price"));
        assertEquals("application/json", typedValues.get("Accept"));
        assertEquals(true, typedValues.get("rememberMe"));
        assertEquals(Integer.class, typedValues.get("id").getClass());
        assertEquals(BigDecimal.class, typedValues.get("price").getClass());
        assertEquals(String.class, typedValues.get("Accept").getClass());
        assertEquals(Boolean.class, typedValues.get("rememberMe").getClass());
    }

    @Test
    public void testFindController17f() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("ids", new String[]{"1", "2", "3"});
        requestParams.put("tags", new String[]{"t1", "t2", "t3"});
        requestParams.put("tags2", new String[]{"t21", "t22", "t23"});
        requestParams.put("prices", new String[]{"10.10", "20.20", "30.30",});
        requestParams.put("special-prices", new String[]{"1.10", "2.20", "3.30",});

        Map<String, String[]> headers = new HashMap<>();
        headers.put("Accept", new String[]{"text/html", "application/json", "application/xml"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17f", "GET", requestParams, headers);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        assertNotNull(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17f", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(6, params.size());
        assertEquals(6, requestValues.size());
        assertEquals(6, typedValues.size());
        assertTrue("ids", requestValues.containsKey("ids"));
        assertTrue("tags", requestValues.containsKey("tags"));
        assertTrue("tags2", requestValues.containsKey("tags2"));
        assertTrue("prices", requestValues.containsKey("prices"));
        assertTrue("special-prices", requestValues.containsKey("special-prices"));
        assertTrue("Accept", requestValues.containsKey("Accept"));
        assertTrue("ids", typedValues.containsKey("ids"));
        assertTrue("tags", typedValues.containsKey("tags"));
        assertTrue("tags2", typedValues.containsKey("tags2"));
        assertTrue("prices", typedValues.containsKey("prices"));
        assertTrue("special-prices", typedValues.containsKey("special-prices"));
        assertTrue("Accept", typedValues.containsKey("Accept"));

        assertEquals(Arrays.asList(new String[]{"1", "2", "3"}), requestValues.get("ids"));
        assertEquals(Arrays.asList(new String[]{"t1", "t2", "t3"}), requestValues.get("tags"));
        assertEquals(Arrays.asList(new String[]{"t21", "t22", "t23"}), requestValues.get("tags2"));
        assertEquals(Arrays.asList(new String[]{"10.10", "20.20", "30.30"}), requestValues.get("prices"));
        assertEquals(Arrays.asList(new String[]{"1.10", "2.20", "3.30"}), requestValues.get("special-prices"));
        assertEquals(Arrays.asList(new String[]{"text/html", "application/json", "application/xml"}), requestValues.get("Accept"));

        assertTrue(Arrays.equals(new int[]{1, 2, 3}, (int[]) typedValues.get("ids")));
        assertTrue(Arrays.equals(new String[]{"t1", "t2", "t3"}, (String[]) typedValues.get("tags")));
        assertEquals(Arrays.asList(new String[]{"t21", "t22", "t23"}), typedValues.get("tags2"));
        assertArrayEquals(new BigDecimal[]{new BigDecimal("10.10"), new BigDecimal("20.20"), new BigDecimal("30.30")}, (BigDecimal[]) typedValues.get("prices"));
        assertEquals(new ArrayList<Double>(Arrays.asList(new Double[]{Double.valueOf(1.10), Double.valueOf(2.20), Double.valueOf(3.30)})), typedValues.get("special-prices"));
        assertEquals(new LinkedHashSet<String>(Arrays.asList(new String[]{"text/html", "application/json", "application/xml"})), typedValues.get("Accept"));

        assertEquals(int[].class, typedValues.get("ids").getClass());
        assertEquals(String[].class, typedValues.get("tags").getClass());
        assertEquals(ArrayList.class, typedValues.get("tags2").getClass());
        assertEquals(BigDecimal[].class, typedValues.get("prices").getClass());
        assertEquals(ArrayList.class, typedValues.get("special-prices").getClass());
        assertEquals(LinkedHashSet.class, typedValues.get("Accept").getClass());
    }

    @Test
    public void testFindController17g() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("param2", new String[]{"val2"});

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("param3", "val3"));

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17g/val1", "GET", requestParams, null, cookies);

        CompositeHandlerResolver compositeHandlerResolver = instance(CompositeHandlerResolver.class);
        CompositeControllerResolver controllerResolver = instance(CompositeControllerResolver.class);
        MethodParams methodParams = instance(MethodParams.class);

        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(reqCtx);
        RequestHandler requestHandler = compositeHandlerResolver.resolve(reqCtx, controllers.values());
        reqCtx.requestHandler(requestHandler);

        assertNotNull(requestHandler);

        List<MethodParam> params = methodParams.get(requestHandler, reqCtx);
        Map<String, List<String>> requestValues = methodParams.values(params, reqCtx);
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, params, reqCtx);

        assertNotNull(requestHandler.handlerMethod());
        assertNotNull(requestHandler.pathMatcher());
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17g", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(3, params.size());
        assertEquals(3, requestValues.size());
        assertEquals(3, typedValues.size());
        assertTrue("param1", requestValues.containsKey("param1"));
        assertTrue("param2", requestValues.containsKey("param2"));
        assertTrue("param3", requestValues.containsKey("param3"));
        assertTrue("param1", typedValues.containsKey("param1"));
        assertTrue("param2", typedValues.containsKey("param2"));
        assertTrue("param3", typedValues.containsKey("param3"));
        assertEquals("val1", requestValues.get("param1").get(0));
        assertEquals("val2", requestValues.get("param2").get(0));
        assertEquals("val3", requestValues.get("param3").get(0));
        assertEquals(ValueOfType.valueOf("val1"), typedValues.get("param1"));
        assertEquals(FromStringType.fromString("val2"), typedValues.get("param2"));
        Assert.assertEquals(new StringConstructorType("val3"), typedValues.get("param3"));
        assertEquals(ValueOfType.class, typedValues.get("param1").getClass());
        assertEquals(FromStringType.class, typedValues.get("param2").getClass());
        assertEquals(StringConstructorType.class, typedValues.get("param3").getClass());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController17h() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("myMap[keyOne]", new String[]{"valueOne"});
        requestParams.put("myMap[keyTwo]", new String[]{"valueTwo"});
        requestParams.put("myMap[keyThree]", new String[]{"valueThree"});
        requestParams.put("myMap[keyFour]", new String[]{"valueFour"});
        requestParams.put("myMap[keyFive]", new String[]{"valueFive"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17h", requestParams);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17h", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("myMap", requestValues.containsKey("myMap"));
        assertTrue("myMap", typedValues.containsKey("myMap"));
        assertEquals(5, ((List<String>) requestValues.get("myMap")).size());
        assertEquals(5, ((Map<String, Object>) typedValues.get("myMap")).size());
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"keyOne=valueOne", "keyTwo=valueTwo", "keyThree=valueThree", "keyFour=valueFour", "keyFive=valueFive"})), new HashSet<>(requestValues.get("myMap")));

        Map<String, String> expectedMap = new LinkedHashMap<>();
        expectedMap.put("keyOne", "valueOne");
        expectedMap.put("keyTwo", "valueTwo");
        expectedMap.put("keyThree", "valueThree");
        expectedMap.put("keyFour", "valueFour");
        expectedMap.put("keyFive", "valueFive");

        assertEquals(expectedMap, typedValues.get("myMap"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController17i() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("myMap[keyOne]", new String[]{"valueOneA", "valueOneB", "valueOneC"});
        requestParams.put("myMap[keyTwo]", new String[]{"valueTwoA", "valueTwoB", "valueTwoC"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17i", requestParams);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17i", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("myMap", requestValues.containsKey("myMap"));
        assertTrue("myMap", typedValues.containsKey("myMap"));
        assertEquals(6, ((List<String>) requestValues.get("myMap")).size());
        assertEquals(2, ((Map<String, Object>) typedValues.get("myMap")).size());
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"keyOne=valueOneA", "keyOne=valueOneB", "keyOne=valueOneC", "keyTwo=valueTwoA", "keyTwo=valueTwoB", "keyTwo=valueTwoC"})), new HashSet<>(requestValues.get("myMap")));
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"valueOneA", "valueOneB", "valueOneC"})), new HashSet<>(Arrays.asList((String[]) ((Map<String, Object>) typedValues.get("myMap")).get("keyOne"))));
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"valueTwoA", "valueTwoB", "valueTwoC"})), new HashSet<>(Arrays.asList((String[]) ((Map<String, Object>) typedValues.get("myMap")).get("keyTwo"))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController17j() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("myMap[keyOne]", new String[]{"valueOneA", "valueOneB", "valueOneC"});
        requestParams.put("myMap[keyTwo]", new String[]{"valueTwoA", "valueTwoB", "valueTwoC"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17j", requestParams);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17j", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("myMap", requestValues.containsKey("myMap"));
        assertTrue("myMap", typedValues.containsKey("myMap"));
        assertEquals(6, ((List<String>) requestValues.get("myMap")).size());
        assertEquals(2, ((Map<String, Object>) typedValues.get("myMap")).size());
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"keyOne=valueOneA", "keyOne=valueOneB", "keyOne=valueOneC", "keyTwo=valueTwoA", "keyTwo=valueTwoB", "keyTwo=valueTwoC"})), new HashSet<>(requestValues.get("myMap")));
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"valueOneA", "valueOneB", "valueOneC"})), new HashSet<>((Collection<?>) ((Map<String, Object>) typedValues.get("myMap")).get("keyOne")));
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"valueTwoA", "valueTwoB", "valueTwoC"})), new HashSet<>((Collection<?>) ((Map<String, Object>) typedValues.get("myMap")).get("keyTwo")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController17k() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("myMap[keyOne]", new String[]{"11", "12", "13"});
        requestParams.put("myMap[keyTwo]", new String[]{"21", "22", "23"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17k", requestParams);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17k", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("myMap", requestValues.containsKey("myMap"));
        assertTrue("myMap", typedValues.containsKey("myMap"));
        assertEquals(6, ((List<String>) requestValues.get("myMap")).size());
        assertEquals(2, ((Map<String, Object>) typedValues.get("myMap")).size());
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"keyOne=11", "keyOne=12", "keyOne=13", "keyTwo=21", "keyTwo=22", "keyTwo=23"})), new HashSet<>(requestValues.get("myMap")));
        assertEquals(new HashSet<>(Arrays.asList(new Integer[]{11, 12, 13})), new HashSet<>((Collection<?>) ((Map<String, Object>) typedValues.get("myMap")).get("keyOne")));
        assertEquals(new HashSet<>(Arrays.asList(new Integer[]{21, 22, 23})), new HashSet<>((Collection<?>) ((Map<String, Object>) typedValues.get("myMap")).get("keyTwo")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController17l() {
        TypeConverterManager.register(Id.class, new IdConverter());

        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("myMap[12345]", new String[]{"String value for id 12345."});
        requestParams.put("myMap[67890]", new String[]{"String value for id 67890."});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17l", requestParams);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17l", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("myMap", requestValues.containsKey("myMap"));
        assertTrue("myMap", typedValues.containsKey("myMap"));
        assertEquals(2, ((List<String>) requestValues.get("myMap")).size());
        assertEquals(2, ((Map<String, Object>) typedValues.get("myMap")).size());
        assertEquals(new HashSet<>(Arrays.asList(new Id[]{Id.valueOf("12345"), Id.valueOf("67890")})), ((Map<String, Object>) typedValues.get("myMap")).keySet());
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"12345=String value for id 12345.", "67890=String value for id 67890."})), new HashSet<>(requestValues.get("myMap")));
        assertEquals("String value for id 12345.", (String) ((Map<String, Object>) typedValues.get("myMap")).get(Id.valueOf("12345")));
        assertEquals("String value for id 67890.", (String) ((Map<String, Object>) typedValues.get("myMap")).get(Id.valueOf("67890")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController17m() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("myMap[12345]", new String[]{"11.11", "22.22", "33.33"});
        requestParams.put("myMap[67890]", new String[]{"111.111", "222.222", "333.333"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17m", requestParams);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17m", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("myMap", requestValues.containsKey("myMap"));
        assertTrue("myMap", typedValues.containsKey("myMap"));
        assertEquals(6, ((List<String>) requestValues.get("myMap")).size());
        assertEquals(2, ((Map<String, Object>) typedValues.get("myMap")).size());
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"12345=11.11", "12345=22.22", "12345=33.33", "67890=111.111", "67890=222.222", "67890=333.333"})), new HashSet<>(requestValues.get("myMap")));
        assertEquals(new HashSet<>(Arrays.asList(new Double[]{11.11d, 22.22d, 33.33d})), new HashSet<>(Arrays.asList((Double[]) ((Map<String, Object>) typedValues.get("myMap")).get(Id.valueOf("12345")))));
        assertEquals(new HashSet<>(Arrays.asList(new Double[]{111.111d, 222.222d, 333.333d})), new HashSet<>(Arrays.asList((Double[]) ((Map<String, Object>) typedValues.get("myMap")).get(Id.valueOf("67890")))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController17n() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("myMap1[12345]", new String[]{"11", "12", "13"});
        requestParams.put("myMap1[67890]", new String[]{"21", "22", "23"});
        requestParams.put("myMap2[123450]", new String[]{"110", "120", "130"});
        requestParams.put("myMap2[678900]", new String[]{"210", "220", "230"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17n", requestParams);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17n", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(2, params.size());
        assertEquals(2, requestValues.size());
        assertEquals(2, typedValues.size());
        assertTrue("myMap1", requestValues.containsKey("myMap1"));
        assertTrue("myMap1", typedValues.containsKey("myMap1"));
        assertTrue("myMap2", requestValues.containsKey("myMap2"));
        assertTrue("myMap2", typedValues.containsKey("myMap2"));
        assertEquals(6, ((List<String>) requestValues.get("myMap1")).size());
        assertEquals(6, ((List<String>) requestValues.get("myMap2")).size());
        assertEquals(2, ((Map<String, Object>) typedValues.get("myMap1")).size());
        assertEquals(2, ((Map<String, Object>) typedValues.get("myMap2")).size());
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"12345=11", "12345=12", "12345=13", "67890=21", "67890=22", "67890=23"})), new HashSet<>(requestValues.get("myMap1")));
        assertEquals(new HashSet<>(Arrays.asList(new Id[]{Id.valueOf("11"), Id.valueOf("12"), Id.valueOf("13")})), new HashSet<>(Arrays.asList((Id[]) ((Map<String, Object>) typedValues.get("myMap1")).get(12345l))));
        assertEquals(new HashSet<>(Arrays.asList(new Id[]{Id.valueOf("21"), Id.valueOf("22"), Id.valueOf("23")})), new HashSet<>(Arrays.asList((Id[]) ((Map<String, Object>) typedValues.get("myMap1")).get(67890l))));
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"123450=110", "123450=120", "123450=130", "678900=210", "678900=220", "678900=230"})), new HashSet<>(requestValues.get("myMap2")));
        assertEquals(new HashSet<>(Arrays.asList(new Id[]{Id.valueOf("110"), Id.valueOf("120"), Id.valueOf("130")})), new HashSet<>((List<Id>) ((Map<String, Object>) typedValues.get("myMap2")).get(123450l)));
        assertEquals(new HashSet<>(Arrays.asList(new Id[]{Id.valueOf("210"), Id.valueOf("220"), Id.valueOf("230")})), new HashSet<>((List<Id>) ((Map<String, Object>) typedValues.get("myMap2")).get(678900l)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController17o() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("myListOfMaps[0][keyOneA]", new String[]{"valueOneA0"});
        requestParams.put("myListOfMaps[0][keyOneB]", new String[]{"valueOneB0"});
        requestParams.put("myListOfMaps[0][keyOneC]", new String[]{"valueOneC0"});
        requestParams.put("myListOfMaps[1][keyOneA]", new String[]{"valueOneA1"});
        requestParams.put("myListOfMaps[1][keyOneB]", new String[]{"valueOneB1"});
        requestParams.put("myListOfMaps[1][keyOneC]", new String[]{"valueOneC1"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17o", requestParams);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17o", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("myListOfMaps", requestValues.containsKey("myListOfMaps"));
        assertTrue("myListOfMaps", typedValues.containsKey("myListOfMaps"));
        assertEquals(6, ((List<String>) requestValues.get("myListOfMaps")).size());
        assertEquals(2, ((List<Map<String, Object>>) typedValues.get("myListOfMaps")).size());

        assertEquals(new HashSet<>(Arrays.asList(new String[]{"[0][keyOneA]=valueOneA0", "[0][keyOneB]=valueOneB0", "[0][keyOneC]=valueOneC0", "[1][keyOneA]=valueOneA1", "[1][keyOneB]=valueOneB1", "[1][keyOneC]=valueOneC1"})), new HashSet<>(
                requestValues.get("myListOfMaps")));
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"valueOneA0", "valueOneB0", "valueOneC0"})), new HashSet<>((Collection<?>) ((List<Map<String, String>>) typedValues.get("myListOfMaps")).get(0).values()));
        assertEquals(new HashSet<>(Arrays.asList(new String[]{"valueOneA1", "valueOneB1", "valueOneC1"})), new HashSet<>((Collection<?>) ((List<Map<String, String>>) typedValues.get("myListOfMaps")).get(1).values()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindController17p() {
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("myListOfMaps[0][111]", new String[]{"110"});
        requestParams.put("myListOfMaps[0][222]", new String[]{"220"});
        requestParams.put("myListOfMaps[0][333]", new String[]{"330"});
        requestParams.put("myListOfMaps[1][111]", new String[]{"119"});
        requestParams.put("myListOfMaps[1][222]", new String[]{"229"});
        requestParams.put("myListOfMaps[1][333]", new String[]{"339"});

        RequestContext reqCtx = newRequestContext("/webapp", "/servlet", "/webapp/servlet/controller17/handler17p", requestParams);

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
        assertEquals(requestHandler.controllerClass(), TestController17.class);
        assertEquals("handler17p", requestHandler.handlerMethod().getName());
        assertNotNull(params);
        assertNotNull(requestValues);
        assertNotNull(typedValues);
        assertEquals(1, params.size());
        assertEquals(1, requestValues.size());
        assertEquals(1, typedValues.size());
        assertTrue("myListOfMaps", requestValues.containsKey("myListOfMaps"));
        assertTrue("myListOfMaps", typedValues.containsKey("myListOfMaps"));
        assertEquals(6, ((List<String>) requestValues.get("myListOfMaps")).size());
        assertEquals(2, ((List<Map<String, Object>>) typedValues.get("myListOfMaps")).size());

        assertEquals(new HashSet<>(Arrays.asList(new String[]{"[0][111]=110", "[0][222]=220", "[0][333]=330", "[1][111]=119", "[1][222]=229", "[1][333]=339"})), new HashSet<>(requestValues.get("myListOfMaps")));
        assertEquals(new HashSet<>(Arrays.asList(new Integer[]{110, 220, 330})), new HashSet<>((Collection<?>) ((List<Map<Id, Integer>>) typedValues.get("myListOfMaps")).get(0).values()));
        assertEquals(new HashSet<>(Arrays.asList(new Integer[]{119, 229, 339})), new HashSet<>((Collection<?>) ((List<Map<String, String>>) typedValues.get("myListOfMaps")).get(1).values()));
    }
}
