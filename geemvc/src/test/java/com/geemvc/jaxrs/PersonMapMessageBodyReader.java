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

package com.geemvc.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.geemvc.mock.bean.Person;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;

/**
 * Created by Michael on 16.08.2016.
 */
@Provider
@Singleton
@Consumes(MediaType.APPLICATION_XML)
public class PersonMapMessageBodyReader implements MessageBodyReader<Map<String, Person>> {

    @Inject
    protected ReflectionProvider reflectionProvider;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!Map.class.isAssignableFrom(type))
            return false;

        List<Class<?>> genericTypeList = reflectionProvider.getGenericType(genericType);

        if (genericTypeList.contains(String.class) && genericTypeList.contains(Person.class)) {
            return true;
        }

        return false;
    }

    @Override
    public Map<String, Person> readFrom(Class<Map<String, Person>> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        return null;
    }
}
