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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cb.geemvc.Char;
import com.cb.geemvc.Str;
import com.cb.geemvc.annotation.Adapter;
import com.cb.geemvc.bind.param.ParamAdapter;
import com.cb.geemvc.bind.param.ParamContext;
import com.cb.geemvc.bind.param.annotation.Model;

@Adapter
public class ModelParamAdapter implements ParamAdapter<Model> {
    @Override
    public String getName(Model modelParam) {
        return modelParam.value() == null ? modelParam.name() : modelParam.value();
    }

    @Override
    public List<String> getValue(Model modelParam, String paramName, ParamContext paramCtx) {
        List<String> values = new ArrayList<>();

        Map<String, String[]> paramMap = paramCtx.requestCtx().getParameterMap();

        Set<String> keys = paramMap.keySet();

        for (String key : keys) {
            if (key.startsWith(new StringBuilder(paramName).append(Str.DOT).toString()) || key.startsWith(new StringBuilder(paramName).append(Str.SQUARE_BRACKET_OPEN).toString())) {
                String[] reqValues = paramMap.get(key);

                for (String val : reqValues) {
                    values.add(new StringBuilder(key).append(Char.EQUALS).append(val).toString());
                }
            }
        }

        return values == null || values.size() == 0 ? null : values;
    }
}
