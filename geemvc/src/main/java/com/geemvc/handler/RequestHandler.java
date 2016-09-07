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

package com.geemvc.handler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.geemvc.annotation.Request;
import com.geemvc.bind.MethodParam;
import com.geemvc.matcher.PathMatcher;

public interface RequestHandler extends Comparable<RequestHandler> {
    RequestHandler build(Class<?> controllerClass, Method handlerMethod);

    Class<?> controllerClass();

    Method handlerMethod();

    String name();

    RequestHandler name(String name);

    RequestHandler pathMatcher(PathMatcher pathMatcher);

    PathMatcher pathMatcher();

    Request controllerRequestMapping();

    Request handlerRequestMapping();

    List<MethodParam> methodParams();

    Object invoke(Map<String, Object> args);

    String toGenericString();
}
