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

import java.util.HashMap;
import java.util.Map;

import com.geemvc.InternalRequestContext;
import com.geemvc.RequestContext;
import com.geemvc.Str;
import com.geemvc.helper.UriBuilder;
import com.geemvc.view.bean.Result;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultRequestHandlerInfo implements RequestHandlerInfo {
    protected RequestHandler requestHandler;
    protected String requestURI;
    protected Map<String, String[]> pathParameters;
    protected Map<String, String[]> parameters;
    protected RequestContext internalRequestContext;
    protected HandlerResolutionPlan handlerResolutionPlan;

    protected final RequestHandlers requestHandlers;

    @Inject
    protected Injector injector;

    @Inject
    public DefaultRequestHandlerInfo(RequestHandlers requestHandlers) {
        this.requestHandlers = requestHandlers;
    }

    @Override
    public RequestHandlerInfo from(Result result, RequestContext requestCtx) {
        if (result == null || requestCtx == null)
            throw new IllegalStateException("The Result and RequestContext objects are required when locating relevant request handler information");

        // Locate a request handler for the returned result object.
        requestHandler = requestHandlers.toRequestHandler(result);

        // If a pathMatcher exists, we'll try to extract the parameters from the handler path.
        if (result.handlerPath() != null)
            pathParameters = requestHandler.pathMatcher().parameters(result.handlerPath());

        // Merge the parameters of the result object together with the extracted path parameters.
        parameters = mergeParameters(pathParameters, result.parameters());

        // Now we can build the new URI composed of a mapped path and parameters that may have been passed to the result object.
        requestURI = requestURI(result, requestHandler, parameters);

        // We need to create an internal RequestContext here so that the new requestURI and parameters are used when invoking the request handler.
        internalRequestContext = injector.getInstance(InternalRequestContext.class)
                .build(requestURI, result.handlerMethod(), parameters, requestCtx.getRequest(), requestCtx.getResponse(), requestCtx.getServletContext())
                .requestHandler(requestHandler);

        // Make sure that parameters, headers and cookie values etc. are compatible with the request handler.
        handlerResolutionPlan = requestHandlers.handlerResolutionPlan(requestHandler, internalRequestContext);

        return this;
    }

    @Override
    public RequestHandler requestHandler() {
        return requestHandler;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public Map<String, String[]> pathParameters() {
        return pathParameters;
    }

    @Override
    public Map<String, String[]> parameters() {
        return parameters;
    }

    @Override
    public RequestContext internalRequestContext() {
        return internalRequestContext;
    }

    @Override
    public HandlerResolutionPlan handlerResolutionPlan() {
        return handlerResolutionPlan;
    }

    @Override
    public boolean isCompatible() {
        return handlerResolutionPlan == null ? false : handlerResolutionPlan.isCompatible();
    }

    protected String requestURI(Result result, RequestHandler requestHandler, Map<String, String[]> parameters) {
        String requestURI = result.handlerPath();

        if (requestURI == null)
            requestURI = injector.getInstance(UriBuilder.class).build(requestHandler.controllerClass(), requestHandler.handlerMethod(), parameters);

        return requestURI;
    }

    protected Map<String, String[]> mergeParameters(Map<String, String[]> pathParameters, Map<String, Object> resultParameters) {
        Map<String, String[]> mergedParameters = new HashMap<>();

        if (resultParameters != null) {
            for (Map.Entry<String, Object> entry : resultParameters.entrySet()) {
                if (Str.isEmpty(entry.getKey()))
                    continue;

                if (entry.getValue() != null) {
                    mergedParameters.put(entry.getKey(), new String[] { String.valueOf(entry.getValue()) });
                } else {
                    mergedParameters.put(entry.getKey(), Str.EMPTY_ARRAY);
                }
            }
        }

        if (pathParameters != null)
            mergedParameters.putAll(pathParameters);

        return mergedParameters;
    }
}
