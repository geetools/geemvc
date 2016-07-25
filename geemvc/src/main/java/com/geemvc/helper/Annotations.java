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

package com.geemvc.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import com.geemvc.annotation.Request;
import com.geemvc.intercept.annotation.*;

public interface Annotations {
    List<Annotation> explodeAnnotations(Annotation[] annotations);

    <T extends Annotation> Set<T> fromMethods(Class<T> annotation);

    Class<? extends Annotation> toType(Annotation annotation);

    String[] paths(Request requestMapping);

    Request requestMapping(final Class<?> clazz);

    Request requestMapping(final Method method);

    Lifecycle lifecycleAnnotation(PreBinding preBinding);

    Lifecycle lifecycleAnnotation(PostBinding postBinding);

    Lifecycle lifecycleAnnotation(PreValidation preValidation);

    Lifecycle lifecycleAnnotation(PostValidation postValidation);

    Lifecycle lifecycleAnnotation(PreHandle preHandle);

    Lifecycle lifecycleAnnotation(PostHandle postHandle);

    Lifecycle lifecycleAnnotation(PreView preView);

    Lifecycle lifecycleAnnotation(PostView postView);
}
