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

import com.cb.geemvc.annotation.Request;
import com.cb.geemvc.handler.RequestHandler;
import com.cb.geemvc.helper.Paths;
import com.cb.geemvc.intercept.annotation.Intercept;
import com.cb.geemvc.intercept.annotation.Lifecycle;
import com.cb.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultInterceptorResolver implements InterceptorResolver {

    protected final ReflectionProvider reflectionProvider;
    protected final Paths paths;

    @Inject
    public DefaultInterceptorResolver(ReflectionProvider reflectionProvider, Paths paths) {
        this.reflectionProvider = reflectionProvider;
        this.paths = paths;
    }

    @Override
    public Set<AroundHandler> resolveInterceptors(RequestHandler requestHandler) {

        Class<?> controllerClass = requestHandler.controllerClass();
        Method handlerMethod = requestHandler.handlerMethod();

        Set<AroundHandler> aroundHandlerInterceptors = reflectionProvider.locateAroundHandlerInterceptors();
        Set<AroundHandler> matchingAroundHandlerInterceptors = new LinkedHashSet<>();

        String uniqueHandlerName = handlerMethod.getAnnotation(Request.class).name();

        for (AroundHandler aroundHandler : aroundHandlerInterceptors) {
            Intercept interceptAnno = annotation(aroundHandler);

            if (interceptAnno.controller().length == 0 && interceptAnno.method().length == 0 && interceptAnno.name().length == 0) {
                matchingAroundHandlerInterceptors.add(aroundHandler);
            } else if (Arrays.binarySearch(interceptAnno.controller(), controllerClass) >= 0 && interceptAnno.method().length == 0 && interceptAnno.name().length == 0) {
                matchingAroundHandlerInterceptors.add(aroundHandler);
            } else if (Arrays.binarySearch(interceptAnno.controller(), controllerClass) >= 0 && Arrays.binarySearch(interceptAnno.method(), handlerMethod.getName()) >= 0) {
                matchingAroundHandlerInterceptors.add(aroundHandler);
            } else if (Arrays.binarySearch(interceptAnno.controller(), controllerClass) >= 0 && uniqueHandlerName != null && Arrays.binarySearch(interceptAnno.name(), uniqueHandlerName) >= 0) {
                matchingAroundHandlerInterceptors.add(aroundHandler);
            }
        }

        return matchingAroundHandlerInterceptors;
    }

    @Override
    public Set<LifecycleInterceptor> resolveLifecycleInterceptors(Class<? extends Annotation> lifecycleAnnotation, RequestHandler requestHandler) {
        Class<?> controllerClass = requestHandler.controllerClass();
        Method handlerMethod = requestHandler.handlerMethod();

        // Get method interceptors from request handler.
        Set<LifecycleInterceptor> lifecycleInterceptors1 = reflectionProvider.locateLifecycleInterceptors(lifecycleAnnotation, controllerClass);
        // Get method interceptors located in external classes.
        Set<LifecycleInterceptor> lifecycleInterceptors2 = reflectionProvider.locateLifecycleInterceptors(lifecycleAnnotation);

        Set<LifecycleInterceptor> lifecycleInterceptors = null;

        if (lifecycleInterceptors1 != null && !lifecycleInterceptors1.isEmpty()) {
            if (lifecycleInterceptors == null)
                lifecycleInterceptors = new LinkedHashSet<>();

            lifecycleInterceptors.addAll(lifecycleInterceptors1);
        }

        if (lifecycleInterceptors2 != null && !lifecycleInterceptors2.isEmpty()) {
            if (lifecycleInterceptors == null)
                lifecycleInterceptors = new LinkedHashSet<>();

            lifecycleInterceptors.addAll(lifecycleInterceptors2);
        }

        Set<LifecycleInterceptor> matchingLifecycleInterceptors = null;

        if (lifecycleInterceptors != null && !lifecycleInterceptors.isEmpty()) {
            matchingLifecycleInterceptors = new LinkedHashSet<>();

            String uniqueHandlerName = handlerMethod.getAnnotation(Request.class).name();

            for (LifecycleInterceptor lifecycleInterceptor : lifecycleInterceptors) {
                Lifecycle lifecycleAnno = (Lifecycle) lifecycleInterceptor.lifecycleAnnotation();

                if (lifecycleAnno.controller().length == 0 && lifecycleAnno.method().length == 0 && lifecycleAnno.name().length == 0) {
                    matchingLifecycleInterceptors.add(lifecycleInterceptor);
                } else if (Arrays.binarySearch(lifecycleAnno.controller(), controllerClass) >= 0 && lifecycleAnno.method().length == 0 && lifecycleAnno.name().length == 0) {
                    matchingLifecycleInterceptors.add(lifecycleInterceptor);
                } else if (Arrays.binarySearch(lifecycleAnno.controller(), controllerClass) >= 0 && Arrays.binarySearch(lifecycleAnno.method(), handlerMethod.getName()) >= 0) {
                    matchingLifecycleInterceptors.add(lifecycleInterceptor);
                } else if (Arrays.binarySearch(lifecycleAnno.controller(), controllerClass) >= 0 && uniqueHandlerName != null && Arrays.binarySearch(lifecycleAnno.name(), uniqueHandlerName) >= 0) {
                    matchingLifecycleInterceptors.add(lifecycleInterceptor);
                }
            }
        }

        return matchingLifecycleInterceptors;
    }

    protected Intercept annotation(AroundHandler aroundHandler) {
        Intercept anno = aroundHandler.getClass().getAnnotation(Intercept.class);

        if (anno != null)
            return anno;

        Class<?>[] interfaces = aroundHandler.getClass().getInterfaces();

        if (interfaces == null || interfaces.length == 0)
            return null;

        for (Class<?> interf : interfaces) {
            anno = interf.getAnnotation(Intercept.class);

            if (anno != null)
                return anno;
        }

        return null;
    }
}
