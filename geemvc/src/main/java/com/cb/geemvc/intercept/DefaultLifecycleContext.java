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

import com.cb.geemvc.Bindings;
import com.cb.geemvc.RequestContext;
import com.cb.geemvc.handler.RequestHandler;
import com.cb.geemvc.validation.Errors;
import com.cb.geemvc.view.bean.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class DefaultLifecycleContext implements LifecycleContext {
    protected Annotation lifecycleAnnotation;
    protected RequestHandler requestHandler;
    protected RequestContext requestCtx;
    protected Bindings bindings;
    protected Errors errors;
    protected View view;
    protected boolean invokeHandler = true;


    @Override
    public LifecycleContext build(RequestHandler requestHandler, RequestContext requestCtx, Errors errors) {
        this.requestHandler = requestHandler;
        this.requestCtx = requestCtx;
        this.errors = errors;

        return this;
    }

    @Override
    public LifecycleContext lifecycle(Annotation lifecycleAnnotation) {
        this.lifecycleAnnotation = lifecycleAnnotation;
        return this;
    }

    @Override
    public Annotation lifecycle() {
        return lifecycleAnnotation;
    }


    @Override
    public RequestHandler requestHandler() {
        return requestHandler;
    }

    @Override
    public Errors errors() {
        return errors;
    }

    @Override
    public Bindings bindings() {
        return bindings;
    }

    @Override
    public LifecycleContext bindings(Bindings bindings) {
        this.bindings = bindings;
        return this;
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public LifecycleContext view(View view) {
        this.view = view;
        return this;
    }

    @Override
    public boolean isInvokeHandler() {
        return invokeHandler;
    }

    @Override
    public LifecycleContext invokeHandler(boolean invokeHandler) {
        this.invokeHandler = invokeHandler;
        return this;
    }

    @Override
    public RequestContext requestCtx() {
        return requestCtx;
    }

    @Override
    public Class<?> controllerClass() {
        return requestHandler == null ? null : requestHandler.controllerClass();
    }

    @Override
    public Method handlerMethod() {
        return requestHandler.handlerMethod();
    }
}
