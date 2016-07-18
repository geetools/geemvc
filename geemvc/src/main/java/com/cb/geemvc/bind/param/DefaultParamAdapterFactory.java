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

package com.cb.geemvc.bind.param;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.cb.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultParamAdapterFactory implements ParamAdapterFactory {
    protected final ReflectionProvider reflectionProvider;

    @Inject
    protected Injector inject;

    @Inject
    public DefaultParamAdapterFactory(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamAdapter<T> create(Class<? extends Annotation> paramAnnotationClass) {
        Map<ParamAdapterKey, ParamAdapter<?>> paramAdapters = reflectionProvider.locateParamAdapters();

        ParamAdapter<T> paramAdapter = (ParamAdapter<T>) paramAdapters.get(inject.getInstance(ParamAdapterKey.class).build(paramAnnotationClass));

        if (paramAdapter == null) {
            // try harder ...
        }

        return paramAdapter;
    }

    @Override
    public boolean exists(Class<? extends Annotation> paramAnnotationClass) {
        return create(paramAnnotationClass) != null;
    }
}
