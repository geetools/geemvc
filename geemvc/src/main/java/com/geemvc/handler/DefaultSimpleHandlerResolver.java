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

import com.geemvc.Char;
import com.geemvc.PathOnlyRequestContext;
import com.geemvc.RequestContext;
import com.geemvc.annotation.Request;
import com.geemvc.cache.Cache;
import com.geemvc.helper.Annotations;
import com.geemvc.helper.Controllers;
import com.geemvc.helper.MimeTypes;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.reflect.ReflectionProvider;
import com.geemvc.matcher.*;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class DefaultSimpleHandlerResolver implements SimpleHandlerResolver {
    protected final ReflectionProvider reflectionProvider;
    protected final Annotations annotations;
    protected final Controllers controllers;
    protected final CompositeControllerResolver controllerResolver;

    protected String acceptHeader = "Accept";
    protected String contentTypeHeader = "Content-Type";

    @Inject
    protected Cache cache;

    @Inject
    protected Injector injector;

    @Logger
    protected Log log;

    protected static final String HANDLER_CACHE_KEY = "geemvc/resolvedHandlers/%s@%s";
    protected static final String PREFILTERED_HANDLERS_CACHE_KEY = "geemvc/prefilteredHandlers/%s->%s";

    @Inject
    public DefaultSimpleHandlerResolver(ReflectionProvider reflectionProvider, Annotations annotations, Controllers controllers, CompositeControllerResolver controllerResolver) {
        this.reflectionProvider = reflectionProvider;
        this.annotations = annotations;
        this.controllers = controllers;
        this.controllerResolver = controllerResolver;
    }

    @Override
    public List<RequestHandler> resolve(String requestURI) {
        return resolve(requestURI, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RequestHandler> resolve(String requestURI, String httpMethod) {
        String cacheKey = String.format(HANDLER_CACHE_KEY, requestURI, httpMethod);

        return (List<RequestHandler>) cache.get(DefaultSimpleHandlerResolver.class, cacheKey, () -> {
            List<RequestHandler> foundHandlers = foundHandlers = new ArrayList<>();

            RequestContext requestCtx = injector.getInstance(PathOnlyRequestContext.class).build(requestURI, httpMethod);

            Map<PathMatcherKey, Class<?>> controllerMap = controllerResolver.resolve(requestCtx);

            Collection<Class<?>> controllerClasses = controllerMap.values();

            for (Class<?> controllerClass : controllerClasses) {
                String basePath = controllers.getBasePath(controllerClass);

                Map<RequestMappingKey, Method> requestMappings = reflectionProvider.getRequestHandlerMethods(controllerClass);

                Set<RequestMappingKey> keys = requestMappings.keySet();

                for (RequestMappingKey requestMappingKey : keys) {
                    Method handlerMethod = requestMappings.get(requestMappingKey);
                    Request requestMapping = requestMappingKey.requestMapping();

                    String[] paths = annotations.paths(requestMapping);

                    for (String path : paths) {
                        PathMatcher pathMatcher = injector.getInstance(PathMatcher.class).build(basePath, path);
                        MatcherContext pathMatcherCtx = injector.getInstance(MatcherContext.class);

                        boolean pathMatches = pathMatcher.matches(requestCtx, pathMatcherCtx);

                        if (pathMatches) {
                            if (isIgnore(requestMapping, requestCtx)) {
                                continue;
                            }

                            if (httpMethod == null || hasRequestMethod(requestMapping, requestCtx)) {
                                RequestHandler requestHandler = injector.getInstance(RequestHandler.class).build(controllerClass, handlerMethod, null, null).name(requestMapping.name()).pathMatcher(pathMatcher);

                                foundHandlers.add(requestHandler);
                            }
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

                Set<RequestMappingKey> keys = requestMappings.keySet();

                for (Map.Entry<RequestMappingKey, Method> requestMappingEntry : requestMappings.entrySet()) {
                    Method handlerMethod = requestMappingEntry.getValue();
                    Request requestMapping = requestMappingEntry.getKey().requestMapping();

                    String[] paths = annotations.paths(requestMapping);

                    for (String path : paths) {
                        PathMatcher pathMatcher = injector.getInstance(PathMatcher.class).build(basePath, path);
                        MatcherContext pathMatcherCtx = injector.getInstance(MatcherContext.class);

                        boolean pathMatches = pathMatcher.matches(requestCtx, pathMatcherCtx);

                        if (pathMatches) {
                            if (isIgnore(requestMapping, requestCtx)) {
                                log.trace("Path '{}' is in ignore list of request mapping {}->{}.", () -> requestCtx.getPath(), () -> controllerClass.getSimpleName(), () -> Arrays.toString(requestMapping.path()));
                                continue;
                            }
                        } else {
                            log.trace("Path '{}' does not match path in request mapping {}->{}.", () -> requestCtx.getPath(), () -> controllerClass.getSimpleName(), () -> Arrays.toString(requestMapping.path()));
                            continue;
                        }

                        log.trace("Path '{}' matches path in request mapping {}->{}.", () -> requestCtx.getPath(), () -> controllerClass.getSimpleName(), () -> Arrays.toString(requestMapping.path()));

                        RequestHandler requestHandler = injector.getInstance(RequestHandler.class)
                                .build(controllerClass, handlerMethod, consumes(requestMapping, requestCtx), produces(requestMapping, requestCtx))
                                .name(requestMapping.name())
                                .pathMatcher(pathMatcher);

                        filteredHandlers.add(requestHandler);
                    }
                }
            }

            return filteredHandlers;
        });

        final List<RequestHandler> logPreFilteredHandlers = preFilteredHandlers;
        log.trace("Using pre-filtered request handlers {}.", () -> logPreFilteredHandlers);

        // As the first filtering stage has already returned a unique result, no need to continue filtering.
//        if (preFilteredHandlers.size() == 1) {
//            log.trace("As only 1 pre-filtered request handler was found, we will simply use that one ({}).", () -> logPreFilteredHandlers.get(0));
//            return preFilteredHandlers.get(0);
//        }

        List<RequestHandler> foundHandlers = new ArrayList<>();

        log.trace("More than 1 pre-filtered request handlers exit ({}). Therefore now attempting to filter by request parameters, headers, consumes, produces, cookies and handles statement.", () -> logPreFilteredHandlers);

        // Now we go into the second filtering stage. Her we attempt to filter by parameters, headers etc.
        for (RequestHandler preFilteredHandler : preFilteredHandlers) {
            Request requestMapping = preFilteredHandler.handlerRequestMapping();

            String[] params = requestMapping.params();
            Collection<String> headers = headers(requestMapping);
            Collection<String> consumes = consumes(requestMapping);
            Collection<String> produces = produces(requestMapping);
            String[] cookies = requestMapping.cookies();
            String handles = requestMapping.handles();

            MatcherContext paramMatcherCtx = injector.getInstance(MatcherContext.class);
            MatcherContext headerMatcherCtx = injector.getInstance(MatcherContext.class);
            MatcherContext cookieMatcherCtx = injector.getInstance(MatcherContext.class);
            MatcherContext handlesMatcherCtx = injector.getInstance(MatcherContext.class);

            if (hasRequestMethod(requestMapping, requestCtx)
                    && (params == null || params.length == 0 || parametersMatch(requestMapping, requestCtx, paramMatcherCtx))
                    && (headers == null || headers.size() == 0 || headersMatch(requestMapping, requestCtx, headerMatcherCtx))
                    && (consumes == null || consumes.size() == 0 || consumesMatch(requestMapping, requestCtx))
                    && (produces == null || produces.size() == 0 || producesMatch(requestMapping, requestCtx))
                    && (cookies == null || cookies.length == 0 || cookiesMatch(requestMapping, requestCtx, cookieMatcherCtx))
                    && (handles == null || handles.trim().isEmpty() || handleScriptMatches(requestMapping, requestCtx, handlesMatcherCtx))) {

                log.trace("Found request handler '{}' for matching request parameters, headers, consumes, produces, cookies and handles statement.", () -> preFilteredHandler);

                RequestHandler requestHandler = injector.getInstance(RequestHandler.class)
                        .build(preFilteredHandler.controllerClass(), preFilteredHandler.handlerMethod(), consumes(requestMapping, requestCtx), produces(requestMapping, requestCtx))
                        .name(requestMapping.name())
                        .pathMatcher(preFilteredHandler.pathMatcher()).resolvedParameters(paramMatcherCtx.resolvedExpressions())
                        .resolvedHeaders(headerMatcherCtx.resolvedExpressions())
                        .resolvedCookies(cookieMatcherCtx.resolvedExpressions())
                        .resolvedHandlesScripts(handlesMatcherCtx.resolvedExpressions());

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

            RequestHandler bestMatch = findBestMatchForHttpMethod(requestCtx, foundHandlers, requestHandlerCollector);

            if (bestMatch == null) {
                if (requestHandlerCollector.size() != 0) {
                    foundHandlers = new ArrayList<>(requestHandlerCollector);
                    requestHandlerCollector = new ArrayList<>();
                }

                logFoundHandlers.clear();
                logFoundHandlers.addAll(foundHandlers);
                log.trace("Attempting to find best match by mime-types using request handlers {} ...", () -> logFoundHandlers);

                bestMatch = findBestMatchForMimeTypes(requestCtx, foundHandlers, requestHandlerCollector, true, false);
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

                bestMatch = findBestMatchByPath(requestCtx, foundHandlers, requestHandlerCollector);
            }

            // If no best match could be found, we Attempt to find the best match by evaluating other criteria.
            if (bestMatch == null) {
                foundHandlers = new ArrayList<>(requestHandlerCollector);
                requestHandlerCollector = new ArrayList<>();

                logFoundHandlers.clear();
                logFoundHandlers.addAll(foundHandlers);
                log.trace("Attempting to find best match by parameters using request handlers {} ...", () -> logFoundHandlers);

                // Evaluate parameters.
                bestMatch = findBestMatch(requestCtx, foundHandlers, HandlerResolverStats::numResolvedParameters, HandlerResolverStats::numResolvedStaticParameters, HandlerResolverStats::numResolvedStaticNegateParameters, requestHandlerCollector);

                // Evaluate Headers.
                if (bestMatch == null) {
                    foundHandlers = new ArrayList<>(requestHandlerCollector);
                    requestHandlerCollector = new ArrayList<>();

                    logFoundHandlers.clear();
                    logFoundHandlers.addAll(foundHandlers);
                    log.trace("Attempting to find best match by headers using request handlers {} ...", () -> logFoundHandlers);

                    bestMatch = findBestMatch(requestCtx, foundHandlers, HandlerResolverStats::numResolvedHeaders, HandlerResolverStats::numResolvedStaticHeaders, HandlerResolverStats::numResolvedStaticNegateHeaders, requestHandlerCollector);

                    // Evaluate Cookies.
                    if (bestMatch == null) {
                        foundHandlers = new ArrayList<>(requestHandlerCollector);
                        requestHandlerCollector = new ArrayList<>();

                        logFoundHandlers.clear();
                        logFoundHandlers.addAll(foundHandlers);
                        log.trace("Attempting to find best match by cookies using request handlers {} ...", () -> logFoundHandlers);

                        bestMatch = findBestMatch(requestCtx, foundHandlers, HandlerResolverStats::numResolvedCookies, HandlerResolverStats::numResolvedStaticCookies, HandlerResolverStats::numResolvedStaticNegateCookies, requestHandlerCollector);

                        // Evaluate path parameters.
                        if (bestMatch == null) {
                            logFoundHandlers.clear();
                            logFoundHandlers.addAll(foundHandlers);
                            log.trace("Attempting to find best match by path parameters using request handlers {} ...", () -> logFoundHandlers);

                            bestMatch = mostMatchingPathParameters(requestCtx, foundHandlers);

                            if (bestMatch == null) {
                                log.trace("Attempting to see if priority has been set as still no unique request handler could be found...");

                                if (anyHandlersHavePriority(requestHandlerCollector)) {
                                    sortByPriority(requestHandlerCollector);
                                    bestMatch = requestHandlerCollector.get(0);
                                }

                                if (bestMatch == null) {
                                    log.trace("Attempting to find first best match by mime-types using request handlers {} ...", () -> logFoundHandlers);

                                    bestMatch = findBestMatchForMimeTypes(requestCtx, foundHandlers, requestHandlerCollector, true, true);
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

    protected Collection<String> headers(Request requestMapping) {
        HashSet<String> headers = new HashSet<>();

        if (requestMapping.headers() != null && requestMapping.headers().length > 0) {
            for (String header : requestMapping.headers()) {
                int pos1 = header.indexOf(Char.EQUALS);
                int pos2 = header.lastIndexOf(Char.EQUALS);

                if (pos1 != -1 && pos1 == pos2) {
                    String name = header.substring(0, pos1).trim();

                    // We will deal with these separately.
                    if (acceptHeader.equalsIgnoreCase(name) || contentTypeHeader.equalsIgnoreCase(name))
                        continue;

                    headers.add(new StringBuilder(name).append(Char.EQUALS).append(header.substring(pos1 + 1).trim()).toString());
                } else {
                    headers.add(header.trim());
                }
            }
        }

        return headers;
    }

    protected Collection<String> consumes(Request requestMapping) {
        HashSet<String> consumesTypes = new LinkedHashSet<>();

        if (requestMapping.headers() != null && requestMapping.headers().length > 0) {
            for (String header : requestMapping.headers()) {
                int pos1 = header.indexOf(Char.EQUALS);
                int pos2 = header.lastIndexOf(Char.EQUALS);

                if (pos1 != -1 && pos1 == pos2) {
                    String name = header.substring(0, pos1).trim();

                    if (contentTypeHeader.equalsIgnoreCase(name))
                        consumesTypes.add(header.substring(pos1 + 1).trim().toLowerCase());
                }
            }
        }

        if (requestMapping.consumes() != null && requestMapping.consumes().length > 0) {
            for (String consumes : requestMapping.consumes()) {
                if (MediaType.WILDCARD.equals(consumes))
                    continue;

                consumesTypes.add(consumes.trim().toLowerCase());
            }
        }

        return consumesTypes;
    }

    protected String consumes(Request requestMapping, RequestContext requestCtx) {
        Collection<String> mappedConsumes = consumes(requestMapping);

        if (mappedConsumes.isEmpty())
            return "*/*";

        if (mappedConsumes.contains(requestCtx.contentType()))
            return requestCtx.contentType();

        String wildcardMimeType = injector.getInstance(MimeTypes.class).toWildCard(requestCtx.contentType());

        if (mappedConsumes.contains(wildcardMimeType))
            return wildcardMimeType;

        return "*/*";
    }

    protected Collection<String> produces(Request requestMapping) {
        HashSet<String> producesTypes = new LinkedHashSet<>();

        if (requestMapping.headers() != null && requestMapping.headers().length > 0) {
            for (String header : requestMapping.headers()) {
                int pos1 = header.indexOf(Char.EQUALS);
                int pos2 = header.lastIndexOf(Char.EQUALS);

                if (pos1 != -1 && pos1 == pos2) {
                    String name = header.substring(0, pos1).trim();

                    if (acceptHeader.equalsIgnoreCase(name))
                        producesTypes.add(header.substring(pos1 + 1).trim().toLowerCase());
                }
            }
        }

        if (requestMapping.produces() != null && requestMapping.produces().length > 0) {
            for (String produces : requestMapping.produces()) {
                producesTypes.add(produces.trim().toLowerCase());
            }
        }

        if (producesTypes.isEmpty()) {
            producesTypes.add("text/html");
        }

        return producesTypes;
    }

    protected String produces(Request requestMapping, RequestContext requestCtx) {
        Collection<String> mappedProduces = produces(requestMapping);
        Collection<String> reqAccepts = requestCtx.accepts();

        for (String reqAccept : reqAccepts) {
            if (mappedProduces.contains(reqAccept)) {
                return reqAccept;
            }
        }

        for (String reqAccept : reqAccepts) {
            String wildcardMimeType = injector.getInstance(MimeTypes.class).toWildCard(requestCtx.contentType());

            if (mappedProduces.contains(wildcardMimeType)) {
                return reqAccept;
            }
        }

        return "text/html";
    }

    protected RequestHandler mostMatchingPathParameters(RequestContext requestCtx, List<RequestHandler> foundHandlers) {
        Map<RequestHandlerKey, Integer> numResolvedPathParameters = new LinkedHashMap<>();
        Map<RequestHandlerKey, RequestHandler> requestHandlerMap = new HashMap<>();

        int numOfHighestResolvedParameters = 0;

        for (RequestHandler requestHandler : foundHandlers) {
            PathMatcher pathMatcher = requestHandler.pathMatcher();

            RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());

            if (pathMatcher.parameterCount() == 0 || pathMatcher.parameterCount() < numOfHighestResolvedParameters)
                continue;

            Map<String, String[]> resolvedParams = pathMatcher.parameters(requestCtx);

            int numResolvedParams = resolvedParams == null ? 0 : resolvedParams.size();

            if (numResolvedParams == 0)
                continue;

            if (numResolvedParams > numOfHighestResolvedParameters)
                numOfHighestResolvedParameters = numResolvedParams;

            numResolvedPathParameters.put(key, resolvedParams.size());
            requestHandlerMap.put(key, requestHandler);
        }

        numResolvedPathParameters = sort(numResolvedPathParameters);

        if (numResolvedPathParameters.size() == 0) {
            return null;
        } else if (numResolvedPathParameters.size() == 1) {
            return requestHandlerMap.get(numResolvedPathParameters.keySet().iterator().next());
        } else {
            Collection<Integer> values = numResolvedPathParameters.values();

            Integer[] numResolvedCounts = values.toArray(new Integer[values.size()]);

            // More than one entry have the same amount of max parameters.
            if (numResolvedCounts[0].intValue() == numResolvedCounts[1].intValue())
                return null;

            return requestHandlerMap.get(numResolvedPathParameters.keySet().iterator().next());
        }
    }

    protected boolean anyHandlersHavePriority(Collection<RequestHandler> requestHandlerCollector) {
        return requestHandlerCollector.stream().anyMatch((rh) -> {
            return rh.handlerRequestMapping().priority() < 111;
        });
    }

    protected void sortByPriority(List<RequestHandler> requestHandlerCollector) {
        requestHandlerCollector.sort((rh1, rh2) -> rh1.compareTo(rh2));
    }

    protected boolean parametersMatch(Request requestMapping, RequestContext requestCtx, MatcherContext matcherCtx) {
        if (requestMapping == null)
            return false;

        String[] mappedParameters = requestMapping.params();

        ParamMatcher matcher = injector.getInstance(ParamMatcher.class).build(mappedParameters);

        return matcher.matches(requestCtx, matcherCtx);
    }

    protected boolean headersMatch(Request requestMapping, RequestContext requestCtx, MatcherContext matcherCtx) {
        if (requestMapping == null)
            return false;

        Collection<String> mappedHeaders = headers(requestMapping);

        HeaderMatcher matcher = injector.getInstance(HeaderMatcher.class).build(mappedHeaders.toArray(new String[mappedHeaders.size()]));

        return matcher.matches(requestCtx, matcherCtx);
    }

    protected boolean consumesMatch(Request requestMapping, RequestContext requestCtx) {
        if (requestMapping == null)
            return false;

        Collection<String> mappedConsumes = consumes(requestMapping);

        if (mappedConsumes.contains("*/*") || mappedConsumes.isEmpty())
            return true;

        if (mappedConsumes.contains(requestCtx.contentType()))
            return true;

        return false;
    }

    protected boolean producesMatch(Request requestMapping, RequestContext requestCtx) {
        if (requestMapping == null)
            return false;

        if (requestCtx.accepts().isEmpty() || requestCtx.accepts().contains("*/*"))
            return true;

        Collection<String> mappedProduces = produces(requestMapping);

        if (mappedProduces.isEmpty())
            return true;

        for (String produces : mappedProduces) {
            if (requestCtx.accepts().contains(produces))
                return true;
        }

        return false;
    }

    protected boolean cookiesMatch(Request requestMapping, RequestContext requestCtx, MatcherContext matcherCtx) {
        if (requestMapping == null)
            return false;

        String[] mappedCookies = requestMapping.cookies();

        CookieMatcher matcher = injector.getInstance(CookieMatcher.class).build(mappedCookies);

        return matcher.matches(requestCtx, matcherCtx);
    }

    protected boolean handleScriptMatches(Request requestMapping, RequestContext requestCtx, MatcherContext matcherCtx) {
        if (requestMapping == null)
            return false;

        String mappedHandleScript = requestMapping.handles();

        HandlesMatcher matcher = injector.getInstance(HandlesMatcher.class).build(mappedHandleScript);

        return matcher.matches(requestCtx, matcherCtx);
    }

    protected boolean hasRequestMethod(Request requestMapping, RequestContext requestCtx) {
        return hasRequestMethod(requestMapping, requestCtx, true);
    }

    protected boolean hasRequestMethod(Request requestMapping, RequestContext requestCtx, boolean emptyIsMatch) {
        if (requestMapping == null)
            return false;

        String[] allowedRequestMethods = requestMapping.method();

        if (allowedRequestMethods == null || allowedRequestMethods.length == 0) {
            return emptyIsMatch ? true : false;
        }

        for (String allowedRequestMethod : allowedRequestMethods) {
            if (requestCtx.getMethod().equalsIgnoreCase(allowedRequestMethod))
                return true;
        }

        return false;
    }

    protected boolean isIgnore(Request requestMapping, RequestContext requestCtx) {
        if (requestMapping == null)
            return false;

        String[] ignorePaths = requestMapping.ignore();

        if (ignorePaths != null && ignorePaths.length > 0) {
            for (String ignorePath : ignorePaths) {
                PathMatcher matcher = injector.getInstance(PathMatcher.class).build(ignorePath);

                if (matcher.matches(requestCtx, injector.getInstance(MatcherContext.class)))
                    return true;
            }
        }

        return false;
    }

    protected RequestHandler findBestMatchByPath(RequestContext requestCtx, List<RequestHandler> requestHandlers, List<RequestHandler> requestHandlerCollector) {
        String requestPath = requestCtx.getPath();
        Map<RequestHandlerKey, Integer> numStaticMatches = new LinkedHashMap<>();
        Map<RequestHandlerKey, RequestHandler> requestHandlerMap = new HashMap<>();

        for (RequestHandler requestHandler : requestHandlers) {
            PathMatcher matcher = requestHandler.pathMatcher();
            String mappedPath = matcher.getMappedPath();

            RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());

            numStaticMatches.put(key, 0);
            requestHandlerMap.put(key, requestHandler);

            // Split the request-path and the handler mapped-path into various
            // parts in order to find the mapped-path
            // that has the most static parts in common with the current
            // requestURI.
            String[] requestPathParts = requestPath.split("\\/");
            String[] mappedPathParts = mappedPath.split("\\/");
            int mappedPathPartsLen = mappedPathParts.length;

            // Iterate through all the request-path-parts and attempt to find
            // matches in the mapped-path.
            for (int i = 0; i < requestPathParts.length; i++) {
                String requestPathPart = requestPathParts[i];

                String mappedPathPart = null;

                if (i < mappedPathPartsLen) {
                    mappedPathPart = mappedPathParts[i];
                }

                // When a match has been found, increment the found-number in
                // the map. The path-mapping with the most
                // matches wins.
                if (mappedPathPart != null && requestPathPart.equals(mappedPathPart)) {
                    int n = numStaticMatches.get(key);
                    numStaticMatches.put(key, ++n);
                }
            }
        }

        // We now sort the map so that the entry with the most matches is at
        // index 0 and the entry with the least
        // number of matches at the end.
        numStaticMatches = sort(numStaticMatches);

        Set<RequestHandlerKey> keys = numStaticMatches.keySet();
        // We remember the request-handlers with the maximum matches in case
        // there is more than 1 and a further
        // reduction needs to be carried out.
        List<RequestHandler> filteredRequestHandlers = new ArrayList<>();

        Integer maxNumMatches = null;
        int countNumMaxMatches = 0;

        // Loop though the num-matches and find the one with the most. Ideally
        // there is only one. If not, we have to do
        // bit more work.
        for (RequestHandlerKey key : keys) {
            if (maxNumMatches == null)
                maxNumMatches = numStaticMatches.get(key);

            int numMatches = numStaticMatches.get(key);

            if (numMatches == maxNumMatches) {
                filteredRequestHandlers.add(requestHandlerMap.get(key));
                countNumMaxMatches++;
            }
        }

        // If there is more than one request-handler with the maximum number of
        // matches, we have another shot at finding
        // THE one, but this time in reverse order. This could help for URIs
        // where there is a wild card in the middle,
        // e.g. /test/**/last-part.
        if (countNumMaxMatches > 1) {
            // This time we only loop though the request-handlers that have the
            // same amount of matching static
            // path-parts.
            for (RequestHandler requestHandler : filteredRequestHandlers) {
                PathMatcher matcher = requestHandler.pathMatcher();
                String mappedPath = matcher.getMappedPath();

                String[] requestPathParts = requestPath.split("\\/");
                String[] mappedPathParts = mappedPath.split("\\/");
                int mappedPathPartsLen = mappedPathParts.length;

                // Reverse the order so we start at the back of the request-URI.
                requestPathParts = Lists.reverse(Arrays.asList(requestPathParts)).toArray(new String[requestPathParts.length]);
                mappedPathParts = Lists.reverse(Arrays.asList(mappedPathParts)).toArray(new String[mappedPathParts.length]);

                RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());

                for (int i = 0; i < requestPathParts.length; i++) {
                    String requestPathPart = requestPathParts[i];
                    String mappedPathPart = null;

                    if (i < mappedPathPartsLen)
                        mappedPathPart = mappedPathParts[i];

                    if (mappedPathPart != null && requestPathPart.equals(mappedPathPart)) {
                        int n = numStaticMatches.get(key);
                        numStaticMatches.put(key, ++n);
                    }
                }
            }

            maxNumMatches = null;
            countNumMaxMatches = 0;

            // Re-sort the map so that the entry with the most matches is at
            // index 0 and the entry with the least
            // number of matches at the end.
            numStaticMatches = sort(numStaticMatches);

            // Get the keys again with the new ordering.
            keys = numStaticMatches.keySet();

            // Again we loop though the num-matches and find the one with the
            // most. Ideally there is only one. If not,
            // we have to give up this time.
            for (RequestHandlerKey key : keys) {
                if (maxNumMatches == null)
                    maxNumMatches = numStaticMatches.get(key);

                int numMatches = numStaticMatches.get(key);

                if (numMatches == maxNumMatches) {
                    requestHandlerCollector.add(requestHandlerMap.get(key));
                    countNumMaxMatches++;
                }
            }
        }

        // If at this point we are left with one path that has the most matching
        // parts, we return it. Otherwise we give
        // up and return null.
        if (countNumMaxMatches == 1) {
            return requestHandlerMap.get(keys.toArray()[0]);
        }

        return null;
    }

    protected boolean containsParameter(String pathPart) {
        int openPos1 = pathPart.indexOf(Char.CURLY_BRACKET_OPEN);
        int openPos2 = pathPart.lastIndexOf(Char.CURLY_BRACKET_OPEN);

        int closePos1 = pathPart.indexOf(Char.CURLY_BRACKET_CLOSE);
        int closePos2 = pathPart.lastIndexOf(Char.CURLY_BRACKET_CLOSE);

        return openPos1 != -1 && openPos1 == openPos2 && closePos1 != -1 && closePos1 == closePos2 && closePos1 > openPos1;
    }

    protected RequestHandler findBestMatch(RequestContext requestCtx, List<RequestHandler> requestHandlers, Function<HandlerResolverStats, Integer> numTotalMatchesFunction, Function<HandlerResolverStats, Integer> numTotalStaticMatchesFunction, Function<HandlerResolverStats, Integer> numTotalStaticNegateMatchesFunction, List<RequestHandler> requestHandlerCollector) {
        Map<RequestHandlerKey, Integer> numMatchesMap = new LinkedHashMap<>();
        Map<RequestHandlerKey, RequestHandler> requestHandlerMap = new HashMap<>();

        // ---------------------------------------------------------------------------------------------------------
        // First we attempt to find the request handler with the most matching
        // (found) request parameters.
        // For evaluation purposes in the next step we remember the number of
        // matching parameters in a map.
        // ---------------------------------------------------------------------------------------------------------
        for (RequestHandler requestHandler : requestHandlers) {
            // The HandlerResolverStats tell us how this request handler was
            // resolved.
            HandlerResolverStats stats = requestHandler.handlerResolverStats();

            RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
            // How many annotation conditions matched when resolving this
            // handler.
            numMatchesMap.put(key, numTotalMatchesFunction.apply(stats));
            // Map the request-handler with the same key for easy reference
            // later.
            requestHandlerMap.put(key, requestHandler);
        }

        // Now that we know how many matches were resolved for each handler,
        // we'll sort them so that the one with the
        // most is at position 0.
        numMatchesMap = sort(numMatchesMap);

        // Map for collecting request-handlers with the same amount of
        // max-matches as there may be more than one.
        // We can then use this map later for further filtering.
        List<RequestHandler> filteredRequestHandlers = new ArrayList<>();

        // Count how many handlers have the max number of matched parameters.
        // Ideally there is only one.
        int noOfMaxMatchesCount = countNoOfMaxMatches(numMatchesMap, requestHandlerMap, filteredRequestHandlers);

        // If only one handler exists with the max number of matched
        // request-mapping-annotation conditions, we just
        // return it, otherwise we have to do more filtering.
        if (noOfMaxMatchesCount == 1) {
            return requestHandlerMap.get(numMatchesMap.keySet().iterator().next());
        }
        // ---------------------------------------------------------------------------------------------------------
        // As the previous filtering revealed more than one result, we have to
        // do some more. This time round we
        // attempt to find the handlers with the highest amount of static
        // (none-negate) matches. With static we
        // mean simple conditions (1=1) and not regular expressions or dynamic
        // scripting.
        // ---------------------------------------------------------------------------------------------------------
        else {
            RequestHandler[] _filteredRequestHandlers = filteredRequestHandlers.toArray(new RequestHandler[filteredRequestHandlers.size()]);
            filteredRequestHandlers.clear();
            numMatchesMap.clear();

            for (RequestHandler requestHandler : _filteredRequestHandlers) {
                HandlerResolverStats stats = requestHandler.handlerResolverStats();

                RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                // How many annotation conditions matched when resolving this
                // handler - now only with static positive
                // conditions (e.g. 1=1).
                int numPositiveMatches = numTotalStaticMatchesFunction.apply(stats) - numTotalStaticNegateMatchesFunction.apply(stats);
                numMatchesMap.put(key, numPositiveMatches < 0 ? 0 : numPositiveMatches);
            }

            // Again, we sort the number of matches so that the handler with the
            // most matches is at position 0.
            numMatchesMap = sort(numMatchesMap);

            // Again, we count how many handlers have the max number of matched
            // conditions. Ideally there is only one.
            noOfMaxMatchesCount = countNoOfMaxMatches(numMatchesMap, requestHandlerMap, filteredRequestHandlers);

            // If only one handler exists with the max number of matches, we
            // just return it, otherwise we have to do
            // more filtering.
            if (noOfMaxMatchesCount == 1) {
                return requestHandlerMap.get(numMatchesMap.keySet().iterator().next());
            }
            // ---------------------------------------------------------------------------------------------------------
            // If the previous filtering has still not revealed a unique handler
            // for handling the current request,
            // continue with the next filtering stage by now attempting to find
            // the handler with the max number
            // of static matches. These must have at least one positive
            // condition and may have any number of negate
            // conditions.
            // ---------------------------------------------------------------------------------------------------------
            else {
                _filteredRequestHandlers = filteredRequestHandlers.toArray(new RequestHandler[filteredRequestHandlers.size()]);
                filteredRequestHandlers.clear();
                numMatchesMap.clear();

                for (RequestHandler requestHandler : _filteredRequestHandlers) {
                    HandlerResolverStats stats = requestHandler.handlerResolverStats();

                    RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                    int numPositiveMatches = numTotalStaticMatchesFunction.apply(stats) - numTotalStaticNegateMatchesFunction.apply(stats);
                    numMatchesMap.put(key, numPositiveMatches > 0 ? numTotalStaticMatchesFunction.apply(stats) : 0);
                }

                // Again, we sort the number of matches so that the handler with
                // the most matches is at position 0.
                numMatchesMap = sort(numMatchesMap);

                // Again, we count how many handlers have the max number of
                // matched parameters. Ideally there is only
                // one.
                noOfMaxMatchesCount = countNoOfMaxMatches(numMatchesMap, requestHandlerMap, filteredRequestHandlers);

                // If only one handler exists with the max number of matches, we
                // just return it, otherwise we have to do
                // more filtering.
                if (noOfMaxMatchesCount == 1) {
                    return requestHandlerMap.get(numMatchesMap.keySet().iterator().next());
                } else {
                    // Otherwise collect the filtered handlers for the next
                    // filtering stage
                    // (one of parameters, headers, cookies).
                    requestHandlerCollector.addAll(filteredRequestHandlers);
                }
            }
        }

        return null;
    }

    protected RequestHandler findBestMatchForHttpMethod(RequestContext requestCtx, List<RequestHandler> requestHandlers, List<RequestHandler> requestHandlerCollector) {
        Map<RequestHandlerKey, RequestHandler> foundMatches = new LinkedHashMap<>();

        for (RequestHandler requestHandler : requestHandlers) {
            Request requestMapping = requestHandler.handlerRequestMapping();

            boolean hasRequestMethod = hasRequestMethod(requestMapping, requestCtx, false);

            if (hasRequestMethod) {
                RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                foundMatches.put(key, requestHandler);
            }
        }

        if (foundMatches.size() == 1) {
            return foundMatches.values().iterator().next();
        } else if (foundMatches.size() > 1) {
            requestHandlerCollector.addAll(foundMatches.values());
        }

        return null;
    }

    protected RequestHandler findBestMatchForMimeTypes(RequestContext requestCtx, List<RequestHandler> requestHandlers, List<RequestHandler> requestHandlerCollector, boolean singleExactMatchOnly, boolean firstMatch) {
        Map<RequestHandlerKey, RequestHandler> foundMatches = new LinkedHashMap<>();

        for (String reqAccept : requestCtx.accepts()) {
            for (RequestHandler requestHandler : requestHandlers) {
                Request requestMapping = requestHandler.handlerRequestMapping();

                Collection<String> mappedConsumes = consumes(requestMapping);
                Collection<String> mappedProduces = produces(requestMapping);

                if (mappedProduces.size() == 0 && requestCtx.accepts().size() == 0 && mappedConsumes.size() == 0 && (requestCtx.contentType() == null || requestCtx.contentType().isEmpty())) {
                    RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                    foundMatches.put(key, requestHandler);
                }

                if (mappedProduces.contains(reqAccept) && mappedConsumes.contains(requestCtx.contentType())) {
                    RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                    foundMatches.put(key, requestHandler);
                }
            }
        }

        if ((singleExactMatchOnly && foundMatches.size() == 1) || (firstMatch && foundMatches.size() > 0)) {
            return foundMatches.values().iterator().next();
        }

        if (foundMatches.size() == 0) {
            for (String reqAccept : requestCtx.accepts()) {
                for (RequestHandler requestHandler : requestHandlers) {
                    Request requestMapping = requestHandler.handlerRequestMapping();

                    Collection<String> mappedConsumes = consumes(requestMapping);
                    Collection<String> mappedProduces = produces(requestMapping);

                    if (mappedProduces.contains(injector.getInstance(MimeTypes.class).toWildCard(reqAccept)) && mappedConsumes.contains(requestCtx.contentType())) {
                        RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                        foundMatches.put(key, requestHandler);
                    }

                    if (mappedProduces.contains(reqAccept) && mappedConsumes.contains(injector.getInstance(MimeTypes.class).toWildCard(requestCtx.contentType()))) {
                        RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                        foundMatches.put(key, requestHandler);
                    }

                    if (mappedProduces.contains(injector.getInstance(MimeTypes.class).toWildCard(reqAccept)) && mappedConsumes.contains(injector.getInstance(MimeTypes.class).toWildCard(requestCtx.contentType()))) {
                        RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                        foundMatches.put(key, requestHandler);
                    }
                }
            }

        }

        if ((singleExactMatchOnly && foundMatches.size() == 1) || (firstMatch && foundMatches.size() > 0)) {
            return foundMatches.values().iterator().next();
        }

        if (foundMatches.size() == 0) {
            for (String reqAccept : requestCtx.accepts()) {
                for (RequestHandler requestHandler : requestHandlers) {
                    Request requestMapping = requestHandler.handlerRequestMapping();

                    Collection<String> mappedConsumes = consumes(requestMapping);
                    Collection<String> mappedProduces = produces(requestMapping);

                    if (mappedProduces.size() == 0 && requestCtx.accepts().size() == 0 && mappedConsumes.contains(requestCtx.contentType())) {
                        RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                        foundMatches.put(key, requestHandler);
                    }

                    if (mappedProduces.contains(reqAccept) && mappedConsumes.size() == 0 && (requestCtx.contentType() == null || requestCtx.contentType().isEmpty())) {
                        RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                        foundMatches.put(key, requestHandler);
                    }
                }
            }
        }

        if ((singleExactMatchOnly && foundMatches.size() == 1) || (firstMatch && foundMatches.size() > 0)) {
            return foundMatches.values().iterator().next();
        }

        if (foundMatches.size() == 0) {
            for (RequestHandler requestHandler : requestHandlers) {
                Request requestMapping = requestHandler.handlerRequestMapping();

                Collection<String> mappedConsumes = consumes(requestMapping);
                Collection<String> mappedProduces = produces(requestMapping);

                if (mappedProduces.isEmpty() && mappedConsumes.isEmpty()) {
                    RequestHandlerKey key = injector.getInstance(RequestHandlerKey.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod());
                    foundMatches.put(key, requestHandler);
                }
            }
        }

        if ((singleExactMatchOnly && foundMatches.size() == 1) || (firstMatch && foundMatches.size() > 0)) {
            return foundMatches.values().iterator().next();
        }

        requestHandlerCollector.addAll(foundMatches.values());

        return null;
    }

    protected RequestHandler findBestMatchForContentTypeHeader(RequestContext requestCtx, List<RequestHandler> requestHandlers, List<RequestHandler> requestHandlerCollector, boolean singleExactMatchOnly) {
        List<RequestHandler> exactMatches = new ArrayList<>();

        for (RequestHandler requestHandler : requestHandlers) {
            Request requestMapping = requestHandler.handlerRequestMapping();

            Collection<String> mappedConsumes = consumes(requestMapping);

            if (mappedConsumes.size() == 0 && (requestCtx.contentType() == null || requestCtx.contentType().isEmpty())) {
                exactMatches.add(requestHandler);
            } else if (mappedConsumes.contains(requestCtx.contentType())) {
                exactMatches.add(requestHandler);
            }
        }

        List<RequestHandler> emptyMatches = new ArrayList<>();

        if (exactMatches.size() == 0) {
            for (RequestHandler requestHandler : requestHandlers) {
                Request requestMapping = requestHandler.handlerRequestMapping();

                Collection<String> mappedConsumes = consumes(requestMapping);

                if (mappedConsumes.isEmpty()) {
                    emptyMatches.add(requestHandler);
                }
            }
        }

        if (singleExactMatchOnly && exactMatches.size() == 1) {
            return exactMatches.get(0);
        } else if (singleExactMatchOnly && emptyMatches.size() == 1) {
            return emptyMatches.get(0);
        } else {
            requestHandlerCollector.addAll(exactMatches);
        }

        return null;
    }

    /**
     * Sorts the map so that the handler with the most matched (request-mapping annotation) conditions is at position 0 and the one with the least
     * number of matches is at the end.
     */
    protected Map<RequestHandlerKey, Integer> sort(Map<RequestHandlerKey, Integer> numMatches) {
        return numMatches.entrySet().stream().sorted(Map.Entry.<RequestHandlerKey, Integer>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> {
            throw new AssertionError();
        }, LinkedHashMap::new));
    }

    /**
     * Finds the maximum number of conditions that have been matched in a single request-mapping annotation. All request-handlers that have this
     * maximum number of matched conditions are collected in requestHandlerCollector.
     */
    protected Integer countNoOfMaxMatches(Map<RequestHandlerKey, Integer> numMatchesMap, Map<RequestHandlerKey, RequestHandler> requestHandlerMap, List<RequestHandler> requestHandlerCollector) {
        Set<RequestHandlerKey> keys = numMatchesMap.keySet();

        Integer maxNumMatches = null;
        int noOfMaxMatchesCount = 0;

        for (RequestHandlerKey key : keys) {
            if (maxNumMatches == null)
                maxNumMatches = numMatchesMap.get(key);

            int numMatches = numMatchesMap.get(key);

            if (numMatches == maxNumMatches) {
                requestHandlerCollector.add(requestHandlerMap.get(key));
                noOfMaxMatchesCount++;
            }
        }

        return noOfMaxMatchesCount;
    }
}
