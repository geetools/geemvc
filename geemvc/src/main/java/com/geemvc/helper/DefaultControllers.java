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

package com.geemvc.helper;

import javax.ws.rs.Path;

import com.geemvc.RequestContext;
import com.geemvc.Str;
import com.geemvc.annotation.Request;
import com.geemvc.cache.Cache;
import com.geemvc.matcher.MatcherContext;
import com.geemvc.matcher.PathMatcher;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class DefaultControllers implements Controllers {
    @Inject
    protected Cache cache;

    @Inject
    protected Injector injector;

    protected static final String BASE_PATH_CACHE_KEY = "geemvc/controller/basePath/%s";

    @Override
    public String getBasePath(Class<?> controllerClass) {
        String cacheKey = String.format(BASE_PATH_CACHE_KEY, controllerClass.getName());

        return (String) cache.get(DefaultControllers.class, cacheKey, () -> {
            String newBasePath = null;
            Request typeRequestMapping = controllerClass.getAnnotation(Request.class);

            if (typeRequestMapping == null) {
                Path typePath = controllerClass.getAnnotation(Path.class);

                if (typePath != null && typePath.value() != null) {
                    newBasePath = typePath.value();
                } else {
                    newBasePath = Str.EMPTY;
                }
            } else {
                String basePath = Str.isEmpty(typeRequestMapping.path()) ? typeRequestMapping.value() : typeRequestMapping.path();
                newBasePath = Str.isEmpty(basePath) ? Str.EMPTY : basePath.trim();
            }

            return newBasePath;
        });
    }

    @Override
    public String[] getIgnorePaths(Class<?> controllerClass) {
        Request typeRequestMapping = controllerClass.getAnnotation(Request.class);

        if (typeRequestMapping == null)
            return null;

        return typeRequestMapping.ignore() == null || typeRequestMapping.ignore().length == 0 ? null : typeRequestMapping.ignore();
    }

    @Override
    public boolean isIgnoreController(Class<?> controllerClass, RequestContext requestCtx) {
        String[] ignorePaths = getIgnorePaths(controllerClass);

        if (ignorePaths != null && ignorePaths.length > 0) {
            for (String ignorePath : ignorePaths) {
                PathMatcher matcher = injector.getInstance(PathMatcher.class).build(ignorePath);
                MatcherContext matcherCtx = injector.getInstance(MatcherContext.class);

                if (matcher.matches(requestCtx, matcherCtx))
                    return true;
            }
        }

        return false;
    }
}