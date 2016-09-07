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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.geemvc.Str;
import com.geemvc.annotation.Request;
import com.geemvc.bind.MethodParam;
import com.geemvc.helper.Annotations;
import com.geemvc.matcher.PathMatcher;
import com.google.inject.Inject;
import com.google.inject.Injector;

import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;

public class DefaultRequestHandler implements RequestHandler {
    protected Class<?> controllerClass = null;
    protected Method method = null;
    protected String name = null;
    protected PathMatcher pathMatcher = null;
    protected List<MethodParam> methodParams = null;

    protected boolean isInitialized = false;

    @Inject
    protected Injector injector;

    public RequestHandler build(Class<?> controllerClass, Method method) {
        if (!isInitialized) {
            this.controllerClass = controllerClass;
            this.method = method;

            isInitialized = true;
        } else {
            throw new RuntimeException("RequestHandler.build() can only be called once");
        }

        return this;
    }

    @Override
    public Class<?> controllerClass() {
        return controllerClass;
    }

    @Override
    public Method handlerMethod() {
        return method;
    }

    public String name() {
        return name;
    }

    public RequestHandler name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public RequestHandler pathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
        return this;
    }

    @Override
    public PathMatcher pathMatcher() {
        return pathMatcher;
    }

    @Override
    public Request controllerRequestMapping() {
        if (controllerClass.isAnnotationPresent(Request.class)
                || controllerClass.isAnnotationPresent(Path.class)
                || controllerClass.isAnnotationPresent(GET.class)
                || controllerClass.isAnnotationPresent(POST.class)
                || controllerClass.isAnnotationPresent(PUT.class)
                || controllerClass.isAnnotationPresent(DELETE.class)
                || controllerClass.isAnnotationPresent(HEAD.class)
                || controllerClass.isAnnotationPresent(OPTIONS.class)) {
            return injector.getInstance(Annotations.class).requestMapping(controllerClass);
        }

        return null;
    }

    @Override
    public Request handlerRequestMapping() {
        if (method.isAnnotationPresent(Request.class)
                || method.isAnnotationPresent(Path.class)
                || method.isAnnotationPresent(GET.class)
                || method.isAnnotationPresent(POST.class)
                || method.isAnnotationPresent(PUT.class)
                || method.isAnnotationPresent(DELETE.class)
                || method.isAnnotationPresent(HEAD.class)
                || method.isAnnotationPresent(OPTIONS.class)) {
            return injector.getInstance(Annotations.class).requestMapping(method);
        }

        return null;
    }

    @Override
    public List<MethodParam> methodParams() {
        if (methodParams == null) {
            methodParams = new ArrayList<>();

            Method m = handlerMethod();
            Parameter[] parameters = m.getParameters();

            if (parameters.length > 0) {
                MethodParameter[] mParameters = Paramo.resolveParameters(m);

                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    MethodParameter mParameter = mParameters[i];

                    methodParams.add(injector.getInstance(MethodParam.class).build(mParameter.getName(), parameter.getType(), parameter.getParameterizedType(), parameter.getAnnotations()));
                }
            }
        }

        return methodParams;
    }

    @Override
    public Object invoke(Map<String, Object> parameters) {
        try {
            Object[] args = null;

            if (parameters != null && !parameters.isEmpty())
                args = parameters.values().toArray();

            return method.invoke(injector == null ? controllerClass.newInstance() : injector.getInstance(controllerClass), args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException("Unable to invoke request handler: " + toGenericString() + ". " + e.getMessage(), e);
        }
    }

    @Override
    public int compareTo(RequestHandler rh) {
        int p1 = handlerRequestMapping().priority();
        int p2 = rh.handlerRequestMapping().priority();

        if (p1 > p2) {
            return 1;
        } else if (p1 < p2) {
            return -1;
        }

        return 0;
    }

    @Override
    public String toString() {
        return controllerClass().getName() + Str.HASH + handlerMethod().getName() + Str.BRACKET_OPEN_CLOSE;
    }

    @Override
    public String toGenericString() {
        return handlerMethod().toGenericString();
    }
}
