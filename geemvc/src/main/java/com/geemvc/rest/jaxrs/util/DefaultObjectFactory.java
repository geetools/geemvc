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

package com.geemvc.rest.jaxrs.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Providers;

import com.geemvc.rest.jaxrs.ProviderFilter;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultObjectFactory implements ObjectFactory {
    protected Providers providers;
    protected ProviderFilter providerFilter;

    @Inject
    protected Injector injector;

    @Inject
    public DefaultObjectFactory(ProviderFilter providerFilter) {
        this.providerFilter = providerFilter;
    }

    @Override
    public <T> T newInstance(Class<T> type) {
        Constructor<?> constructor = providerFilter.mostRelevantConstructor(type);

        if (constructor == null || constructor.getParameterCount() == 0) {
            try {
                return injectMembers((T) type.newInstance());
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }

        Parameter[] parameters = constructor.getParameters();
        List<Object> paramObjects = new ArrayList<>();

        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(Context.class)) {
                paramObjects.add(injector.getInstance(parameter.getType()));
            } else {
                paramObjects.add(null);
            }
        }

        try {
            return injectMembers((T) constructor.newInstance(paramObjects.toArray()));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T injectMembers(T instance) {
        injector.injectMembers(instance);
        return instance;
    }
}
