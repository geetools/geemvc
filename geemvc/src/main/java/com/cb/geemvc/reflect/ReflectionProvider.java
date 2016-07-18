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

package com.cb.geemvc.reflect;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import com.cb.geemvc.annotation.Request;
import com.cb.geemvc.bind.param.ParamAdapter;
import com.cb.geemvc.bind.param.ParamAdapterKey;
import com.cb.geemvc.converter.ConverterAdapter;
import com.cb.geemvc.converter.ConverterAdapterKey;
import com.cb.geemvc.data.DataAdapter;
import com.cb.geemvc.handler.ControllerResolver;
import com.cb.geemvc.handler.HandlerResolver;
import com.cb.geemvc.handler.RequestMappingKey;
import com.cb.geemvc.i18n.message.MessageResolver;
import com.cb.geemvc.intercept.AroundHandler;
import com.cb.geemvc.intercept.LifecycleInterceptor;
import com.cb.geemvc.validation.ValidationAdapter;
import com.cb.geemvc.validation.ValidationAdapterKey;
import com.cb.geemvc.validation.Validator;
import com.cb.geemvc.view.ViewAdapter;

public interface ReflectionProvider {
    Set<Class<?>> locateControllers();

    Set<ControllerResolver> locateControllerResolvers();

    Set<HandlerResolver> locateHandlerResolvers();

    Set<MessageResolver> locateMessageResolvers();

    Set<AroundHandler> locateAroundHandlerInterceptors();

    Set<LifecycleInterceptor> locateLifecycleInterceptors(Class<? extends Annotation> lifecycleAnnotation, Class<?> controllerClass);

    Set<LifecycleInterceptor> locateLifecycleInterceptors(Class<? extends Annotation> lifecycleAnnotation);

    Set<Class<?>> locateEvaluators();

    Request getRequestAnnotation(Object o);

    Set<Method> locateHandlerMethods(Predicate<Method> predicate);

    Map<RequestMappingKey, Method> getRequestHandlerMethods(Class<?> controllerClass);

    Map<RequestMappingKey, Method> getRequestHandlerMethods(Class<?> controllerClass, Predicate<Entry<RequestMappingKey, Method>> predicate);

    Map<ConverterAdapterKey, ConverterAdapter<?>> locateConverterAdapters();

    Map<ParamAdapterKey, ParamAdapter<?>> locateParamAdapters();

    Map<ValidationAdapterKey, ValidationAdapter<? extends Annotation>> locateValidationAdapters();

    Set<Validator> locateBeanValidators(Class<?> forType);

    Map<String, ViewAdapter> locateViewAdapters();

    Map<String, DataAdapter> locateDataAdapters();

    List<Class<?>> getGenericType(Type[] inferfaces);

    List<Class<?>> getGenericType(Type genericType);

    List<Class<?>> getGenericType(Class<?> type, String propertyName);

    Set<Field> getMutableFields(Class<?> clazz);

    Set<Field> getFieldsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotationClass);

    Field getField(Class<?> clazz, String name);

    PropertyDescriptor getPropertyDescriptor(Class<?> beanType, String name);

    boolean isSimpleType(Class<?> type);

    Class<?> getPrimaryType(Class<?> type, Type parameterizedType);
}
