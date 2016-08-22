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

import com.geemvc.mock.bean.Person;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 16.08.2016.
 */
@Provider
@Singleton
public class MapMessageBodyWriter implements MessageBodyWriter<Map<String, Person>> {

    @Inject
    protected ReflectionProvider reflectionProvider;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!Map.class.isAssignableFrom(type))
            return false;

        List<Class<?>> genericTypeList = reflectionProvider.getGenericType(genericType);

        if (genericTypeList.contains(String.class) && genericTypeList.contains(Person.class)) {
            return true;
        }

        return false;
    }

    @Override
    public long getSize(Map<String, Person> stringPersonMap, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(Map<String, Person> stringPersonMap, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

    }
}
