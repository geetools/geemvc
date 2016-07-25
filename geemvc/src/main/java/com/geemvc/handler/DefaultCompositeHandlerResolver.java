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
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Singleton
public class DefaultCompositeHandlerResolver implements CompositeHandlerResolver {
    protected final Set<HandlerResolver> handlerResolvers;

    protected final ReflectionProvider reflectionProvider;

    @Logger
    protected Log log;

    @Inject
    protected Injector injector;

    @Inject
    public DefaultCompositeHandlerResolver(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
        this.handlerResolvers = reflectionProvider.locateHandlerResolvers();
    }

    @Override
    public List<RequestHandler> resolve(String requestURI) {
        List<RequestHandler> requestHandlers = new ArrayList<>();

        for (HandlerResolver handlerResolver : handlerResolvers) {
            log.trace("Looking for request handlers using path '{}' and handler resolver '{}'.", () -> requestURI, () -> handlerResolver.getClass().getName());

            final List<RequestHandler> reqHandlers = handlerResolver.resolve(requestURI);

            if (reqHandlers != null && !reqHandlers.isEmpty())
                requestHandlers.addAll(reqHandlers);

            log.trace("Found {} request handlers using path '{}' and handler resolver '{}'.", () -> reqHandlers == null ? 0 : reqHandlers.size(), () -> requestURI, () -> handlerResolver.getClass().getName());
        }

        log.trace("Found request handlers {} for path '{}'.", () -> requestHandlers, () -> requestURI);

        return requestHandlers;
    }

    @Override
    public List<RequestHandler> resolve(String requestURI, String httpMethod) {
        List<RequestHandler> requestHandlers = new ArrayList<>();

        for (HandlerResolver handlerResolver : handlerResolvers) {
            log.trace("Looking for request handlers using path '{} {}' and handler resolver '{}'.", () -> httpMethod, () -> requestURI, () -> handlerResolver.getClass().getName());

            List<RequestHandler> reqHandlers = handlerResolver.resolve(requestURI, httpMethod);

            if (reqHandlers != null && !reqHandlers.isEmpty())
                requestHandlers.addAll(reqHandlers);

            log.trace("Found {} request handlers using path '{} {}' and handler resolver '{}'.", () -> reqHandlers == null ? 0 : reqHandlers.size(), () -> httpMethod, () -> requestURI, () -> handlerResolver.getClass().getName());
        }

        log.trace("Found request handlers {} for path '{}'.", () -> requestHandlers, () -> requestURI);

        return requestHandlers;
    }

    @Override
    public RequestHandler resolve(RequestContext requestCtx, Collection<Class<?>> controllerClasses) {
        List<RequestHandler> requestHandlers = new ArrayList<>();

        for (HandlerResolver handlerResolver : handlerResolvers) {
            log.trace("Looking for request handlers in controller classes {} using handler resolver '{}'.", () -> controllerClasses, () -> handlerResolver.getClass().getName());

            RequestHandler requestHandler = handlerResolver.resolve(requestCtx, controllerClasses);

            if (requestHandler != null) {
                log.trace("Found request handler '{}' using handler resolver '{}'.", () -> requestHandler, () -> handlerResolver.getClass().getName());
                requestHandlers.add(requestHandler);
            } else {
                log.trace("No request handler found using handler resolver '{}'.", () -> handlerResolver.getClass().getName());
            }
        }

        if (requestHandlers.size() == 1) {
            log.info("Found 1 request handler ({}) for the path '{}'.", () -> requestHandlers.get(0), () -> requestCtx.getPath());
        } else if (requestHandlers.isEmpty()) {
            log.warn("No request handler found for path '{}'.", () -> requestCtx.getPath());
        } else if (requestHandlers.size() > 0) {
            log.info("More than 1 request handlers found for the path '{}'. Using the first one of {}.", () -> requestCtx.getPath(), () -> requestHandlers);
        }

        return requestHandlers.isEmpty() ? null : requestHandlers.get(0);
    }
}
