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

package com.cb.geemvc;

import com.cb.geemvc.annotation.Request;
import com.cb.geemvc.bind.MethodParam;
import com.cb.geemvc.bind.MethodParams;
import com.cb.geemvc.config.Configuration;
import com.cb.geemvc.handler.CompositeControllerResolver;
import com.cb.geemvc.handler.CompositeHandlerResolver;
import com.cb.geemvc.handler.RequestHandler;
import com.cb.geemvc.i18n.locale.LocaleResolver;
import com.cb.geemvc.i18n.notice.Notices;
import com.cb.geemvc.intercept.Interceptors;
import com.cb.geemvc.intercept.LifecycleContext;
import com.cb.geemvc.intercept.annotation.*;
import com.cb.geemvc.logging.Log;
import com.cb.geemvc.logging.annotation.Logger;
import com.cb.geemvc.matcher.PathMatcher;
import com.cb.geemvc.matcher.PathMatcherKey;
import com.cb.geemvc.validation.Errors;
import com.cb.geemvc.validation.ValidationContext;
import com.cb.geemvc.validation.Validator;
import com.cb.geemvc.validation.ViewOnlyRequestHandler;
import com.cb.geemvc.view.GeemvcKey;
import com.cb.geemvc.view.ViewHandler;
import com.cb.geemvc.view.bean.View;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DefaultRequestRunner implements RequestRunner {
    protected final Configuration configuration;
    protected final CompositeControllerResolver controllerResolver;
    protected final CompositeHandlerResolver handlerResolver;
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
    public DefaultRequestRunner(Configuration configuration, CompositeControllerResolver controllerResolver, CompositeHandlerResolver handlerResolver, LocaleResolver localeResolver, Interceptors interceptors,
                                MethodParams methodParams, Validator Validator, ViewHandler viewHandler) {

        this.configuration = configuration;
        this.controllerResolver = controllerResolver;
        this.handlerResolver = handlerResolver;
        this.interceptors = interceptors;
        this.localeResolver = localeResolver;
        this.methodParams = methodParams;
        this.validator = Validator;
        this.viewHandler = viewHandler;
    }

    @Override
    public void process(RequestContext requestCtx) throws Exception {
        // Create a new Error instance for collecting errors.
        Errors errors = injector.getInstance(Errors.class);
        // Create a new Notices instance for collecting notice information.
        Notices notices = injector.getInstance(Notices.class);

        // Add errors and notices to thread local stash so that they can be retrieved in taglibs etc.
        ThreadStash.put(Errors.class, errors);
        ThreadStash.put(Notices.class, notices);

        // Find the request handler for the current request.
        RequestHandler requestHandler = resolveHandler(requestCtx);

        // Process the locale for this request and set the character encoding.
        processLocale(requestCtx);

        // Add context attributes to request for later use in taglibs etc.
        setContextAttributes(requestCtx, errors, notices);

        // Process the resolved request-handler.
        View view = processRequestHandler(requestHandler, requestCtx, errors, notices);

        // Adds the content type to the response.
        setContentType(view, requestHandler, requestCtx);

        // Adds cache headers to the current response.
        setCacheHeaders(view, requestCtx);

        if (view != null) {
            // Process the view that resulted from invoking the request-handler method.
            processView(view, requestCtx);
        }
    }

    protected void processView(View view, RequestContext requestCtx) throws ServletException, IOException {
        viewHandler.handle(view, requestCtx);
    }

    protected View processRequestHandler(RequestHandler requestHandler, RequestContext requestCtx, Errors errors, Notices notices) {

        LifecycleContext lifecycleCtx = injector.getInstance(LifecycleContext.class).build(requestHandler, requestCtx, errors, notices);
        ThreadStash.put(LifecycleContext.class, lifecycleCtx);

        // ---------- Intercept lifecycle: PreBinding.
        View preBindingView = view(interceptors.interceptLifecycle(PreBinding.class, lifecycleCtx));
        lifecycleCtx.view(preBindingView);

        // Get the parameters of the request handler method.
        List<MethodParam> methodParameters = methodParams.get(requestHandler, requestCtx);

        // Fetch the String request values for the fetched method parameters.
        Map<String, List<String>> requestValues = methodParams.values(methodParameters, requestCtx);

        // Now we convert the string parameters to the appropriate types.
        Map<String, Object> typedValues = methodParams.typedValues(requestValues, methodParameters, requestCtx);

        Bindings bindings = injector.getInstance(Bindings.class).build(requestValues, typedValues, errors, notices);
        lifecycleCtx.bindings(bindings);

        // ---------- Intercept lifecycle: PostBinding.
        View postBindingView = view(interceptors.interceptLifecycle(PostBinding.class, lifecycleCtx));

        if (postBindingView != null) {
            lifecycleCtx.view(postBindingView);
        }

        // ---------- Intercept lifecycle: PreValidation.
        View preValidationView = view(interceptors.interceptLifecycle(PreValidation.class, lifecycleCtx));

        if (preValidationView != null) {
            lifecycleCtx.view(preValidationView);
        }

        // Execute pre-handler validation.
        View preHandlerValidationView = preHandlerValidation(requestHandler, typedValues, requestCtx, errors, notices);

        if (preHandlerValidationView != null) {
            lifecycleCtx.view(preHandlerValidationView).invokeHandler(false);
        }

        // ---------- Intercept lifecycle: PostValidation.
        View postValidationView = view(interceptors.interceptLifecycle(PostValidation.class, lifecycleCtx));

        if (postValidationView != null) {
            lifecycleCtx.view(postValidationView);
        }

        if (lifecycleCtx.view() != null) {
            return view(intercept(injector.getInstance(ViewOnlyRequestHandler.class).build(lifecycleCtx.view(), requestHandler), typedValues, requestCtx, errors, notices));
        } else {
            View preHandleView = view(interceptors.interceptLifecycle(PreHandle.class, lifecycleCtx));

            if (preHandleView != null) {
                lifecycleCtx.view(preHandleView);
            }

            if (lifecycleCtx.isInvokeHandler()) {
                View handlerView = view(intercept(requestHandler, typedValues, requestCtx, errors, notices));

                log.debug("Request handler returned view '{}'.", () -> handlerView);

                if (handlerView != null) {
                    lifecycleCtx.view(handlerView);
                }
            }

            View postHandleView = view(interceptors.interceptLifecycle(PostHandle.class, lifecycleCtx));

            if (postHandleView != null) {
                lifecycleCtx.view(postHandleView);
            }

            return lifecycleCtx.view();
        }
    }

    protected boolean onErrorViewExists(RequestHandler requestHandler) {
        return onErrorView(requestHandler) != null;
    }

    protected String onErrorView(RequestHandler requestHandler) {
        Request controllerRequestMapping = requestHandler.controllerRequestMapping();
        Request handlerRequestMapping = requestHandler.handlerRequestMapping();

        if (!Str.isEmpty(handlerRequestMapping.onError()))
            return handlerRequestMapping.onError();

        if (!Str.isEmpty(controllerRequestMapping.onError()))
            return controllerRequestMapping.onError();

        return null;
    }

    protected Object intercept(RequestHandler targetRequestHandler, Map<String, Object> targetArgs, RequestContext requestCtx, Errors errors, Notices notices) {
        return interceptors.intercept(targetRequestHandler, targetArgs, requestCtx, errors, notices);
    }

    protected View preHandlerValidation(RequestHandler requestHandler, Map<String, Object> typedValues, RequestContext requestCtx, Errors errors, Notices notices) {
        ValidationContext validationCtx = injector.getInstance(ValidationContext.class).build(requestCtx, typedValues, notices);
        Object view = validator.validate(requestHandler, validationCtx, errors);

        if (view != null) {
            return view(view);
        } else if (!errors.isEmpty() && onErrorViewExists(requestHandler)) {
            View errorView = view(onErrorView(requestHandler));
            errorView.bind(typedValues);

            return errorView;
        }

        return null;
    }

    protected RequestHandler resolveHandler(RequestContext requestCtx) {
        Map<PathMatcherKey, Class<?>> controllers = controllerResolver.resolve(requestCtx);

        if (controllers == null || controllers.isEmpty())
            throw new IllegalStateException("No controller found for path '" + requestCtx.getPath() + "'");

        RequestHandler requestHandler = handlerResolver.resolve(requestCtx, controllers.values());

        log.debug("Found request handler '{}'.", () -> requestHandler);

        if (requestHandler == null) {
            StringBuilder message = new StringBuilder("Geemvc was unable to find a unique request-handler for the path '" + requestCtx.getPath() + "'.\n");

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

            throw new IllegalStateException(message.toString());
        }

        requestCtx.requestHandler(requestHandler);

        return requestHandler;
    }

    protected View view(Object handlerResult) {
        if (handlerResult == null || handlerResult instanceof View)
            return (View) handlerResult;

        if (handlerResult instanceof String) {
            String result = (String) handlerResult;

            if (result.trim().startsWith("forward:")) {
                return Views.forward(result.substring(8).trim());
            } else if (result.trim().startsWith("redirect:")) {
                return Views.redirect(result.substring(9).trim());
            }
        }

        return null;
    }

    protected void processLocale(RequestContext requestCtx) {
        HttpServletRequest request = (HttpServletRequest) requestCtx.getRequest();
        HttpServletResponse response = (HttpServletResponse) requestCtx.getResponse();

        Locale locale = localeResolver.resolve(requestCtx);

        String characterEncoding = configuration.characterEncodingFor(locale);

        log.debug("Using locale '{}' and character encofing '{}'.", () -> locale, () -> characterEncoding);

        try {
            request.setCharacterEncoding(characterEncoding);
            requestCtx.currentLocale(locale);
            response.setCharacterEncoding(characterEncoding);
            response.setLocale(locale);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void setContentType(View view, RequestHandler requestHandler, RequestContext requestCtx) {
        HttpServletResponse response = (HttpServletResponse) requestCtx.getResponse();

        if (view != null && !Str.isEmpty(view.characterEncoding())) {
            log.debug("Using contentType '{}' from view object.", () -> view.characterEncoding());
            response.setContentType(view.characterEncoding());
        } else if (!Str.isEmpty(requestHandler.produces())) {
            log.debug("Using contentType '{}' from mapped 'produces'.", () -> requestHandler.produces());
            response.setContentType(requestHandler.produces());
        } else {
            log.debug("Using contentType '{}' from configuration'.", () -> configuration.defaultContentType());
            response.setContentType(configuration.defaultContentType());
        }
    }

    protected void setCacheHeaders(View view, RequestContext requestCtx) {
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
            Map<String, String[]> params = pm.parameters(requestCtx);

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
}
