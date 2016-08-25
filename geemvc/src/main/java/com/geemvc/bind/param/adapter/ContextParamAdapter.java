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

package com.geemvc.bind.param.adapter;

import com.geemvc.Bindings;
import com.geemvc.ThreadStash;
import com.geemvc.annotation.Adapter;
import com.geemvc.bind.param.ParamContext;
import com.geemvc.bind.param.TypedParamAdapter;
import com.geemvc.i18n.message.Messages;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.reflect.ReflectionProvider;
import com.geemvc.validation.Errors;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Singleton
@Adapter
public class ContextParamAdapter implements TypedParamAdapter<Context> {

    protected final ReflectionProvider reflectionProvider;

    @Inject
    protected Injector injector;

    @Inject
    protected ContextParamAdapter(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    @Override
    public String getName(Context contextParam) {
        return null;
    }

    @Override
    public List<String> getValue(Context paramAnnotation, String paramName, ParamContext paramCtx) {
        return null;
    }

    @Override
    public Object getTypedValue(Context paramAnnotation, String paramName, ParamContext paramCtx) {
        Class<?> paramType = paramCtx.methodParam().type();

        HttpServletRequest request = (HttpServletRequest) paramCtx.requestCtx().getRequest();

        if (ServletRequest.class.isAssignableFrom(paramType)) {
            return request;
        } else if (ServletResponse.class.isAssignableFrom(paramType)) {
            return paramCtx.requestCtx().getResponse();
        } else if (ServletContext.class.isAssignableFrom(paramType)) {
            return request.getServletContext();
        } else if (HttpSession.class.isAssignableFrom(paramType)) {
            return request.getSession(false);
        } else if (Cookie[].class.isAssignableFrom(paramType)) {
            return request.getCookies();
        } else if (Locale.class.isAssignableFrom(paramType)) {
            return request.getLocale();
        } else if (Errors.class.isAssignableFrom(paramType)) {
            return ThreadStash.get(Errors.class);
        } else if (Notices.class.isAssignableFrom(paramType)) {
            return ThreadStash.get(Notices.class);
        } else if (Messages.class.isAssignableFrom(paramType)) {
            return injector.getInstance(Messages.class);
        } else if (Bindings.class.isAssignableFrom(paramType)) {
            return injector.getInstance(Bindings.class).build(paramCtx.requestValues(), paramCtx.typedValues(), (Errors) ThreadStash.get(Errors.class), (Notices) ThreadStash.get(Notices.class));
        } else if (Map.class.isAssignableFrom(paramType)) {
            Type paramGenericType = paramCtx.methodParam().parameterizedType();
            List<Class<?>> genericType = reflectionProvider.getGenericType(paramGenericType);

            if (genericType != null && genericType.size() == 2 && String.class == genericType.get(0) && String[].class == genericType.get(1)) {
                return request.getParameterMap();
            } else {
                throw new IllegalStateException("The @Context annotation only support a map of type Map<String, String[]> which provides the request parameter map.");
            }
        } else {
            throw new IllegalStateException("The @Context annotation does not support the type: " + paramType.getName());
        }
    }
}
