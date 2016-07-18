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

package com.cb.geemvc.bind.param.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cb.geemvc.annotation.Adapter;
import com.cb.geemvc.bind.param.ParamAdapter;
import com.cb.geemvc.bind.param.ParamContext;
import com.cb.geemvc.bind.param.annotation.Cookie;

@Adapter
public class CookieParamAdapter implements ParamAdapter<Cookie> {
    @Override
    public String getName(Cookie cookieParam) {
        return cookieParam.value() == null ? cookieParam.name() : cookieParam.value();
    }

    @Override
    public List<String> getValue(Cookie cookieParam, String paramName, ParamContext paramCtx) {
        Map<String, String[]> cookieMap = paramCtx.requestCtx().getCookieMap();

        String[] values = cookieMap.get(paramName);

        return values == null ? null : Arrays.asList(values);
    }
}
