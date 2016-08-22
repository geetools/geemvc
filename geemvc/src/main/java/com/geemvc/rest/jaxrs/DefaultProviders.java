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

package com.geemvc.rest.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.reflect.ReflectionProvider;
import com.geemvc.reflect.ReflectionsWrapper;
import com.geemvc.rest.jaxrs.util.ObjectFactory;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Created by Michael on 16.08.2016.
 */
@Singleton
public class DefaultProviders implements Providers {
    protected final Application application;
    protected final ReflectionsWrapper reflectionsWrapper;
    protected final ReflectionProvider reflectionProvider;
    protected final ProviderFilter providerFilter;
    protected final ObjectFactory objectFactory;

    @Inject
    protected Injector injector;

    @Logger
    protected Log log;

    @Inject
    public DefaultProviders(Application application, ReflectionsWrapper reflectionsWrapper, ReflectionProvider reflectionProvider, ProviderFilter providerFilter, ObjectFactory objectFactory) {
        this.application = application;
        this.reflectionsWrapper = reflectionsWrapper;
        this.reflectionProvider = reflectionProvider;
        this.providerFilter = providerFilter;
        this.objectFactory = objectFactory;
    }

    @Override
    public <T> MessageBodyReader<T> getMessageBodyReader(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        List<MessageBodyReader> messageBodyReaders = messageBodyReaders();

        return (MessageBodyReader<T>) providerFilter.mostRelevantProvider(MessageBodyReader.class, type, genericType, mediaType, messageBodyReaders);
    }

    @Override
    public <T> MessageBodyWriter<T> getMessageBodyWriter(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        List<MessageBodyWriter> messageBodyWriters = messageBodyWriters();
        
        return (MessageBodyWriter<T>) providerFilter.mostRelevantProvider(MessageBodyWriter.class, type, genericType, mediaType, messageBodyWriters);
    }

    @Override
    public <T extends Throwable> ExceptionMapper<T> getExceptionMapper(Class<T> type) {
        return null;
    }

    @Override
    public <T> ContextResolver<T> getContextResolver(Class<T> contextType, MediaType mediaType) {
        List<ContextResolver> contextResolvers = contextResolvers();

        return (ContextResolver<T>) providerFilter.mostRelevantContextResolver(contextType, contextType, mediaType, contextResolvers);
    }

    protected List<ContextResolver> contextResolvers() {
        return providers(ContextResolver.class);
    }

    protected List<MessageBodyReader> messageBodyReaders() {
        return providers(MessageBodyReader.class);
    }

    protected List<MessageBodyWriter> messageBodyWriters() {
        return providers(MessageBodyWriter.class);
    }

    protected <T> List<T> providers(Class<T> providerType) {
        List<T> providers = new ArrayList<>();
        Set<Class<?>> providerClasses = reflectionsWrapper.getTypesAnnotatedWith(Provider.class, true);

        for (Class<?> providerClass : providerClasses) {
            if (providerType.isAssignableFrom(providerClass)) {
//                if (isSingleton(providerClass)) {
                    providers.add((T) objectFactory.newInstance(providerClass));
//                } else {
//                    log.warn("Provider classes are expected to be singletons. Please mark the class '{}' with the @Singleton annotation.", () -> providerClass.getName());
//                }
            }
        }

        return providers;
    }

    public boolean isSingleton(Class<?> providerClass) {
        if (providerClass.isAnnotationPresent(Singleton.class) || providerClass.isAnnotationPresent(javax.inject.Singleton.class)) {
            return true;
        }

        Class<?>[] interfaces = providerClass.getInterfaces();

        for (Class<?> interf : interfaces) {
            if (interf.isAnnotationPresent(Singleton.class) || interf.isAnnotationPresent(javax.inject.Singleton.class)) {
                return true;
            }
        }

        return false;
    }
}
