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

package com.geemvc.view.binding;

import java.lang.reflect.Method;

import com.geemvc.Bindings;
import com.geemvc.RequestContext;
import com.geemvc.handler.RequestHandler;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.validation.Errors;
import com.geemvc.view.bean.Result;

public class DefaultBindingContext implements BindingContext {
    protected RequestHandler requestHandler;
    protected RequestContext requestCtx;
    protected Bindings bindings;
    protected Errors errors;
    protected Notices notices;
    protected Result result;

    @Override
    public BindingContext build(RequestHandler requestHandler, RequestContext requestCtx, Errors errors, Notices notices) {
        this.requestHandler = requestHandler;
        this.requestCtx = requestCtx;
        this.errors = errors;
        this.notices = notices;

        return this;
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
    public BindingContext bindings(Bindings bindings) {
        this.bindings = bindings;
        return this;
    }

    @Override
    public Result result() {
        return result;
    }

    @Override
    public BindingContext result(Result result) {
        this.result = result;
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
