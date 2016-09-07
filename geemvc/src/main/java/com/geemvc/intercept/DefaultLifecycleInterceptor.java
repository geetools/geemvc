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

package com.geemvc.intercept;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.geemvc.Bindings;
import com.geemvc.RequestContext;
import com.geemvc.Str;
import com.geemvc.helper.Annotations;
import com.geemvc.i18n.message.Messages;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.intercept.annotation.Lifecycle;
import com.geemvc.intercept.annotation.PostBinding;
import com.geemvc.intercept.annotation.PostHandle;
import com.geemvc.intercept.annotation.PostValidation;
import com.geemvc.intercept.annotation.PostView;
import com.geemvc.intercept.annotation.PreBinding;
import com.geemvc.intercept.annotation.PreHandle;
import com.geemvc.intercept.annotation.PreValidation;
import com.geemvc.intercept.annotation.PreView;
import com.geemvc.reflect.ReflectionProvider;
import com.geemvc.validation.Errors;
import com.geemvc.view.bean.Result;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Created by Michael on 13.07.2016.
 */
public class DefaultLifecycleInterceptor implements LifecycleInterceptor {
    protected Lifecycle lifecycleAnnotation;
    protected Method interceptMethod;

    @Inject
    protected Injector injector;

    @Override
    public LifecycleInterceptor build(Annotation lifecycleAnnotation, Method interceptMethod) {
        this.lifecycleAnnotation = lifecycleAnnotation(lifecycleAnnotation);
        this.interceptMethod = interceptMethod;

        return this;
    }

    protected Lifecycle lifecycleAnnotation(Annotation annotation) {
        if (annotation instanceof PreBinding) {
            return injector.getInstance(Annotations.class).lifecycleAnnotation((PreBinding) annotation);
        } else if (annotation instanceof PostBinding) {
            return injector.getInstance(Annotations.class).lifecycleAnnotation((PostBinding) annotation);
        } else if (annotation instanceof PreValidation) {
            return injector.getInstance(Annotations.class).lifecycleAnnotation((PreValidation) annotation);
        } else if (annotation instanceof PostValidation) {
            return injector.getInstance(Annotations.class).lifecycleAnnotation((PostValidation) annotation);
        } else if (annotation instanceof PreHandle) {
            return injector.getInstance(Annotations.class).lifecycleAnnotation((PreHandle) annotation);
        } else if (annotation instanceof PostHandle) {
            return injector.getInstance(Annotations.class).lifecycleAnnotation((PostHandle) annotation);
        } else if (annotation instanceof PreView) {
            return injector.getInstance(Annotations.class).lifecycleAnnotation((PreView) annotation);
        } else if (annotation instanceof PostView) {
            return injector.getInstance(Annotations.class).lifecycleAnnotation((PostView) annotation);
        }

        return null;
    }

    @Override
    public Object invoke(LifecycleContext lifecycleCtx) {
        try {
            Type[] parametersTypes = interceptMethod.getGenericParameterTypes();

            if (parametersTypes.length > 0) {
                Object[] args = args(lifecycleCtx);

                return interceptMethod.invoke(instance(), args);
            } else {
                return interceptMethod.invoke(instance(), null);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Lifecycle lifecycleAnnotation() {
        return lifecycleAnnotation;
    }

    protected Object[] args(LifecycleContext lifecycleCtx) {
        List<Object> args = new ArrayList();

        Class<?>[] types = interceptMethod.getParameterTypes();
        Type[] parametersTypes = interceptMethod.getGenericParameterTypes();

        for (int i = 0; i < parametersTypes.length; i++) {
            Class<?> type = types[i];
            Type parameterType = parametersTypes[i];

            List<Class<?>> genericType = injector.getInstance(ReflectionProvider.class).getGenericType(parameterType);
            args.add(arg(type, genericType, lifecycleCtx));
        }

        return args.toArray();
    }

    protected Object arg(Class<?> type, List<Class<?>> genericType, LifecycleContext lifecycleCtx) {
        HttpServletRequest request = (HttpServletRequest) lifecycleCtx.requestCtx().getRequest();

        if (LifecycleContext.class.isAssignableFrom(type)) {
            return lifecycleCtx;
        } else if (RequestContext.class.isAssignableFrom(type)) {
            return lifecycleCtx.requestCtx();
        } else if (ServletRequest.class.isAssignableFrom(type)) {
            return request;
        } else if (ServletResponse.class.isAssignableFrom(type)) {
            return lifecycleCtx.requestCtx().getResponse();
        } else if (ServletContext.class.isAssignableFrom(type)) {
            return request.getServletContext();
        } else if (HttpSession.class.isAssignableFrom(type)) {
            return request.getSession(false);
        } else if (Cookie[].class.isAssignableFrom(type)) {
            return request.getCookies();
        } else if (Locale.class.isAssignableFrom(type)) {
            return request.getLocale();
        } else if (Errors.class.isAssignableFrom(type)) {
            return lifecycleCtx.errors();
        } else if (Notices.class.isAssignableFrom(type)) {
            return lifecycleCtx.notices();
        } else if (Messages.class.isAssignableFrom(type)) {
            return injector.getInstance(Messages.class);
        } else if (Bindings.class.isAssignableFrom(type)) {
            return lifecycleCtx.bindings();
        } else if (Result.class.isAssignableFrom(type)) {
            return lifecycleCtx.result();
        } else if (Map.class.isAssignableFrom(type)) {
            if (genericType != null && genericType.size() == 2 && String.class == genericType.get(0) && String[].class == genericType.get(1)) {
                return request.getParameterMap();
            } else {
                throw new IllegalStateException("The interceptor method only support a map of type Map<String, String[]> which provides the request parameter map.");
            }
        } else {
            throw new IllegalStateException("The interceptor method does not support the type: " + type);
        }
    }

    protected Object instance() {
        return injector.getInstance(interceptMethod.getDeclaringClass());
    }

    @Override
    public String toString() {
        return interceptMethod.getDeclaringClass().getName() + Str.HASH + interceptMethod.getName() + Str.BRACKET_OPEN_CLOSE;
    }
}
