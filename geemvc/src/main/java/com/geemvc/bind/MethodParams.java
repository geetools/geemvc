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

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.geemvc.RequestContext;
import com.geemvc.bind.param.ParamAdapter;
import com.geemvc.handler.RequestHandler;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.validation.Errors;

public interface MethodParams {
    List<MethodParam> get(RequestHandler requestHandler, RequestContext requestContext);

    Map<String, List<String>> values(List<MethodParam> methodParams, RequestContext requestContex, Errors errors, Notices noticest);

    Map<String, Object> typedValues(Map<String, List<String>> requestValues, List<MethodParam> methodParams, RequestContext requestCtx, Errors errors, Notices notices);

    String name(ParamAdapter<Annotation> paramAdapter, Annotation paramAnnotation, MethodParam methodParam);
}
