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

package com.geemvc.bind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.geemvc.RequestContext;
import com.geemvc.Str;
import com.geemvc.bind.param.ParamAdapter;
import com.geemvc.bind.param.ParamAdapterFactory;
import com.geemvc.bind.param.ParamContext;
import com.geemvc.bind.param.TypedParamAdapter;
import com.geemvc.converter.ConverterAdapter;
import com.geemvc.converter.ConverterAdapterFactory;
import com.geemvc.converter.ConverterContext;
import com.geemvc.converter.SimpleConverter;
import com.geemvc.converter.bean.BeanConverterAdapter;
import com.geemvc.converter.bean.BeanConverterAdapterFactory;
import com.geemvc.handler.RequestHandler;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.reflect.ReflectionProvider;
import com.geemvc.validation.Errors;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultMethodParams implements MethodParams {
    protected final ParamAdapterFactory paramAdapterFactory;
    protected final ConverterAdapterFactory converterAdapterFactory;
    protected final BeanConverterAdapterFactory beanConverterAdapterFactory;
    protected final ReflectionProvider reflectionProvider;

    @Inject
    protected Injector injector;

    @Logger
    protected Log log;

    @Inject
    public DefaultMethodParams(ParamAdapterFactory paramAdapterFactory, ConverterAdapterFactory converterAdapterFactory, BeanConverterAdapterFactory beanConverterAdapterFactory, ReflectionProvider reflectionProvider) {
        this.paramAdapterFactory = paramAdapterFactory;
        this.converterAdapterFactory = converterAdapterFactory;
        this.beanConverterAdapterFactory = beanConverterAdapterFactory;
        this.reflectionProvider = reflectionProvider;
    }

    @Override
    public List<MethodParam> get(RequestHandler requestHandler, RequestContext requestContext) {
        return requestHandler == null ? null : requestHandler.methodParams();
    }

    @Override
    public Map<String, List<String>> values(List<MethodParam> methodParams, RequestContext requestCtx, Errors errors, Notices notices) {
        Map<String, List<String>> paramValues = new LinkedHashMap<>();

        if (methodParams != null && !methodParams.isEmpty()) {
            for (MethodParam methodParam : methodParams) {
                Annotation paramAnnotation = methodParam.paramAnnotation();

                if (paramAnnotation != null) {
                    ParamAdapter<Annotation> paramAdapter = paramAdapterFactory.create(paramAnnotation.annotationType());

                    ParamContext paramCtx = injector.getInstance(ParamContext.class).build(methodParam, paramValues, null, requestCtx, errors, notices);

                    String name = name(paramAdapter, paramAnnotation, methodParam);

                    paramValues.put(name, paramAdapter.getValue(paramAnnotation, name, paramCtx));
                }
            }
        }

        return paramValues;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map<String, Object> typedValues(Map<String, List<String>> requestValues, List<MethodParam> methodParams, RequestContext requestCtx, Errors errors, Notices notices) {
        SimpleConverter simpleConverter = injector.getInstance(SimpleConverter.class);

        Map<String, Object> typedValues = new LinkedHashMap<>();

        if (methodParams != null && !methodParams.isEmpty()) {
            for (MethodParam methodParam : methodParams) {
                ParamContext paramCtx = injector.getInstance(ParamContext.class).build(methodParam, requestValues, typedValues, requestCtx, errors, notices);

                Annotation paramAnnotation = methodParam.paramAnnotation();

                // Find adapter class for the method parameter annotation.
                ParamAdapter<Annotation> paramAdapter = paramAdapterFactory.create(paramAnnotation.annotationType());

                String name = name(paramAdapter, paramAnnotation, methodParam);

                // If the ParamAdapter is already returning a specific type, we will skip type-conversion and just add
                // the value directly to the typedValues map.
                if (paramAdapter instanceof TypedParamAdapter) {
                    Object typedValue = ((TypedParamAdapter) paramAdapter).getTypedValue(paramAnnotation, name, paramCtx);
                    typedValues.put(name, typedValue);
                    continue;
                }

                List<String> value = requestValues.get(name);
                Class<?> type = methodParam.type();

                Type parameterizedType = methodParam.parameterizedType();
                List<Class<?>> genericType = null;

                if (parameterizedType != null)
                    genericType = reflectionProvider.getGenericType(parameterizedType);

                ConverterContext converterCtx = injector.getInstance(ConverterContext.class).build(name, type, genericType, requestCtx, requestValues, errors, notices);

                // No value in request found to convert.
                if (value == null) {
                    // If parameter is a bean and @Nullable is not set, we create a new empty instance.
                    if (!methodParam.isNullable() && !simpleConverter.canConvert((Class<?>) type)) {
                        BeanConverterAdapter beanConverter = beanConverterAdapterFactory.create(type, methodParam.parameterizedType());

                        if (beanConverter != null) {
                            typedValues.put(name, beanConverter.newInstance(type, converterCtx));
                        } else {
                            log.warn("Unable to find a compatible bean converter for the bean '{}' while attempting to bind values to the method param '{}'. Binding 'null' instead.", () -> type.getName(), () -> methodParam.name());
                            typedValues.put(name, null);
                        }
                    } else {
                        // Otherwise we simply pass a null value to the handler.
                        typedValues.put(name, null);
                    }

                    continue;
                } else if (value.size() == 0) {
                    typedValues.put(name, null);
                    continue;
                }

                ConverterAdapter<?> converterAdapter = converterAdapterFactory.create(type, parameterizedType);

                if (converterAdapter != null && Str.isEmpty(value.get(0))) {
                    typedValues.put(name, null);
                } else if (converterAdapter != null && converterAdapter.canConvert(value, converterCtx)) {
                    Object convertedValue = converterAdapter.fromStrings(value, converterCtx);
                    typedValues.put(name, convertedValue);
                } else {
                    if (simpleConverter.canConvert((Class<?>) type)) {
                        try {
                            Object typedVal = simpleConverter.fromString(value.get(0), (Class<?>) type);
                            typedValues.put(name, typedVal);
                        } catch (Exception e) {
                            log.warn("Unable to convert parameter '{}' due to the following error: '{}'. Binding 'null' instead.", () -> name, () -> e.getMessage());
                            typedValues.put(name, null);
                        }
                    } else {
                        BeanConverterAdapter beanConverter = beanConverterAdapterFactory.create(type, methodParam.parameterizedType());

                        if (beanConverter != null) {
                            Object bean = beanConverter.fromStrings(value, name, type, converterCtx);
                            typedValues.put(name, bean);
                        } else {
                            log.warn("Unable to find a compatible bean converter for the bean '{}' while attempting to bind values to the method param '{}'. Binding 'null' instead.", () -> type.getName(), () -> methodParam.name());
                            typedValues.put(name, null);
                        }
                    }
                }
            }
        }

        return typedValues;
    }

    @Override
    public String name(ParamAdapter<Annotation> paramAdapter, Annotation paramAnnotation, MethodParam methodParam) {
        String annotationName = paramAdapter.getName(paramAnnotation);
        return Str.isEmpty(annotationName) ? methodParam.name() : annotationName;
    }
}
