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

package com.cb.geemvc.bind.param;

import java.util.List;
import java.util.Map;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.bind.MethodParam;
import com.cb.geemvc.handler.RequestHandler;

public class DefaultParamContext implements ParamContext {
    protected MethodParam methodParam;
    protected Map<String, List<String>> requestValues;
    protected Map<String, Object> typedValues;
    protected RequestContext requestCtx;

    @Override
    public ParamContext build(MethodParam methodParam, Map<String, List<String>> requestValues, Map<String, Object> typedValues, RequestContext requestCtx) {
        this.methodParam = methodParam;
        this.requestValues = requestValues;
        this.typedValues = typedValues;
        this.requestCtx = requestCtx;
        return this;
    }

    @Override
    public MethodParam methodParam() {
        return methodParam;
    }

    @Override
    public Map<String, List<String>> requestValues() {
        return requestValues;
    }

    @Override
    public Map<String, Object> typedValues() {
        return typedValues;
    }

    @Override
    public RequestContext requestCtx() {
        return requestCtx;
    }

    @Override
    public RequestHandler requestHandler() {
        return requestCtx.requestHandler();
    }
}
