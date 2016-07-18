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

import java.util.List;

import com.cb.geemvc.annotation.Adapter;
import com.cb.geemvc.bind.param.ParamAdapter;
import com.cb.geemvc.bind.param.ParamAdapters;
import com.cb.geemvc.bind.param.ParamContext;
import com.cb.geemvc.bind.param.annotation.Param;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Adapter
public class RequestParamAdapter implements ParamAdapter<Param> {

    @Inject
    Injector injector;

    @Override
    public String getName(Param requestParam) {
        return requestParam.value() == null ? requestParam.name() : requestParam.value();
    }

    @Override
    public List<String> getValue(Param requestParam, String paramName, ParamContext paramCtx) {
        return injector.getInstance(ParamAdapters.class).getRequestValues(paramName, paramCtx.requestCtx());
    }
}
