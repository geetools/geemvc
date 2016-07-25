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

package com.geemvc.validation;

import com.geemvc.annotation.Request;
import com.geemvc.bind.MethodParam;
import com.geemvc.handler.HandlerResolverStats;
import com.geemvc.handler.RequestHandler;
import com.geemvc.matcher.PathMatcher;
import com.geemvc.view.bean.View;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 13.07.2016.
 */
public class DefaultViewOnlyRequestHandler implements ViewOnlyRequestHandler {

    protected View view;
    protected RequestHandler requestHandler;

    @Override
    public RequestHandler build(View view, RequestHandler requestHandler) {
        this.view = view;
        this.requestHandler = requestHandler;

        if (view == null || requestHandler == null)
            throw new NullPointerException();

        return this;
    }

    @Override
    public RequestHandler build(Class<?> controllerClass, Method handlerMethod, String consumes, String produces) {
        throw new IllegalStateException("The ViewOnlyRequestHandler does not support this build method");
    }

    @Override
    public Class<?> controllerClass() {
        return requestHandler.controllerClass();
    }

    @Override
    public Method handlerMethod() {
        return requestHandler.handlerMethod();
    }

    @Override
    public String name() {
        return requestHandler.name();
    }

    @Override
    public RequestHandler name(String name) {
        requestHandler.name(name);
        return this;
    }

    @Override
    public String consumes() {
        return requestHandler.consumes();
    }

    @Override
    public String produces() {
        return requestHandler.produces();
    }

    @Override
    public RequestHandler pathMatcher(PathMatcher pathMatcher) {
        requestHandler.pathMatcher(pathMatcher);
        return this;
    }

    @Override
    public PathMatcher pathMatcher() {
        return requestHandler.pathMatcher();
    }

    @Override
    public Collection<String> resolvedParameters() {
        return requestHandler.resolvedParameters();
    }

    @Override
    public RequestHandler resolvedParameters(Collection<String> resolvedParameters) {
        return requestHandler.resolvedCookies(resolvedParameters);
    }

    @Override
    public Collection<String> resolvedHeaders() {
        return requestHandler.resolvedHeaders();
    }

    @Override
    public RequestHandler resolvedHeaders(Collection<String> resolvedHeaders) {
        requestHandler.resolvedHeaders(resolvedHeaders);
        return this;
    }

    @Override
    public Collection<String> resolvedCookies() {
        return requestHandler.resolvedCookies();
    }

    @Override
    public RequestHandler resolvedCookies(Collection<String> resolvedCookes) {
        return requestHandler.resolvedCookies(resolvedCookes);
    }

    @Override
    public Collection<String> resolvedHandlesScripts() {
        return requestHandler.resolvedHandlesScripts();
    }

    @Override
    public RequestHandler resolvedHandlesScripts(Collection<String> resolvedHandlesScripts) {
        return requestHandler.resolvedHandlesScripts(resolvedHandlesScripts);
    }

    @Override
    public HandlerResolverStats handlerResolverStats() {
        return requestHandler.handlerResolverStats();
    }

    @Override
    public Request controllerRequestMapping() {
        return requestHandler.controllerRequestMapping();
    }

    @Override
    public Request handlerRequestMapping() {
        return requestHandler.handlerRequestMapping();
    }

    @Override
    public List<MethodParam> methodParams() {
        return requestHandler.methodParams();
    }

    @Override
    public Object invoke(Map<String, Object> args) {
        return this.view;
    }

    @Override
    public String toGenericString() {
        return requestHandler.toGenericString();
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
}
