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

package com.cb.geemvc.handler;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.reflect.ReflectionProvider;
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
            requestHandlers.addAll(handlerResolver.resolve(requestURI));
        }

        return requestHandlers;
    }

    @Override
    public List<RequestHandler> resolve(String requestURI, String httpMethod) {
        List<RequestHandler> requestHandlers = new ArrayList<>();

        for (HandlerResolver handlerResolver : handlerResolvers) {
            List<RequestHandler> reqHandlers = handlerResolver.resolve(requestURI, httpMethod);

            if (reqHandlers != null && !reqHandlers.isEmpty())
                requestHandlers.addAll(reqHandlers);
        }

        return requestHandlers;
    }

    @Override
    public RequestHandler resolve(RequestContext requestCtx, Collection<Class<?>> controllers) {
        List<RequestHandler> requestHandlers = new ArrayList<>();

        for (HandlerResolver handlerResolver : handlerResolvers) {
            RequestHandler requestHandler = handlerResolver.resolve(requestCtx, controllers);

            if (requestHandler != null)
                requestHandlers.add(requestHandler);
        }

        return requestHandlers.isEmpty() ? null : requestHandlers.get(0);
    }
}
