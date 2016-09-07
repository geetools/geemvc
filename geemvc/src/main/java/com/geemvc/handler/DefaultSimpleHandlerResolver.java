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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.geemvc.InternalRequestContext;
import com.geemvc.RequestContext;
import com.geemvc.annotation.Request;
import com.geemvc.cache.Cache;
import com.geemvc.helper.Annotations;
import com.geemvc.helper.Controllers;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.matcher.MatcherContext;
import com.geemvc.matcher.PathMatcher;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class DefaultSimpleHandlerResolver implements SimpleHandlerResolver {
    protected final ReflectionProvider reflectionProvider;
    protected final Annotations annotations;
    protected final Controllers controllers;
    protected final CompositeControllerResolver controllerResolver;
    protected final RequestHandlers requestHandlers;

    @Inject
    protected Cache cache;

    @Inject
    protected Injector injector;

    @Logger
    protected Log log;

    protected static final String HANDLER_CACHE_KEY = "geemvc/resolvedHandlers/%s@%s";
    protected static final String PREFILTERED_HANDLERS_CACHE_KEY = "geemvc/prefilteredHandlers/%s->%s";

    @Inject
    public DefaultSimpleHandlerResolver(ReflectionProvider reflectionProvider, Annotations annotations, Controllers controllers, CompositeControllerResolver controllerResolver, RequestHandlers requestHandlers) {
        this.reflectionProvider = reflectionProvider;
        this.annotations = annotations;
        this.controllers = controllers;
        this.controllerResolver = controllerResolver;
        this.requestHandlers = requestHandlers;
    }

    @Override
    public RequestHandler resolve(Class<?> controllerClass, String handlerMethod) {
        String cacheKey = String.format(HANDLER_CACHE_KEY, controllerClass.getName(), handlerMethod);

        return (RequestHandler) cache.get(DefaultSimpleHandlerResolver.class, cacheKey, () -> {
            Method[] handlerMethods = controllerClass.getMethods();

            for (Method hm : handlerMethods) {
                if (hm.getName().equals(handlerMethod)) {
                    Request requestMapping = hm.getAnnotation(Request.class);

                    if (requestMapping == null)
                        continue;

                    return injector.getInstance(RequestHandler.class).build(controllerClass, hm).name(requestMapping.name());
                }
            }

            return null;
        });
    }

    @Override
    public RequestHandler resolveByName(String uniqueName) {
        String cacheKey = String.format(HANDLER_CACHE_KEY, "unique-name", uniqueName);

        return (RequestHandler) cache.get(DefaultSimpleHandlerResolver.class, cacheKey, () -> {
            Map<PathMatcherKey, Class<?>> controllerMap = controllerResolver.allControllers();

            Collection<Class<?>> controllerClasses = controllerMap.values();

            for (Class<?> controllerClass : controllerClasses) {
                Map<RequestMappingKey, Method> requestMappings = reflectionProvider.getRequestHandlerMethods(controllerClass);

                Set<RequestMappingKey> keys = requestMappings.keySet();

                for (RequestMappingKey requestMappingKey : keys) {
                    Method handlerMethod = requestMappings.get(requestMappingKey);
                    Request requestMapping = requestMappingKey.requestMapping();

                    if (requestMapping == null)
                        continue;

                    if (uniqueName.equals(requestMapping.name())) {
                        return injector.getInstance(RequestHandler.class).build(controllerClass, handlerMethod).name(requestMapping.name());
                    }
                }
            }

            return null;
        });
    }

    @Override
    public List<RequestHandler> resolve(String requestURI) {
        return resolve(requestURI, (String) null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RequestHandler> resolve(String requestURI, String httpMethod) {
        String cacheKey = String.format(HANDLER_CACHE_KEY, requestURI, httpMethod);

        return (List<RequestHandler>) cache.get(DefaultSimpleHandlerResolver.class, cacheKey, () -> {
            List<RequestHandler> foundHandlers = new ArrayList<>();

            RequestContext requestCtx = injector.getInstance(InternalRequestContext.class).build(requestURI, httpMethod);

            Map<PathMatcherKey, Class<?>> controllerMap = controllerResolver.resolve(requestCtx);

            Collection<Class<?>> controllerClasses = controllerMap.values();

            for (Class<?> controllerClass : controllerClasses) {
                String basePath = controllers.getBasePath(controllerClass);

                Map<RequestMappingKey, Method> requestMappings = reflectionProvider.getRequestHandlerMethods(controllerClass);

                Set<RequestMappingKey> keys = requestMappings.keySet();

                for (RequestMappingKey requestMappingKey : keys) {
                    Method handlerMethod = requestMappings.get(requestMappingKey);
                    Request requestMapping = requestMappingKey.requestMapping();

                    String path = annotations.path(requestMapping);

                    PathMatcher pathMatcher = injector.getInstance(PathMatcher.class).build(basePath, path);
                    MatcherContext pathMatcherCtx = injector.getInstance(MatcherContext.class);

                    boolean pathMatches = pathMatcher.matches(requestCtx, pathMatcherCtx);

                    if (pathMatches) {
                        if (requestHandlers.isIgnore(requestMapping, requestCtx)) {
                            continue;
                        }

                        if (httpMethod == null || requestHandlers.hasRequestMethod(requestMapping, requestCtx)) {
                            RequestHandler requestHandler = injector.getInstance(RequestHandler.class).build(controllerClass, handlerMethod)
                                    .name(requestMapping.name())
                                    .pathMatcher(pathMatcher);

                            foundHandlers.add(requestHandler);
                        }
                    }
                }
            }

            return foundHandlers;
        });
    }

    @Override
    public RequestHandler resolve(RequestContext requestCtx, Collection<Class<?>> controllerClasses) {
        if (controllers == null)
            return null;

        log.trace("Attempting to resolve request handler for path '{}' in controller classes {}.", () -> requestCtx.getPath(), () -> controllerClasses);

        String cacheKey = String.format(PREFILTERED_HANDLERS_CACHE_KEY, requestCtx.getMethod(), requestCtx.getPath());

        // First we pre-filter the available request-handlers as this makes it possible to cache them by their path.
        List<RequestHandler> preFilteredHandlers = (List<RequestHandler>) cache.get(DefaultSimpleHandlerResolver.class, cacheKey, () -> {
            List<RequestHandler> filteredHandlers = new ArrayList<>();

            log.trace("Pre-filtering handler methods by path '{}'.", () -> requestCtx.getPath());

            for (Class<?> controllerClass : controllerClasses) {
                String basePath = controllers.getBasePath(controllerClass);

                Map<RequestMappingKey, Method> requestMappings = reflectionProvider.getRequestHandlerMethods(controllerClass);

                for (Map.Entry<RequestMappingKey, Method> requestMappingEntry : requestMappings.entrySet()) {
                    Method handlerMethod = requestMappingEntry.getValue();
                    Request requestMapping = requestMappingEntry.getKey().requestMapping();

                    String path = annotations.path(requestMapping);

                    PathMatcher pathMatcher = injector.getInstance(PathMatcher.class).build(basePath, path);
                    MatcherContext pathMatcherCtx = injector.getInstance(MatcherContext.class);

                    boolean pathMatches = pathMatcher.matches(requestCtx, pathMatcherCtx);

                    if (pathMatches) {
                        if (requestHandlers.isIgnore(requestMapping, requestCtx)) {
                            log.trace("Path '{}' is in ignore list of request mapping {}->{}.", () -> requestCtx.getPath(), () -> controllerClass.getSimpleName(), () -> requestMapping.path());
                            continue;
                        }
                    } else {
                        log.trace("Path '{}' does not match path in request mapping {}->{}.", () -> requestCtx.getPath(), () -> controllerClass.getSimpleName(), () -> requestMapping.path());
                        continue;
                    }

                    log.trace("Path '{}' matches path in request mapping {}->{}.", () -> requestCtx.getPath(), () -> controllerClass.getSimpleName(), () -> requestMapping.path());

                    RequestHandler requestHandler = injector.getInstance(RequestHandler.class)
                            .build(controllerClass, handlerMethod)
                            .name(requestMapping.name())
                            .pathMatcher(pathMatcher);

                    filteredHandlers.add(requestHandler);
                }
            }

            return filteredHandlers;
        });

        final List<RequestHandler> logPreFilteredHandlers = preFilteredHandlers;
        log.trace("Using pre-filtered request handlers {}.", () -> logPreFilteredHandlers);

        // As the first filtering stage has already returned a unique result, no need to continue filtering.
        // if (preFilteredHandlers.size() == 1) {
        // log.trace("As only 1 pre-filtered request handler was found, we will simply use that one ({}).", () -> logPreFilteredHandlers.get(0));
        // return preFilteredHandlers.get(0);
        // }

        List<RequestHandler> foundHandlers = new ArrayList<>();

        log.trace("More than 1 pre-filtered request handlers exit ({}). Therefore now attempting to filter by request parameters, headers, consumes, produces, cookies and handles statement.", () -> logPreFilteredHandlers);

        // Now we go into the second filtering stage. Her we attempt to filter by parameters, headers etc.
        for (RequestHandler preFilteredHandler : preFilteredHandlers) {
            Request requestMapping = preFilteredHandler.handlerRequestMapping();

            if (requestHandlers.handlerResolutionPlan(preFilteredHandler, requestCtx).isCompatible()) {
                log.trace("Found request handler '{}' for matching request parameters, headers, consumes, produces, cookies and handles statement.", () -> preFilteredHandler);

                RequestHandler requestHandler = injector.getInstance(RequestHandler.class)
                        .build(preFilteredHandler.controllerClass(), preFilteredHandler.handlerMethod())
                        .name(requestMapping.name())
                        .pathMatcher(preFilteredHandler.pathMatcher());

                foundHandlers.add(requestHandler);
            }
        }

        // As the second filtering stage has returned a single result, no need to continue filtering.
        if (foundHandlers.size() == 1) {
            final RequestHandler foundHandler = foundHandlers.get(0);
            log.trace("Filtering request handlers by request parameters, headers, consumes, produces, cookies and handles statement revealed only 1 handler method. Therefore using that one ({}).", () -> foundHandler);
            return foundHandler;
        }

        // If however we have still not found a unique request handler, we will need to continue finding the best match.
        if (foundHandlers.size() > 1) {
            final List<RequestHandler> logFoundHandlers = new ArrayList<>(foundHandlers);
            log.trace("Filtering request handlers by request parameters, headers, consumes, produces, cookies and handles statement revealed more than 1 handler method. Therefore attempting to find best match out of ({}).", () -> logFoundHandlers);

            List<RequestHandler> requestHandlerCollector = new ArrayList<>();

            logFoundHandlers.clear();
            logFoundHandlers.addAll(foundHandlers);
            log.trace("Attempting to find best match by http methods using request handlers {} ...", () -> logFoundHandlers);

            RequestHandler bestMatch = requestHandlers.findBestMatchForHttpMethod(requestCtx, foundHandlers, requestHandlerCollector);

            if (bestMatch == null) {
                if (requestHandlerCollector.size() != 0) {
                    foundHandlers = new ArrayList<>(requestHandlerCollector);
                    requestHandlerCollector = new ArrayList<>();
                }

                logFoundHandlers.clear();
                logFoundHandlers.addAll(foundHandlers);
                log.trace("Attempting to find best match by mime-types using request handlers {} ...", () -> logFoundHandlers);

                bestMatch = requestHandlers.findBestMatchForMimeTypes(requestCtx, foundHandlers, requestHandlerCollector, true, false);
            }

            // Attempt to find the best match by finding the closest (single) path match.
            if (bestMatch == null) {
                if (requestHandlerCollector.size() != 0) {
                    foundHandlers = new ArrayList<>(requestHandlerCollector);
                    requestHandlerCollector = new ArrayList<>();
                }

                logFoundHandlers.clear();
                logFoundHandlers.addAll(foundHandlers);
                log.trace("Attempting to find best match by path using request handlers {} ...", () -> logFoundHandlers);

                bestMatch = requestHandlers.findBestMatchByPath(requestCtx, foundHandlers, requestHandlerCollector);
            }

            // If no best match could be found, we Attempt to find the best match by evaluating other criteria.
            if (bestMatch == null) {
                foundHandlers = new ArrayList<>(requestHandlerCollector);
                requestHandlerCollector = new ArrayList<>();

                logFoundHandlers.clear();
                logFoundHandlers.addAll(foundHandlers);
                log.trace("Attempting to find best match by parameters using request handlers {} ...", () -> logFoundHandlers);

                // Evaluate parameters.
                bestMatch = requestHandlers.findBestMatch(requestCtx, foundHandlers,
                        HandlerResolutionPlan::numResolvedParameters,
                        HandlerResolutionPlan::numResolvedStaticParameters,
                        HandlerResolutionPlan::numResolvedStaticNegateParameters,
                        requestHandlerCollector);

                // Evaluate Headers.
                if (bestMatch == null) {
                    foundHandlers = new ArrayList<>(requestHandlerCollector);
                    requestHandlerCollector = new ArrayList<>();

                    logFoundHandlers.clear();
                    logFoundHandlers.addAll(foundHandlers);
                    log.trace("Attempting to find best match by headers using request handlers {} ...", () -> logFoundHandlers);

                    bestMatch = requestHandlers.findBestMatch(requestCtx, foundHandlers,
                            HandlerResolutionPlan::numResolvedHeaders,
                            HandlerResolutionPlan::numResolvedStaticHeaders,
                            HandlerResolutionPlan::numResolvedStaticNegateHeaders,
                            requestHandlerCollector);

                    // Evaluate Cookies.
                    if (bestMatch == null) {
                        foundHandlers = new ArrayList<>(requestHandlerCollector);
                        requestHandlerCollector = new ArrayList<>();

                        logFoundHandlers.clear();
                        logFoundHandlers.addAll(foundHandlers);
                        log.trace("Attempting to find best match by cookies using request handlers {} ...", () -> logFoundHandlers);

                        bestMatch = requestHandlers.findBestMatch(requestCtx, foundHandlers,
                                HandlerResolutionPlan::numResolvedCookies,
                                HandlerResolutionPlan::numResolvedStaticCookies,
                                HandlerResolutionPlan::numResolvedStaticNegateCookies,
                                requestHandlerCollector);

                        // Evaluate path parameters.
                        if (bestMatch == null) {
                            logFoundHandlers.clear();
                            logFoundHandlers.addAll(foundHandlers);
                            log.trace("Attempting to find best match by path parameters using request handlers {} ...", () -> logFoundHandlers);

                            bestMatch = requestHandlers.mostMatchingPathParameters(requestCtx, foundHandlers);

                            if (bestMatch == null) {
                                log.trace("Attempting to see if priority has been set as still no unique request handler could be found...");

                                if (requestHandlers.anyHandlersHavePriority(requestHandlerCollector)) {
                                    requestHandlers.sortByPriority(requestHandlerCollector);
                                    bestMatch = requestHandlerCollector.get(0);
                                }

                                if (bestMatch == null) {
                                    log.trace("Attempting to find first best match by mime-types using request handlers {} ...", () -> logFoundHandlers);

                                    bestMatch = requestHandlers.findBestMatchForMimeTypes(requestCtx, foundHandlers, requestHandlerCollector, true, true);
                                }
                            }
                        }
                    }
                }
            }

            final RequestHandler logBestMatch = bestMatch;
            log.trace("Returning best matching request handler '{}'.", () -> logBestMatch);

            return bestMatch != null ? bestMatch : null;
        } else if (foundHandlers.size() == 1) {
            final RequestHandler foundSingleRequestHandler = foundHandlers.get(0);
            log.trace("Returning the only request handler ({}) that was found.", () -> foundSingleRequestHandler);

            return foundSingleRequestHandler;
        }

        log.trace("No request handler could be found.");

        return null;
    }

}
