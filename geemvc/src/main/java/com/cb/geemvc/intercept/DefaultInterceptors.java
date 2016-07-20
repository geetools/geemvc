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
import com.cb.geemvc.helper.Paths;
import com.cb.geemvc.intercept.annotation.Lifecycle;
import com.cb.geemvc.logging.Log;
import com.cb.geemvc.logging.annotation.Logger;
import com.cb.geemvc.validation.Errors;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public class DefaultInterceptors implements Interceptors {

    protected final InterceptorResolver interceptorResolver;
    protected final Paths paths;

    @Logger
    protected Log log;

    @Inject
    protected Injector injector;

    @Inject
    public DefaultInterceptors(InterceptorResolver interceptorResolver, Paths paths) {
        this.interceptorResolver = interceptorResolver;
        this.paths = paths;
    }

    @Override
    public Object intercept(RequestHandler targetRequestHandler, Map<String, Object> targetArgs, RequestContext requestCtx, Errors errors) {
        Set<AroundHandler> aroundInterceptors = interceptorResolver.resolveInterceptors(targetRequestHandler);

        if (aroundInterceptors != null && !aroundInterceptors.isEmpty()) {
            return injector.getInstance(InvocationContext.class).build(targetRequestHandler, targetArgs, aroundInterceptors, requestCtx, errors).proceed();
        } else {
            return targetRequestHandler.invoke(targetArgs);
        }
    }

    @Override
    public Object interceptLifecycle(Class<? extends Annotation> lifecycleAnnotation, LifecycleContext lifecycleCtx) {
        log.trace("Looking for lifecycle interceptors at stage '{}'.", () -> lifecycleAnnotation.getSimpleName());

        Set<LifecycleInterceptor> lifecycleInterceptors = interceptorResolver.resolveLifecycleInterceptors(lifecycleAnnotation, lifecycleCtx.requestHandler());


        if (lifecycleInterceptors != null && !lifecycleInterceptors.isEmpty()) {
            for (LifecycleInterceptor lifecycleInterceptor : lifecycleInterceptors) {
                Lifecycle lifecycle = lifecycleInterceptor.lifecycleAnnotation();

                if ((lifecycle.when() == When.ALWAYS || (lifecycle.when() == When.NO_ERRORS && lifecycleCtx.errors().isEmpty()) || (lifecycle.when() == When.HAS_ERRORS && !lifecycleCtx.errors().isEmpty()))
                        && (lifecycle.onView() == OnView.ALWAYS || (lifecycle.onView() == OnView.NOT_EXISTS && lifecycleCtx.view() == null) || (lifecycle.onView() == OnView.EXISTS && lifecycleCtx.view() != null))) {

                    log.trace("Invoking lifecycle interceptor '{}' for stage '{}'.", () -> lifecycleInterceptor, () -> lifecycleAnnotation.getSimpleName());

                    Object view = lifecycleInterceptor.invoke(lifecycleCtx.lifecycle(lifecycle));

                    log.trace("Lifecycle interceptor '{}' for stage '{}' returned view '{}'.", () -> lifecycleInterceptor, () -> lifecycleAnnotation.getSimpleName(), () -> view);

                    if (view != null)
                        return view;
                }
            }
        }

        return null;
    }

    protected boolean isValidForRequest(Lifecycle lifecycleAnnotation, LifecycleContext lifecycleContext) {
        if (lifecycleAnnotation.on().length == 0)
            return true;

        if (paths.isValidForRequest(lifecycleAnnotation.on(), lifecycleContext.requestHandler(), lifecycleContext.requestCtx())) {
            return true;
        } else {
            return false;
        }
    }
}
