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

package com.geemvc.logging;

import com.google.inject.MembersInjector;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Michael on 15.07.2016.
 */
public class LoggerMembersInjector<T> implements MembersInjector<T> {
    protected final Field field;
    protected static final Map<String, Log> logCache = new ConcurrentHashMap();

    public LoggerMembersInjector(Field field) {
        this.field = field;
        field.setAccessible(true);

        Log log = logCache.get(field.getDeclaringClass().getName());

        if (log == null) {
            logCache.putIfAbsent(field.getDeclaringClass().getName(), new DefaultLog().get(field.getDeclaringClass()));
        }
    }

    @Override
    public void injectMembers(T t) {
        try {
            Log log = logCache.get(field.getDeclaringClass().getName());
            field.set(t, log);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
