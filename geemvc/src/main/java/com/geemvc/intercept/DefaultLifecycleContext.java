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

package com.geemvc.intercept;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import com.geemvc.Bindings;
import com.geemvc.RequestContext;
import com.geemvc.handler.RequestHandler;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.validation.Errors;
import com.geemvc.view.bean.Result;

public class DefaultLifecycleContext implements LifecycleContext {
    protected Annotation lifecycleAnnotation;
    protected RequestHandler requestHandler;
    protected RequestContext requestCtx;
    protected Bindings bindings;
    protected Errors errors;
    protected Notices notices;
    protected Result result;
    protected boolean invokeHandler = true;

    @Override
    public LifecycleContext build(RequestHandler requestHandler, RequestContext requestCtx, Errors errors, Notices notices) {
        this.requestHandler = requestHandler;
        this.requestCtx = requestCtx;
        this.errors = errors;
        this.notices = notices;

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
    public Notices notices() {
        return notices;
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
    public Result result() {
        return result;
    }

    @Override
    public LifecycleContext result(Result result) {
        // Merge previous result (bindings) with new one.
        if (this.result != null && result != null) {
            // Make sure that the bindings from the previous result object are not lost.
            // Bindings with the same key will not be overridden.
            this.result = merge(result, this.result);
        } else {
            this.result = result;
        }

        return this;
    }

    protected Result merge(Result newResult, Result previousResult) {
        if (previousResult.hasBindings()) {
            for (Map.Entry<String, Object> entry : previousResult.bindings().entrySet()) {
                if (!newResult.containsBinding(entry.getKey())) {
                    newResult.bind(entry.getKey(), entry.getValue());
                }
            }
        }

        return newResult;
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
