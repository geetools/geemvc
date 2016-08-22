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

package com.geemvc.rest.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import com.geemvc.cache.Cache;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Created by Michael on 17.08.2016.
 */
public class JaxRsApplication extends Application {

    @Inject
    protected Cache cache;

    @Inject
    protected Injector injector;

    @Logger
    protected Log log;

    @Override
    public Set<Class<?>> getClasses() {
        return null;
    }

    @Override
    public Set<Object> getSingletons() {
        return null;
    }

    protected boolean isValidResource(Class<?> resourceClass) {
        boolean isValid = true;

        if (isSingleton(resourceClass))
            isValid = false;

        if (isValid) {
            boolean httpMethodAnnotationPresent = false;
            boolean pathAnnotationPresent = false;

            Method[] methods = resourceClass.getMethods();

            // There must be at least one method with either a @Path annotation or a request method designator (@GET, @POST etc).
            for (Method method : methods) {
                if (method.getDeclaringClass() != Object.class && Modifier.isPublic(method.getModifiers())) {
                    Annotation[] annotations = method.getAnnotations();

                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType() == Path.class) {
                            pathAnnotationPresent = true;
                            break;
                        }

                        if (annotation.annotationType().isAnnotationPresent(HttpMethod.class)) {
                            httpMethodAnnotationPresent = true;
                            break;
                        }
                    }

                    if (httpMethodAnnotationPresent || pathAnnotationPresent)
                        break;
                }
            }

            isValid = httpMethodAnnotationPresent || pathAnnotationPresent;
        }

        if (!isValid)
            log.warn("geeMVC found the resource class '{}', but it is not valid. Please make sure that it has not been annotated with"
                    + " @Singleton and that at least 1 method exists that is either annotated with @Path or has a request method designator (@GET, @POST etc).",
                    () -> resourceClass.getName());

        return isValid;
    }

    protected boolean isResource(Class<?> resourceClass) {
        boolean isResource = false;

        if (resourceClass.isAnnotationPresent(Path.class))
            isResource = true;

        if (!isResource) {
            Class<?>[] interfaces = resourceClass.getInterfaces();

            for (Class<?> interf : interfaces) {
                if (interf.isAnnotationPresent(Path.class)) {
                    isResource = true;
                    break;
                }
            }
        }

        if (!isResource) {
            Method[] methods = resourceClass.getMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(Path.class)) {
                    isResource = true;
                    break;
                }
            }
        }

        if (isResource && isSingleton(resourceClass)) {
            log.warn("Resource classes cannot be singletons. Please remove the @Singleton annotation from the class '{}'.", () -> resourceClass.getName());
        }

        return isResource;
    }

    public boolean isSingleton(Class<?> providerClass) {
        if (providerClass.isAnnotationPresent(Singleton.class) || providerClass.isAnnotationPresent(javax.inject.Singleton.class)) {
            return true;
        }

        Class<?>[] interfaces = providerClass.getInterfaces();

        for (Class<?> interf : interfaces) {
            if (interf.isAnnotationPresent(Singleton.class) || interf.isAnnotationPresent(javax.inject.Singleton.class)) {
                return true;
            }
        }

        return false;
    }
}
