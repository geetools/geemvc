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

package com.cb.geemvc.helper;

import com.cb.geemvc.HttpMethod;
import com.cb.geemvc.Str;
import com.cb.geemvc.annotation.Request;
import com.cb.geemvc.intercept.OnView;
import com.cb.geemvc.intercept.When;
import com.cb.geemvc.intercept.annotation.*;
import com.cb.geemvc.reflect.ReflectionsWrapper;
import com.cb.geemvc.validation.Validator;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class DefaultAnnotations implements Annotations {
    protected final ReflectionsWrapper reflectionsWrapper;

    protected String[] emptyStringArray = new String[]{Str.EMPTY};
    protected String[] zeroLengthStringArray = new String[]{};
    protected Class[] zeroLengthClassArray = new Class[]{};
    protected String[] defaultHttpMethodsArray = new String[]{HttpMethod.GET};

    protected String valueMethodName = "value";

    @Inject
    public DefaultAnnotations(ReflectionsWrapper reflectionsWrapper) {
        this.reflectionsWrapper = reflectionsWrapper;
    }

    @Override
    public List<Annotation> explodeAnnotations(Annotation[] annotations) {

        List<Annotation> explodedAnnotations = new ArrayList<>();

        for (Annotation annotation : annotations) {
            Method[] annotationMethods = annotation.annotationType().getDeclaredMethods();

            if (annotationMethods.length == 1
                    && valueMethodName.equals(annotationMethods[0].getName())
                    && annotationMethods[0].getReturnType().isArray()
                    && Annotation.class.isAssignableFrom(annotationMethods[0].getReturnType().getComponentType())) {
                try {
                    Annotation[] repeatableAnnotations = (Annotation[]) annotationMethods[0].invoke(annotation, (Object[]) null);

                    for (Annotation repeatableAnno : repeatableAnnotations) {
                        explodedAnnotations.add(repeatableAnno);
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            } else {
                explodedAnnotations.add(annotation);
            }
        }

        return explodedAnnotations;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Annotation> toType(Annotation annotation) {
        if (annotation instanceof Proxy) {
            Class<?>[] interfaces = ((Proxy) annotation).getClass().getInterfaces();

            if (interfaces.length == 1 && Annotation.class.isAssignableFrom(interfaces[0])) {
                return (Class<? extends Annotation>) interfaces[0];
            } else if (interfaces.length > 1) {
                for (Class<?> interf : interfaces) {
                    if (Annotation.class.isAssignableFrom(interf))
                        return (Class<? extends Annotation>) interf;
                }
            }
        } else {
            return annotation.getClass();
        }

        return null;
    }

    @Override
    public <T extends Annotation> Set<T> fromMethods(Class<T> annotation) {
        Set<Method> methods = reflectionsWrapper.getMethodsAnnotatedWith(annotation);

        Set<T> annotations = new LinkedHashSet<>();

        for (Method method : methods) {
            annotations.add(method.getAnnotation(annotation));
        }

        return annotations;
    }

    @Override
    public String[] paths(Request requestMapping) {
        if (requestMapping == null)
            return emptyStringArray;

        String[] paths = requestMapping.path() == null || requestMapping.path().length == 0 ? requestMapping.value() : requestMapping.path();

        return paths == null || paths.length == 0 ? emptyStringArray : paths;
    }

    @Override
    public Request requestMapping(final Class<?> clazz) {
        if (clazz == null)
            return null;

        final Request requestMapping = clazz.getAnnotation(Request.class);

        final Path jsr311Path = clazz.getAnnotation(Path.class);
        final Produces jsr311Produces = clazz.getAnnotation(Produces.class);
        final Consumes jsr311Consumes = clazz.getAnnotation(Consumes.class);

        final GET get = clazz.getAnnotation(GET.class);
        final POST post = clazz.getAnnotation(POST.class);
        final PUT put = clazz.getAnnotation(PUT.class);
        final DELETE delete = clazz.getAnnotation(DELETE.class);
        final OPTIONS options = clazz.getAnnotation(OPTIONS.class);
        final HEAD head = clazz.getAnnotation(HEAD.class);

        final List<String> jsr311HttpMethods = new ArrayList<>();

        if (get != null)
            jsr311HttpMethods.add(GET.class.getSimpleName());

        if (post != null)
            jsr311HttpMethods.add(POST.class.getSimpleName());

        if (put != null)
            jsr311HttpMethods.add(PUT.class.getSimpleName());

        if (delete != null)
            jsr311HttpMethods.add(DELETE.class.getSimpleName());

        if (options != null)
            jsr311HttpMethods.add(OPTIONS.class.getSimpleName());

        if (head != null)
            jsr311HttpMethods.add(HEAD.class.getSimpleName());

        return new Request() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }

            @Override
            public String[] value() {
                String[] reqMappingPaths = paths(requestMapping);
                String[] jsr311Paths = jsr311Path == null ? null : new String[]{jsr311Path.value()};

                if ((reqMappingPaths.length > 1 || !reqMappingPaths[0].isEmpty()) && jsr311Paths != null && !jsr311Paths[0].isEmpty())
                    throw new IllegalStateException("You cannot specify a path in both the @Request annotation and the jsr311 @Path annotation. Please choose one of the two.");

                return jsr311Paths == null ? reqMappingPaths : jsr311Paths;
            }

            @Override
            public String name() {
                return requestMapping == null ? Str.EMPTY : requestMapping.name();
            }

            @Override
            public String[] produces() {
                String[] reqMappingProduces = requestMapping == null ? zeroLengthStringArray : requestMapping.produces();

                if (jsr311Produces != null && reqMappingProduces.length > 0)
                    throw new IllegalStateException("You cannot specify 'produces' in both the @Request annotation and the jsr311 @Produces annotation. Please choose one of the two.");

                return jsr311Produces == null ? reqMappingProduces : jsr311Produces.value();
            }

            @Override
            public int priority() {
                return requestMapping == null ? 111 : requestMapping.priority();
            }

            @Override
            public String[] path() {
                return value();
            }

            @Override
            public String[] params() {
                return requestMapping == null ? zeroLengthStringArray : requestMapping.params();
            }

            @Override
            public String[] method() {
                String[] reqMappingHttpMethods = requestMapping == null ? null : requestMapping.method();

                if (reqMappingHttpMethods != null && reqMappingHttpMethods.length > 0 && jsr311HttpMethods.size() > 0)
                    throw new IllegalStateException("You cannot specify HTTP methods in both the @Request annotation and the jsr311 annotations (@GET, @POST, @PUT etc). Please choose one of the two.");

                return jsr311HttpMethods.size() > 0 ? jsr311HttpMethods.toArray(new String[jsr311HttpMethods.size()]) : reqMappingHttpMethods == null ? defaultHttpMethodsArray : reqMappingHttpMethods;
            }

            @Override
            public String[] ignore() {
                return requestMapping == null ? zeroLengthStringArray : requestMapping.ignore();
            }

            @Override
            public String[] headers() {
                return requestMapping == null ? zeroLengthStringArray : requestMapping.headers();
            }

            @Override
            public String handles() {
                return requestMapping == null ? Str.EMPTY : requestMapping.handles();
            }

            @Override
            public String[] cookies() {
                return requestMapping == null ? zeroLengthStringArray : requestMapping.cookies();
            }

            @Override
            public String[] consumes() {
                String[] reqMappingConsumes = requestMapping == null ? zeroLengthStringArray : requestMapping.consumes();

                if (jsr311Consumes != null && reqMappingConsumes.length > 0)
                    throw new IllegalStateException("You cannot specify 'consumes' in both the @Request annotation and the jsr311 @Consumes annotation. Please choose one of the two.");

                return jsr311Consumes == null ? reqMappingConsumes : jsr311Consumes.value();
            }

            @SuppressWarnings("unchecked")
            @Override
            public Class<? extends Validator>[] validator() {
                return requestMapping == null ? zeroLengthClassArray : requestMapping.validator();
            }

            @Override
            public String onError() {
                return requestMapping == null ? Str.EMPTY : requestMapping.onError();
            }
        };
    }

    @Override
    public Request requestMapping(final Method method) {
        if (method == null)
            return null;

        final Request requestMapping = method.getAnnotation(Request.class);

        final Path jsr311Path = method.getAnnotation(Path.class);
        final Produces jsr311Produces = method.getAnnotation(Produces.class);
        final Consumes jsr311Consumes = method.getAnnotation(Consumes.class);

        final GET get = method.getAnnotation(GET.class);
        final POST post = method.getAnnotation(POST.class);
        final PUT put = method.getAnnotation(PUT.class);
        final DELETE delete = method.getAnnotation(DELETE.class);
        final OPTIONS options = method.getAnnotation(OPTIONS.class);
        final HEAD head = method.getAnnotation(HEAD.class);

        final List<String> jsr311HttpMethods = new ArrayList<>();

        if (get != null)
            jsr311HttpMethods.add(GET.class.getSimpleName());

        if (post != null)
            jsr311HttpMethods.add(POST.class.getSimpleName());

        if (put != null)
            jsr311HttpMethods.add(PUT.class.getSimpleName());

        if (delete != null)
            jsr311HttpMethods.add(DELETE.class.getSimpleName());

        if (options != null)
            jsr311HttpMethods.add(OPTIONS.class.getSimpleName());

        if (head != null)
            jsr311HttpMethods.add(HEAD.class.getSimpleName());

        return new Request() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }

            @Override
            public String[] value() {
                String[] reqMappingPaths = paths(requestMapping);
                String[] jsr311Paths = jsr311Path == null ? null : new String[]{jsr311Path.value()};

                if ((reqMappingPaths.length > 1 || !reqMappingPaths[0].isEmpty()) && jsr311Paths != null && !jsr311Paths[0].isEmpty())
                    throw new IllegalStateException("You cannot specify a path in both the @Request annotation and the jsr311 @Path annotation. Please choose one of the two.");

                return jsr311Paths == null ? reqMappingPaths : jsr311Paths;
            }

            @Override
            public String name() {
                return requestMapping == null ? Str.EMPTY : requestMapping.name();
            }

            @Override
            public String[] produces() {
                String[] reqMappingProduces = requestMapping == null ? zeroLengthStringArray : requestMapping.produces();

                if (jsr311Produces != null && reqMappingProduces.length > 0)
                    throw new IllegalStateException("You cannot specify 'produces' in both the @Request annotation and the jsr311 @Produces annotation. Please choose one of the two.");

                return jsr311Produces == null ? reqMappingProduces : jsr311Produces.value();
            }

            @Override
            public int priority() {
                return requestMapping == null ? 111 : requestMapping.priority();
            }

            @Override
            public String[] path() {
                return value();
            }

            @Override
            public String[] params() {
                return requestMapping == null ? zeroLengthStringArray : requestMapping.params();
            }

            @Override
            public String[] method() {
                String[] reqMappingHttpMethods = requestMapping == null ? null : requestMapping.method();

                if (reqMappingHttpMethods != null && reqMappingHttpMethods.length > 0 && jsr311HttpMethods.size() > 0)
                    throw new IllegalStateException("You cannot specify HTTP methods in both the @Request annotation and the jsr311 annotations (@GET, @POST, @PUT etc). Please choose one of the two.");

                return jsr311HttpMethods.size() > 0 ? jsr311HttpMethods.toArray(new String[jsr311HttpMethods.size()]) : reqMappingHttpMethods == null ? defaultHttpMethodsArray : reqMappingHttpMethods;
            }

            @Override
            public String[] ignore() {
                return requestMapping == null ? zeroLengthStringArray : requestMapping.ignore();
            }

            @Override
            public String[] headers() {
                return requestMapping == null ? zeroLengthStringArray : requestMapping.headers();
            }

            @Override
            public String handles() {
                return requestMapping == null ? Str.EMPTY : requestMapping.handles();
            }

            @Override
            public String[] cookies() {
                return requestMapping == null ? zeroLengthStringArray : requestMapping.cookies();
            }

            @Override
            public String[] consumes() {
                String[] reqMappingConsumes = requestMapping == null ? zeroLengthStringArray : requestMapping.consumes();

                if (jsr311Consumes != null && reqMappingConsumes.length > 0)
                    throw new IllegalStateException("You cannot specify 'consumes' in both the @Request annotation and the jsr311 @Consumes annotation. Please choose one of the two.");

                return jsr311Consumes == null ? reqMappingConsumes : jsr311Consumes.value();
            }

            @SuppressWarnings("unchecked")
            @Override
            public Class<? extends Validator>[] validator() {
                return requestMapping == null ? zeroLengthClassArray : requestMapping.validator();
            }

            @Override
            public String onError() {
                return requestMapping == null ? Str.EMPTY : requestMapping.onError();
            }
        };
    }

    @Override
    public Lifecycle lifecycleAnnotation(PreBinding preBinding) {
        return new Lifecycle() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Lifecycle.class;
            }

            @Override
            public Class<? extends Annotation> annotation() {
                return PreBinding.class;
            }

            @Override
            public String[] on() {
                return preBinding.on();
            }

            @Override
            public Class<?>[] controller() {
                return preBinding.controller();
            }

            @Override
            public String[] method() {
                return preBinding.method();
            }

            @Override
            public String[] name() {
                return preBinding.name();
            }

            @Override
            public When when() {
                return preBinding.when();
            }

            @Override
            public OnView onView() {
                return preBinding.onView();
            }

            @Override
            public int weight() {
                return preBinding.weight();
            }
        };
    }

    @Override
    public Lifecycle lifecycleAnnotation(PostBinding postBinding) {
        return new Lifecycle() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Lifecycle.class;
            }

            @Override
            public Class<? extends Annotation> annotation() {
                return PostBinding.class;
            }

            @Override
            public String[] on() {
                return postBinding.on();
            }

            @Override
            public Class<?>[] controller() {
                return postBinding.controller();
            }

            @Override
            public String[] method() {
                return postBinding.method();
            }

            @Override
            public String[] name() {
                return postBinding.name();
            }

            @Override
            public When when() {
                return postBinding.when();
            }

            @Override
            public OnView onView() {
                return postBinding.onView();
            }

            @Override
            public int weight() {
                return postBinding.weight();
            }
        };
    }

    @Override
    public Lifecycle lifecycleAnnotation(PreValidation preValidation) {
        return new Lifecycle() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Lifecycle.class;
            }

            @Override
            public Class<? extends Annotation> annotation() {
                return PreValidation.class;
            }

            @Override
            public String[] on() {
                return preValidation.on();
            }

            @Override
            public Class<?>[] controller() {
                return preValidation.controller();
            }

            @Override
            public String[] method() {
                return preValidation.method();
            }

            @Override
            public String[] name() {
                return preValidation.name();
            }

            @Override
            public When when() {
                return preValidation.when();
            }

            @Override
            public OnView onView() {
                return preValidation.onView();
            }

            @Override
            public int weight() {
                return preValidation.weight();
            }
        };
    }

    @Override
    public Lifecycle lifecycleAnnotation(PostValidation postValidation) {
        return new Lifecycle() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Lifecycle.class;
            }

            @Override
            public Class<? extends Annotation> annotation() {
                return PostValidation.class;
            }

            @Override
            public String[] on() {
                return postValidation.on();
            }

            @Override
            public Class<?>[] controller() {
                return postValidation.controller();
            }

            @Override
            public String[] method() {
                return postValidation.method();
            }

            @Override
            public String[] name() {
                return postValidation.name();
            }

            @Override
            public When when() {
                return postValidation.when();
            }

            @Override
            public OnView onView() {
                return postValidation.onView();
            }

            @Override
            public int weight() {
                return postValidation.weight();
            }
        };
    }

    @Override
    public Lifecycle lifecycleAnnotation(PreHandle preHandle) {
        return new Lifecycle() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Lifecycle.class;
            }

            @Override
            public Class<? extends Annotation> annotation() {
                return PreHandle.class;
            }

            @Override
            public String[] on() {
                return preHandle.on();
            }

            @Override
            public Class<?>[] controller() {
                return preHandle.controller();
            }

            @Override
            public String[] method() {
                return preHandle.method();
            }

            @Override
            public String[] name() {
                return preHandle.name();
            }

            @Override
            public When when() {
                return preHandle.when();
            }

            @Override
            public OnView onView() {
                return preHandle.onView();
            }

            @Override
            public int weight() {
                return preHandle.weight();
            }
        };
    }

    @Override
    public Lifecycle lifecycleAnnotation(PostHandle postHandle) {
        return new Lifecycle() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Lifecycle.class;
            }

            @Override
            public Class<? extends Annotation> annotation() {
                return PostHandle.class;
            }

            @Override
            public String[] on() {
                return postHandle.on();
            }

            @Override
            public Class<?>[] controller() {
                return postHandle.controller();
            }

            @Override
            public String[] method() {
                return postHandle.method();
            }

            @Override
            public String[] name() {
                return postHandle.name();
            }

            @Override
            public When when() {
                return postHandle.when();
            }

            @Override
            public OnView onView() {
                return postHandle.onView();
            }

            @Override
            public int weight() {
                return postHandle.weight();
            }
        };
    }

    @Override
    public Lifecycle lifecycleAnnotation(PreView preView) {
        return new Lifecycle() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Lifecycle.class;
            }

            @Override
            public Class<? extends Annotation> annotation() {
                return PreView.class;
            }

            @Override
            public String[] on() {
                return preView.on();
            }

            @Override
            public Class<?>[] controller() {
                return preView.controller();
            }

            @Override
            public String[] method() {
                return preView.method();
            }

            @Override
            public String[] name() {
                return preView.name();
            }

            @Override
            public When when() {
                return preView.when();
            }

            @Override
            public OnView onView() {
                return preView.onView();
            }

            @Override
            public int weight() {
                return preView.weight();
            }
        };
    }

    @Override
    public Lifecycle lifecycleAnnotation(PostView postView) {
        return new Lifecycle() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Lifecycle.class;
            }

            @Override
            public Class<? extends Annotation> annotation() {
                return PostView.class;
            }

            @Override
            public String[] on() {
                return postView.on();
            }

            @Override
            public Class<?>[] controller() {
                return postView.controller();
            }

            @Override
            public String[] method() {
                return postView.method();
            }

            @Override
            public String[] name() {
                return postView.name();
            }

            @Override
            public When when() {
                return postView.when();
            }

            @Override
            public OnView onView() {
                return postView.onView();
            }

            @Override
            public int weight() {
                return postView.weight();
            }
        };
    }
}
