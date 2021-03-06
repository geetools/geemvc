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

import com.geemvc.RequestContext;
import com.geemvc.cache.Cache;
import com.geemvc.helper.Annotations;
import com.geemvc.helper.Controllers;
import com.geemvc.matcher.MatcherContext;
import com.geemvc.matcher.PathMatcher;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class DefaultSimpleControllerResolver implements SimpleControllerResolver {
    protected final ReflectionProvider reflectionProvider;
    protected final Annotations annotations;
    protected final Controllers controllers;

    @Inject
    protected Cache cache;

    @Inject
    protected Injector injector;

    protected static final String CONTROLLERS_CACHE_KEY = "geemvc/resolvedControllers";

    @Inject
    public DefaultSimpleControllerResolver(ReflectionProvider reflectionProvider, Annotations annotations, Controllers controllers) {
        this.reflectionProvider = reflectionProvider;
        this.annotations = annotations;
        this.controllers = controllers;
    }

    @Override
    public Map<PathMatcherKey, Class<?>> allControllers() {
        return (Map<PathMatcherKey, Class<?>>) cache.get(DefaultSimpleControllerResolver.class, CONTROLLERS_CACHE_KEY, () -> {
            return toMap(reflectionProvider.locateControllers());
        });
    }

    @Override
    public Map<PathMatcherKey, Class<?>> resolve(RequestContext requestCtx) {
        Map<PathMatcherKey, Class<?>> controllerMap = allControllers();
        Map<PathMatcherKey, Class<?>> matchingControllers = new LinkedHashMap<>();

        for (Map.Entry<PathMatcherKey, Class<?>> entry : controllerMap.entrySet()) {
            Class<?> controllerClass = entry.getValue();
            PathMatcher pathMatcher = entry.getKey().matcher();

            MatcherContext matcherCtx = injector.getInstance(MatcherContext.class);

            if (pathMatcher.matches(requestCtx, matcherCtx)) {

                if (controllers.isIgnoreController(controllerClass, requestCtx)) {
                    continue;
                }

                matchingControllers.put(entry.getKey(), controllerClass);
            }
        }

        return matchingControllers;
    }

    protected Map<PathMatcherKey, Class<?>> toMap(Set<Class<?>> controllerClasses) {
        Map<PathMatcherKey, Class<?>> controllerMap = new LinkedHashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            PathMatcher matcher = injector.getInstance(PathMatcher.class).build(controllers.getBasePath(controllerClass));

            controllerMap.put(injector.getInstance(PathMatcherKey.class).build(controllerClass, matcher), controllerClass);
        }

        return controllerMap;
    }
}