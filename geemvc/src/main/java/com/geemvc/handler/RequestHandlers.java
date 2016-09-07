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

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.geemvc.RequestContext;
import com.geemvc.annotation.Request;
import com.geemvc.matcher.MatcherContext;
import com.geemvc.view.bean.Result;

public interface RequestHandlers {
    RequestHandler toRequestHandler(Result result);

    HandlerResolutionPlan handlerResolutionPlan(RequestHandler requestHandler, RequestContext requestCtx);

    Collection<String> headers(Request requestMapping);

    Collection<String> consumes(Request requestMapping);

    String consumes(Request requestMapping, RequestContext requestCtx);

    Collection<String> produces(Request requestMapping);

    String produces(Request requestMapping, RequestContext requestCtx);

    RequestHandler mostMatchingPathParameters(RequestContext requestCtx, List<RequestHandler> foundHandlers);

    boolean anyHandlersHavePriority(Collection<RequestHandler> requestHandlerCollector);

    void sortByPriority(List<RequestHandler> requestHandlerCollector);

    boolean parametersMatch(Request requestMapping, RequestContext requestCtx, MatcherContext matcherCtx);

    boolean headersMatch(Request requestMapping, RequestContext requestCtx, MatcherContext matcherCtx);

    boolean consumesMatch(Request requestMapping, RequestContext requestCtx);

    boolean producesMatch(Request requestMapping, RequestContext requestCtx);

    boolean cookiesMatch(Request requestMapping, RequestContext requestCtx, MatcherContext matcherCtx);

    boolean handleScriptMatches(Request requestMapping, RequestContext requestCtx, MatcherContext matcherCtx);

    boolean hasRequestMethod(Request requestMapping, RequestContext requestCtx);

    boolean hasRequestMethod(Request requestMapping, RequestContext requestCtx, boolean emptyIsMatch);

    boolean isIgnore(Request requestMapping, RequestContext requestCtx);

    RequestHandler findBestMatchByPath(RequestContext requestCtx, List<RequestHandler> requestHandlers, List<RequestHandler> requestHandlerCollector);

    RequestHandler findBestMatch(RequestContext requestCtx, List<RequestHandler> requestHandlers, Function<HandlerResolutionPlan, Integer> numTotalMatchesFunction, Function<HandlerResolutionPlan, Integer> numTotalStaticMatchesFunction, Function<HandlerResolutionPlan, Integer> numTotalStaticNegateMatchesFunction, List<RequestHandler> requestHandlerCollector);

    RequestHandler findBestMatchForHttpMethod(RequestContext requestCtx, List<RequestHandler> requestHandlers, List<RequestHandler> requestHandlerCollector);

    RequestHandler findBestMatchForMimeTypes(RequestContext requestCtx, List<RequestHandler> requestHandlers, List<RequestHandler> requestHandlerCollector, boolean singleExactMatchOnly, boolean firstMatch);

    RequestHandler findBestMatchForContentTypeHeader(RequestContext requestCtx, List<RequestHandler> requestHandlers, List<RequestHandler> requestHandlerCollector, boolean singleExactMatchOnly);
}
