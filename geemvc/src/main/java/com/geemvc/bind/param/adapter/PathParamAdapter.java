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

package com.geemvc.bind.param.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.geemvc.annotation.Adapter;
import com.geemvc.bind.param.ParamAdapter;
import com.geemvc.bind.param.ParamContext;
import com.geemvc.bind.param.annotation.PathParam;

@Adapter
public class PathParamAdapter implements ParamAdapter<PathParam> {
    @Override
    public String getName(PathParam pathParam) {
        return pathParam.value();
    }

    @Override
    public List<String> getValue(PathParam pathParam, String paramName, ParamContext paramCtx) {
        Map<String, String[]> pathParameterMap = paramCtx.requestCtx().getPathParameters();

        String[] values = null;

        if (pathParameterMap != null && pathParameterMap.containsKey(paramName))
            values = pathParameterMap.get(paramName);

        return values == null ? null : Arrays.asList(values);
    }
}
