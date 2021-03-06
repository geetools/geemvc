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

package com.geemvc.reflect;

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

import com.geemvc.annotation.Request;
import com.geemvc.bind.param.ParamAdapter;
import com.geemvc.bind.param.ParamAdapterKey;
import com.geemvc.converter.ConverterAdapter;
import com.geemvc.converter.ConverterAdapterKey;
import com.geemvc.converter.bean.BeanConverterAdapter;
import com.geemvc.data.DataAdapter;
import com.geemvc.handler.ControllerResolver;
import com.geemvc.handler.HandlerResolver;
import com.geemvc.handler.RequestMappingKey;
import com.geemvc.i18n.message.MessageResolver;
import com.geemvc.intercept.AroundHandler;
import com.geemvc.intercept.LifecycleInterceptor;
import com.geemvc.reader.ReaderAdapterKey;
import com.geemvc.reader.bean.BeanReaderAdapter;
import com.geemvc.validation.ValidationAdapter;
import com.geemvc.validation.ValidationAdapterKey;
import com.geemvc.validation.Validator;
import com.geemvc.view.ViewAdapter;
import com.geemvc.view.binding.Bindable;

public interface ReflectionProvider {
    Set<Class<?>> locateControllers();

    Set<ControllerResolver> locateControllerResolvers();

    Set<HandlerResolver> locateHandlerResolvers();

    Set<MessageResolver> locateMessageResolvers();

    Set<AroundHandler> locateAroundHandlerInterceptors();

    Set<LifecycleInterceptor> locateLifecycleInterceptors(Class<? extends Annotation> lifecycleAnnotation, Class<?> controllerClass);

    Set<LifecycleInterceptor> locateLifecycleInterceptors(Class<? extends Annotation> lifecycleAnnotation);

    Set<Bindable> locateBindings();

    Set<Class<?>> locateEvaluators();

    Request getRequestAnnotation(Object o);

    Set<Method> locateHandlerMethods(Predicate<Method> predicate);

    Map<RequestMappingKey, Method> getRequestHandlerMethods(Class<?> controllerClass);

    Map<RequestMappingKey, Method> getRequestHandlerMethods(Class<?> controllerClass, Predicate<Entry<RequestMappingKey, Method>> predicate);

    Map<ConverterAdapterKey, ConverterAdapter<?>> locateConverterAdapters();

    Map<ConverterAdapterKey, BeanConverterAdapter<?>> locateBeanConverterAdapters();

    Map<ReaderAdapterKey, BeanReaderAdapter<?>> locateBeanReaderAdapters();

    Map<ParamAdapterKey, ParamAdapter<?>> locateParamAdapters();

    Map<ValidationAdapterKey, ValidationAdapter<? extends Annotation>> locateValidationAdapters();

    Set<Validator> locateBeanValidators(Class<?> forType);

    Map<String, ViewAdapter> locateViewAdapters();

    Map<String, DataAdapter> locateDataAdapters();

    List<Class<?>> getGenericType(Type genericType);

    List<Class<?>> getGenericType(Class<?> type, String propertyName);

    String toString(Class<?> clazz, Type parameterizedType);

    Set<Field> getMutableFields(Class<?> clazz);

    Set<Field> getFieldsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotationClass);

    Field getField(Class<?> clazz, String name);

    PropertyDescriptor getPropertyDescriptor(Class<?> beanType, String name);

    boolean isSimpleType(Class<?> type);

    Class<?> getPrimaryType(Class<?> type, Type parameterizedType);
}
