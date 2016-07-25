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

package com.geemvc.validation;

import com.geemvc.Str;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.lang.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class DefaultErrors implements Errors {

    protected Map<String, Set<Error>> errors = null;

    protected static final String GLOBAL_ERROR_KEY = "__globalErrors";

    @Inject
    protected Injector injector;

    @Override
    public void add(String field, String message, Object... args) {
        if (errors == null)
            errors = new LinkedHashMap<>();

        Set<Error> fieldErrors = errors.get(field);

        if (fieldErrors == null) {
            fieldErrors = new LinkedHashSet<>();
            errors.put(field, fieldErrors);
        }

        if (message.startsWith(Str.CURLY_BRACKET_OPEN) && message.endsWith(Str.CURLY_BRACKET_CLOSE))
            message = message.substring(1, message.length() - 1);

        fieldErrors.add(injector.getInstance(Error.class).build(field, message, args));
    }

    @Override
    public void add(String message, Object... args) {
        if (errors == null)
            errors = new LinkedHashMap<>();

        Set<Error> fieldErrors = errors.get(GLOBAL_ERROR_KEY);

        if (fieldErrors == null) {
            fieldErrors = new LinkedHashSet<>();
            errors.put(GLOBAL_ERROR_KEY, fieldErrors);
        }

        if (message.startsWith(Str.CURLY_BRACKET_OPEN) && message.endsWith(Str.CURLY_BRACKET_CLOSE))
            message = message.substring(1, message.length() - 1);

        fieldErrors.add(injector.getInstance(Error.class).build(GLOBAL_ERROR_KEY, message, args));
    }

    @Override
    public boolean exist(String field) {
        return errors != null && errors.get(field) != null;
    }

    @Override
    public Error get(String field) {
        if (errors == null)
            return null;

        Set<Error> fieldErrors = errors.get(field);

        if (fieldErrors == null || fieldErrors.isEmpty())
            return null;

        return errors.get(field).iterator().next();
    }

    @Override
    public Set<Error> globalErrors() {
        if (errors == null)
            return null;

        return errors.get(GLOBAL_ERROR_KEY);
    }

    @Override
    public Set<Error> allErrors() {
        if (errors == null)
            return null;

        Set<Error> allErrors = new LinkedHashSet<>();

        for (Map.Entry<String, Set<Error>> errorEntry : errors.entrySet()) {
            allErrors.addAll(errorEntry.getValue());
        }

        return allErrors;
    }

    @Override
    public Set<Error> fieldErrors() {
        if (errors == null)
            return null;

        Set<Error> fieldErrors = new LinkedHashSet<>();

        for (Map.Entry<String, Set<Error>> errorEntry : errors.entrySet()) {
            if (!GLOBAL_ERROR_KEY.equals(errorEntry.getKey())) {
                fieldErrors.addAll(errorEntry.getValue());
            }
        }

        return fieldErrors;
    }

    @Override
    public boolean isEmpty() {
        return errors == null || errors.isEmpty();
    }

    @Override
    public String toString() {
        return "DefaultErrors [errors=" + errors + "]";
    }
}
