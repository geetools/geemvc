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

package com.cb.geemvc.validation;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.cb.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultValidationAdapterFactory implements ValidationAdapterFactory {
    protected final ReflectionProvider reflectionProvider;

    @Inject
    protected Injector inject;

    @Inject
    public DefaultValidationAdapterFactory(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Annotation> ValidationAdapter<T> create(Class<? extends Annotation> validationAnnotationClass) {
        Map<ValidationAdapterKey, ValidationAdapter<?>> validationAdapters = reflectionProvider.locateValidationAdapters();

        ValidationAdapter<T> validationAdapter = (ValidationAdapter<T>) validationAdapters.get(inject.getInstance(ValidationAdapterKey.class).build(validationAnnotationClass));

        if (validationAdapter == null) {
            // try harder ...
        }

        return validationAdapter;
    }

    @Override
    public boolean exists(Class<? extends Annotation> validationAnnotationClass) {
        return create(validationAnnotationClass) != null;
    }
}
