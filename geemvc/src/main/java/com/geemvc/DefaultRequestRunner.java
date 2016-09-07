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

package com.geemvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ext.RuntimeDelegate;

import com.geemvc.annotation.Request;
import com.geemvc.bind.MethodParam;
import com.geemvc.bind.MethodParams;
import com.geemvc.config.Configuration;
import com.geemvc.config.Configurations;
import com.geemvc.handler.CompositeControllerResolver;
import com.geemvc.handler.CompositeHandlerResolver;
import com.geemvc.handler.HandlerNotFoundException;
import com.geemvc.handler.RequestHandler;
import com.geemvc.handler.RequestHandlers;
import com.geemvc.helper.UriBuilder;
import com.geemvc.i18n.locale.LocaleResolver;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.intercept.Interceptors;
import com.geemvc.intercept.LifecycleContext;
import com.geemvc.intercept.annotation.PostBinding;
import com.geemvc.intercept.annotation.PostHandle;
import com.geemvc.intercept.annotation.PostValidation;
import com.geemvc.intercept.annotation.PreBinding;
import com.geemvc.intercept.annotation.PreHandle;
import com.geemvc.intercept.annotation.PreValidation;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.matcher.PathMatcher;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.validation.Errors;
import com.geemvc.validation.ValidationContext;
import com.geemvc.validation.Validator;
import com.geemvc.validation.ViewOnlyRequestHandler;
import com.geemvc.view.GeemvcKey;
import com.geemvc.view.ViewHandler;
import com.geemvc.view.bean.Result;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultRequestRunner implements RequestRunner {
    protected final Configuration configuration = Configurations.get();
    protected final CompositeControllerResolver controllerResolver;
    protected final CompositeHandlerResolver handlerResolver;
    protected final RequestHandlers requestHandlers;
    protected final LocaleResolver localeResolver;
    protected final Interceptors interceptors;
    protected final MethodParams methodParams;
    protected final Validator validator;
    protected final ViewHandler viewHandler;

    @Inject
    protected Injector injector;

    @Logger
    protected Log log;

    @Inject
    public DefaultRequestRunner(CompositeControllerResolver controllerResolver, CompositeHandlerResolver handlerResolver, RequestHandlers requestHandlers, LocaleResolver localeResolver, Interceptors interceptors,
            MethodParams methodParams, Validator Validator, ViewHandler viewHandler) {

        this.controllerResolver = controllerResolver;
        this.handlerResolver = handlerResolver;
        this.requestHandlers = requestHandlers;
        this.localeResolver = localeResolver;
        this.interceptors = interceptors;
        this.methodParams = methodParams;
        this.validator = Validator;
        this.viewHandler = viewHandler;
    }

    @Override
    public void process(RequestContext requestCtx) throws Exception {
        ThreadStash.put(RequestContext.class, requestCtx);

        initJaxRsRuntime();

        // Create a new Error instance for collecting errors.
        Errors errors = injector.getInstance(Errors.class);
        // Create a new Notices instance for collecting notice information.
        Notices notices = injector.getInstance(Notices.class);

        // Add errors and notices to thread local stash so that they can be retrieved in taglibs etc.
        ThreadStash.put(Errors.class, errors);
        ThreadStash.put(Notices.class, notices);

        RequestHandler requestHandler = null;

        try {
            // Find the request handler for the current request.
            requestHandler = resolveHandler(requestCtx);

            // Process the locale for this request and set the character encoding.
            processLocale(requestCtx);

            // Add context attributes to request for later use in taglibs etc.
            setContextAttributes(requestCtx, errors, notices);

            // Process the resolved request-handler.
            Result result = processRequestHandler(requestHandler, requestCtx, errors, notices);

            // Adds the content type to the response.
            setContentType(result, requestHandler, requestCtx);

            // Adds cache headers to the current response.
            setCacheHeaders(result, requestCtx);

            if (result != null) {
                // Process the view that resulted from invoking the request-handler method.
                processView(result, requestCtx);
            } else {
                processEmptyView(requestCtx);
            }
        } catch (HandlerNotFoundException e) {
            handle404(requestCtx);
            return;
        }
    }

    protected void processView(Result result, RequestContext requestCtx) throws ServletException, IOException {
        viewHandler.handle(result, requestCtx);
    }

    protected void processEmptyView(RequestContext requestCtx) throws ServletException, IOException {
        ((HttpServletResponse) requestCtx.getResponse()).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    protected Result processRequestHandler(RequestHandler requestHandler, RequestContext requestCtx, Errors errors, Notices notices) throws HandlerNotFoundException {

        LifecycleContext lifecycleCtx = injector.getInstance(LifecycleContext.class).build(requestHandler, requestCtx, errors, notices);
        ThreadStash.put(LifecycleContext.class, lifecycleCtx);

        // ---------- Intercept lifecycle: PreBinding.
        Result preBindingResult = result(interceptors.interceptLifecycle(PreBinding.class, lifecycleCtx));
        lifecycleCtx.result(preBindingResult);

        Bindings bindings = bindings(requestHandler, requestCtx, errors, notices);
        lifecycleCtx.bindings(bindings);

        // ---------- Intercept lifecycle: PostBinding.
        Result postBindingResult = result(interceptors.interceptLifecycle(PostBinding.class, lifecycleCtx));

        if (postBindingResult != null) {
            lifecycleCtx.result(postBindingResult);
        }

        // ---------- Intercept lifecycle: PreValidation.
        Result preValidationResult = result(interceptors.interceptLifecycle(PreValidation.class, lifecycleCtx));

        if (preValidationResult != null) {
            lifecycleCtx.result(preValidationResult);
        }

        // Execute pre-handler validation.
        Result preHandlerValidationResult = preHandlerValidation(requestHandler, bindings.typedValues(), requestCtx, errors, notices);

        if (preHandlerValidationResult != null) {
            lifecycleCtx.result(preHandlerValidationResult).invokeHandler(false);
        }

        // ---------- Intercept lifecycle: PostValidation.
        Result postValidationResult = result(interceptors.interceptLifecycle(PostValidation.class, lifecycleCtx));

        if (postValidationResult != null) {
            lifecycleCtx.result(postValidationResult);
        }

        if (lifecycleCtx.result() != null) {
            return result(intercept(injector.getInstance(ViewOnlyRequestHandler.class).build(lifecycleCtx.result(), requestHandler), bindings.typedValues(), requestCtx, errors, notices));
        } else {
            return invokeRequestHandler(requestHandler, requestCtx, errors, notices, bindings.typedValues(), lifecycleCtx);
        }
    }

    protected Result invokeRequestHandler(RequestHandler requestHandler, RequestContext requestCtx, Errors errors, Notices notices, Map<String, Object> typedValues, LifecycleContext lifecycleCtx) throws HandlerNotFoundException {
        // Ensure that we do not end up in an endless handler execution loop.
        checkHandlerIncovationCount(requestHandler, requestCtx);

        Result preHandleResult = result(interceptors.interceptLifecycle(PreHandle.class, lifecycleCtx));

        if (preHandleResult != null) {
            lifecycleCtx.result(preHandleResult);
        }

        if (lifecycleCtx.isInvokeHandler()) {
            Result handlerResult = result(intercept(requestHandler, typedValues, requestCtx, errors, notices));

            log.debug("Request handler returned view '{}'.", () -> handlerResult);

            if (handlerResult != null) {
                lifecycleCtx.result(handlerResult);
            }
        }

        Result postHandleResult = result(interceptors.interceptLifecycle(PostHandle.class, lifecycleCtx));

        if (postHandleResult != null) {
            lifecycleCtx.result(postHandleResult);
        }

        // Check if the return object is leading to another request handler instead of a view.
        if (isInvokeNextHandler(lifecycleCtx.result())) {
            Result result = lifecycleCtx.result();

            // Locate a request handler for the returned result object.
            RequestHandler nextRequestHandler = requestHandlers.toRequestHandler(result);

            // If a pathMatcher exists, we'll try to extract the parameters from the handler path.
            Map<String, String[]> pathParameters = null;
            if (result.handlerPath() != null)
                pathParameters = nextRequestHandler.pathMatcher().parameters(result.handlerPath());

            // Merge the parameters of the result object together with the extracted path parameters.
            Map<String, String[]> parameters = mergeParameters(pathParameters, result.parameters());

            // Now we can build the new URI composed of a mapped path and parameters that may have been passed to the result object.
            String nextRequestURI = requestURI(result, nextRequestHandler, parameters);

            // We need to create an internal RequestContext here so that the new requestURI and parameters are used when invoking the request handler.
            RequestContext internalRequestCtx = injector.getInstance(InternalRequestContext.class).build(nextRequestURI, result.handlerMethod(), parameters, requestCtx.getRequest(), requestCtx.getResponse(), requestCtx.getServletContext())
                    .requestHandler(nextRequestHandler);

            // Make sure that parameters, headers and cookie values etc. are compatible with the request handler.
            if (requestHandlers.handlerResolutionPlan(nextRequestHandler, internalRequestCtx).isCompatible()) {
                Bindings bindings = bindings(nextRequestHandler, internalRequestCtx, errors, notices);
                lifecycleCtx.bindings(bindings);

                // Now we are ready to invoke.
                return invokeRequestHandler(nextRequestHandler, internalRequestCtx, errors, notices, bindings.typedValues(), lifecycleCtx);
            } else {
                log.debug("Unable to find the handler '" + nextRequestURI + "' in the handler chain. Ensure that all the values (i.e. path, parameters, headers etc.) match a valid request handler. The initial request was '"
                        + requestCtx.getMethod() + " " + requestCtx.getPath() + "'");

                throw new HandlerNotFoundException();
            }
        } else {
            return lifecycleCtx.result();
        }
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

    protected boolean isInvokeNextHandler(Result result) {
        return result != null && (!Str.isEmpty(result.handlerPath()) || !Str.isEmpty(result.uniqueHandler()) || (result.controllerClass() != null && !Str.isEmpty(result.handlerMethod())));
    }

    protected void checkHandlerIncovationCount(RequestHandler requestHandler, RequestContext requestCtx) {
        Integer handlerExecCount = (Integer) requestCtx.getAttribute("handler-exec-count");

        if (handlerExecCount != null && handlerExecCount > 100)
            throw new IllegalStateException(
                    "Possibly detected an endless handler chain execution. The first path in the chain was '" + requestCtx.getPath() + "'. The current request handler that will not be executed is: " + requestHandler.toGenericString());

        requestCtx.setAttribute("handler-exec-count", handlerExecCount == null ? 1 : ++handlerExecCount);
    }

    protected boolean onErrorViewExists(RequestHandler requestHandler) {
        return onErrorView(requestHandler) != null;
    }

    protected String onErrorView(RequestHandler requestHandler) {
        Request controllerRequestMapping = requestHandler.controllerRequestMapping();
        Request handlerRequestMapping = requestHandler.handlerRequestMapping();

        if (!Str.isEmpty(handlerRequestMapping.onError())) {
            String onError = handlerRequestMapping.onError().trim();

            if (!onError.startsWith("view:")) {
                onError = "view:" + onError;
            }
            return onError;
        }

        if (!Str.isEmpty(controllerRequestMapping.onError())) {
            String onError = controllerRequestMapping.onError().trim();

            if (!onError.startsWith("view:")) {
                onError = "view:" + onError;
            }
            return onError;
        }

        return null;
    }

    protected Object intercept(RequestHandler targetRequestHandler, Map<String, Object> targetArgs, RequestContext requestCtx, Errors errors, Notices notices) {
        return interceptors.intercept(targetRequestHandler, targetArgs, requestCtx, errors, notices);
    }

    protected Result preHandlerValidation(RequestHandler requestHandler, Map<String, Object> typedValues, RequestContext requestCtx, Errors errors, Notices notices) {
        ValidationContext validationCtx = injector.getInstance(ValidationContext.class).build(requestCtx, typedValues, notices);
        Object result = validator.validate(requestHandler, validationCtx, errors);

        Result errorResult = null;

        if (result != null) {
            errorResult = result(result);
        } else if (!errors.isEmpty() && onErrorViewExists(requestHandler)) {
            errorResult = result(onErrorView(requestHandler));
        }

        // If result and errors exist, re-bind values to the result object if they do not exist yet.
        if (errorResult != null && !errors.isEmpty()) {
            for (Map.Entry<String, Object> entry : typedValues.entrySet()) {
                if (!errorResult.containsBinding(entry.getKey())) {
                    errorResult.bind(entry.getKey(), entry.getValue());
                }
            }
        }

        return errorResult;
    }

    protected RequestHandler resolveHandler(RequestContext requestCtx) throws HandlerNotFoundException {
        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(requestCtx);

        if (controllers == null || controllers.isEmpty())
            throw new HandlerNotFoundException("No controller found for path '" + requestCtx.getPath() + "'");

        RequestHandler requestHandler = handlerResolver.resolve(requestCtx, controllers.values());

        log.debug("Found request handler '{}'.", () -> requestHandler);

        if (requestHandler == null) {
            StringBuilder message = new StringBuilder("geeMVC was unable to find a unique request-handler for the path '" + requestCtx.getPath() + "'.\n");

            if (!controllers.isEmpty()) {
                Collection<Class<?>> values = controllers.values();

                message.append("The following controllers were found that may contain the request-handler method: ");

                int x = 0;
                for (Class<?> controllerClass : values) {
                    if (x > 0)
                        message.append(", ");

                    message.append(controllerClass.getName());

                    x++;
                }

                message.append(Char.DOT);
                message.append("\nIf a handler exists with the matching path, then make sure that all other settings like http-method, consumes, produces, parameters, headers and cookies also match.");
            } else {
                message.append("Please check your annotated controller classes as none of them seem to match your requestURI.");
            }

            log.debug(message.toString());

            throw new HandlerNotFoundException();
        }

        requestCtx.requestHandler(requestHandler);

        return requestHandler;
    }

    protected Result result(Object handlerResult) {
        if (handlerResult == null || handlerResult instanceof Result)
            return (Result) handlerResult;

        if (handlerResult instanceof String) {
            String result = ((String) handlerResult).trim();

            if (result.startsWith("view:")) {
                return Results.view(result.substring(8).trim());
            } else if (result.startsWith("redirect:")) {
                return Results.redirect(result.substring(9).trim());
            } else if (result.startsWith("handler:")) {
                return Results.handler(result.substring(8).trim());
            } else if (result.startsWith("status:")) {
                String statusAndMessage = result.substring(7).trim();
                int pos = statusAndMessage.indexOf(Char.SPACE);
                if (pos != -1) {
                    return Results.status(Integer.valueOf(statusAndMessage.substring(0, pos)), statusAndMessage.substring(pos + 1).trim());

                } else {
                    return Results.status(Integer.valueOf(statusAndMessage));
                }
            } else {
                // Content-type is set automatically later.
                return Results.stream((String) null, result);
            }
        } else {
            return Results.stream(null, handlerResult);
        }
    }

    protected Bindings bindings(RequestHandler requestHandler, RequestContext requestCtx, Errors errors, Notices notices) {
        // Get the parameters of the request handler method.
        List<MethodParam> methodParameters = methodParams.get(requestHandler, requestCtx);

        // Fetch the String request values for the fetched method parameters.
        Map<String, List<String>> requestValues = methodParams.values(methodParameters, requestCtx);

        // Now we convert the string parameters to the appropriate types.
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, methodParameters, requestCtx);

        return injector.getInstance(Bindings.class).build(requestValues, typedValues, errors, notices);
    }

    protected void processLocale(RequestContext requestCtx) {
        HttpServletRequest request = (HttpServletRequest) requestCtx.getRequest();
        HttpServletResponse response = (HttpServletResponse) requestCtx.getResponse();

        Locale locale = localeResolver.resolve(requestCtx);

        String characterEncoding = configuration.characterEncodingFor(locale);

        log.debug("Using locale '{}' and character encoding '{}'.", () -> locale, () -> characterEncoding);

        try {
            request.setCharacterEncoding(characterEncoding);
            requestCtx.currentLocale(locale);
            response.setCharacterEncoding(characterEncoding);
            response.setLocale(locale);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void setContentType(Result result, RequestHandler requestHandler, RequestContext requestCtx) {
        HttpServletResponse response = (HttpServletResponse) requestCtx.getResponse();

        if (result != null && !Str.isEmpty(result.characterEncoding())) {
            log.debug("Using contentType '{}' from view object.", () -> result.characterEncoding());
            response.setContentType(result.characterEncoding());
            return;
        }

        String produces = requestHandlers.produces(requestHandler.handlerRequestMapping(), requestCtx);

        if (!Str.isEmpty(produces)) {
            log.debug("Using contentType '{}' from mapped 'produces'.", () -> produces);
            response.setContentType(produces);
        } else if (result != null) {
            log.debug("Using contentType '{}' from configuration'.", () -> configuration.defaultContentType());
            response.setContentType(configuration.defaultContentType());
        }
    }

    protected void setCacheHeaders(Result result, RequestContext requestCtx) {
        // TODO
    }

    protected void setContextAttributes(RequestContext requestCtx, Errors errors, Notices notices) {
        RequestHandler rh = requestCtx.requestHandler();
        PathMatcher pm = requestCtx.requestHandler().pathMatcher();

        HttpServletRequest request = (HttpServletRequest) requestCtx.getRequest();
        request.setAttribute(GeemvcKey.REQUEST_CONTEXT, requestCtx);
        request.setAttribute(GeemvcKey.CONTROLLER_CLASS, rh.controllerClass());
        request.setAttribute(GeemvcKey.HANDLER_METHOD, rh.handlerMethod());
        request.setAttribute(GeemvcKey.REQUEST_HANDLER, rh);
        request.setAttribute(GeemvcKey.RESOLVED_PATH, pm.getMappedPath());
        request.setAttribute(GeemvcKey.VALIDATION_ERRORS, errors);
        request.setAttribute(GeemvcKey.NOTICES, notices);

        if (pm.parameterCount() > 0 && pm.parameterExists("id")) {
            Map<String, String[]> params = pm.parameters(requestCtx.getPath());

            String[] id = params.get("id");

            if (id != null && id.length > 0) {
                request.setAttribute(GeemvcKey.RESOLVED_ID_PARAM, id[0]);
            } else {
                String _id = request.getParameter("id");

                if (_id != null) {
                    request.setAttribute(GeemvcKey.RESOLVED_ID_PARAM, _id);
                }
            }
        }
    }

    protected void handle404(RequestContext requestCtx) throws ServletException, IOException {
        ((HttpServletResponse) requestCtx.getResponse()).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    protected void initJaxRsRuntime() {
        if (configuration.isJaxRsEnabled()) {
            // JAX-RS runtime delegate.
            log.debug("Initializging JAX-RS Runtime.");
            RuntimeDelegate.setInstance(injector.getInstance(RuntimeDelegate.class));
        } else {
            log.debug("JAX-RS Runtime disabled by configuration.");
        }
    }
}
