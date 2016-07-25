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

import org.junit.Test;

import com.geemvc.bind.param.ParamAdapter;
import com.geemvc.bind.param.ParamAdapterFactory;
import com.geemvc.bind.param.adapter.CookieParamAdapter;
import com.geemvc.bind.param.adapter.HeaderParamAdapter;
import com.geemvc.bind.param.adapter.PathParamAdapter;
import com.geemvc.bind.param.adapter.RequestParamAdapter;
import com.geemvc.bind.param.annotation.Cookie;
import com.geemvc.bind.param.annotation.Header;
import com.geemvc.bind.param.annotation.Param;
import com.geemvc.bind.param.annotation.PathParam;
import com.geemvc.test.BaseTest;

public class ParamAdapterTest extends BaseTest {
    @Test
    public void testPathVariableAdapter() {
        ParamAdapterFactory paramAdapterFactory = instance(ParamAdapterFactory.class);
        ParamAdapter<PathParam> pathVariableAdapter = paramAdapterFactory.create(PathParam.class);

        assertNotNull(pathVariableAdapter);
        assertEquals(PathParamAdapter.class, pathVariableAdapter.getClass());
    }

    @Test
    public void testRequestParamAdapter() {
        ParamAdapterFactory paramAdapterFactory = instance(ParamAdapterFactory.class);
        ParamAdapter<Param> requestParamAdapter = paramAdapterFactory.create(Param.class);

        assertNotNull(requestParamAdapter);
        assertEquals(RequestParamAdapter.class, requestParamAdapter.getClass());
    }

    @Test
    public void testRequestHeaderAdapter() {
        ParamAdapterFactory paramAdapterFactory = instance(ParamAdapterFactory.class);
        ParamAdapter<Header> requestHeaderAdapter = paramAdapterFactory.create(Header.class);

        assertNotNull(requestHeaderAdapter);
        assertEquals(HeaderParamAdapter.class, requestHeaderAdapter.getClass());
    }

    @Test
    public void testCookieValueAdapter() {
        ParamAdapterFactory paramAdapterFactory = instance(ParamAdapterFactory.class);
        ParamAdapter<Cookie> cookieValueAdapter = paramAdapterFactory.create(Cookie.class);

        assertNotNull(cookieValueAdapter);
        assertEquals(CookieParamAdapter.class, cookieValueAdapter.getClass());
    }

}
