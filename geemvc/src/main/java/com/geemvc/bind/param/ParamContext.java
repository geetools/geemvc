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

package com.geemvc.bind.param;

import java.util.List;
import java.util.Map;

import com.geemvc.RequestContext;
import com.geemvc.bind.MethodParam;
import com.geemvc.handler.RequestHandler;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.validation.Errors;

public interface ParamContext {
    ParamContext build(MethodParam methodParam, Map<String, List<String>> requestValues, Map<String, Object> typedValues, RequestContext requestCtx, Errors errors, Notices notices);

    MethodParam methodParam();

    Map<String, List<String>> requestValues();

    Map<String, Object> typedValues();

    RequestContext requestCtx();

    RequestHandler requestHandler();

    Errors errors();

    Notices notices();
}
