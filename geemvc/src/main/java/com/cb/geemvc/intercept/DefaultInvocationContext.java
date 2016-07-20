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

package com.cb.geemvc.intercept;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.handler.RequestHandler;
import com.cb.geemvc.logging.Log;
import com.cb.geemvc.logging.annotation.Logger;
import com.cb.geemvc.validation.Errors;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DefaultInvocationContext implements InvocationContext {
    protected RequestHandler targetHandler;
    protected Map<String, Object> targetArgs;
    protected Set<AroundHandler> interceptors;
    protected Iterator<AroundHandler> iterator;
    protected RequestContext requestContext;
    protected Errors errors;

    @Logger
    protected Log log;

    @Override
    public InvocationContext build(RequestHandler targetHandler, Map<String, Object> targetArgs, Set<AroundHandler> interceptors, RequestContext requestContext, Errors errors) {
        this.targetHandler = targetHandler;
        this.targetArgs = targetArgs;
        this.interceptors = interceptors;
        this.iterator = interceptors.iterator();
        this.requestContext = requestContext;
        this.errors = errors;

        return this;
    }

    @Override
    public Object proceed() {

        try {
            if (iterator.hasNext())
                return invoke(iterator.next());
            else {
                log.trace("Invoking request handler '{}' with args {}.", () -> targetHandler, () -> targetArgs);
                return targetHandler.invoke(targetArgs);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    protected Object invoke(AroundHandler aroundHandler) throws Throwable {
        log.trace("Invoking around handler '{}'.", () -> aroundHandler == null ? null : aroundHandler.getClass().getName());

        return aroundHandler.invokeAround(this);
    }

    @Override
    public RequestHandler requestHandler() {
        return targetHandler;
    }

    @Override
    public Map<String, Object> args() {
        return targetArgs;
    }

    @Override
    public Class<?> controllerClass() {
        return targetHandler.controllerClass();
    }

    @Override
    public Method handlerMethod() {
        return targetHandler.handlerMethod();
    }

    @Override
    public RequestContext requestContext() {
        return requestContext;
    }

    @Override
    public Errors errors() {
        return errors;
    }
}
