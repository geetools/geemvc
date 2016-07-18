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

package com.cb.geemvc.data.adapter;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;

import com.cb.geemvc.annotation.Adapter;
import com.cb.geemvc.data.DataAdapter;
import com.cb.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
@Adapter
public class JpaDataAdapter implements DataAdapter {
    @Inject
    protected Injector injector;

    protected ReflectionProvider reflectionProvider;

    protected static final String NAME = "JpaDataAdapter";

    @Inject
    protected JpaDataAdapter(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean canHandle(Class<?> beanClass) {
        return beanClass != null && beanClass.getAnnotation(Entity.class) != null;
    }

    @Override
    public Map<String, Class<?>> beanIdentifiers(Class<?> beanClass) {
        Set<Field> idFields = reflectionProvider.getFieldsAnnotatedWith(beanClass, Id.class);

        Map<String, Class<?>> identifiers = new LinkedHashMap<>();

        for (Field field : idFields) {
            identifiers.put(field.getName(), field.getType());
        }

        return identifiers;
    }

    @Override
    public Object fetch(Class<?> beanClass, Object id) {
        return injector.getInstance(EntityManager.class).find(beanClass, id);
    }
}
