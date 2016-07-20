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

package com.cb.geemvc.reflect;

import com.cb.geemvc.Str;
import com.cb.geemvc.ThreadStash;
import com.cb.geemvc.cache.Cache;
import com.cb.geemvc.config.Configuration;
import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.reflections.Reflections;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.regex.Pattern;

@Singleton
public class DefaultReflectionsWrapper implements ReflectionsWrapper {

    protected Reflections reflections;

    @Inject
    protected Cache cache;

    protected static final String REFLECTIONS_CACHE_KEY = "geemvc/reflections";

    @Override
    public ReflectionsWrapper configure() {
        reflections = (Reflections) cache.get(DefaultReflectionsWrapper.class, REFLECTIONS_CACHE_KEY, () -> reflections());

        if (reflections == null)
            throw new IllegalStateException("Unable to initialize reflections. Make sure that you have provided the correct lib, classes and classLoaders.");

        return this;
    }

    protected Reflections reflections() {
        ServletConfig servletConfig = (ServletConfig) ThreadStash.get(ServletConfig.class);
        ServletContext servletContext = servletConfig.getServletContext();

        // First we check to see if an reflections provider "instance" has already been added to the servlet-context.
        ReflectionsProvider reflectionsProvider = (ReflectionsProvider) servletContext.getAttribute(Configuration.REFLECTIONS_PROVIDER_KEY);

        // If not then we check to see if a custom provider class has been configured.
        if (reflectionsProvider == null) {
            String configuredReflectionsProvider = servletConfig.getInitParameter(Configuration.REFLECTIONS_PROVIDER_KEY);

            try {
                if (!Str.isEmpty(configuredReflectionsProvider)) {
                    reflectionsProvider = (ReflectionsProvider) Class.forName(configuredReflectionsProvider).newInstance();
                } else {
                    // If still no provider has been resolved, simply use the default.
                    reflectionsProvider = new DefaultReflectionsProvider();
                }
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }

        // Add reflections-provider to the threadLocal variable.
        ReflectionsStash.set(reflectionsProvider);

        return reflectionsProvider.provide();
    }

    /**
     * gets all sub types in hierarchy of a given type
     * <p/>
     * depends on SubTypesScanner configured, otherwise an empty set is returned
     */
    public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        return reflections.getSubTypesOf(type);
    }

    /**
     * get types annotated with a given annotation, both classes and annotations
     * <p>
     * {@link java.lang.annotation.Inherited} is honored.
     * <p>
     * when honoring @Inherited, meta-annotation should only effect annotated super classes and its sub types
     * <p>
     * <i>Note that this (@Inherited) meta-annotation type has no effect if the annotated type is used for anything other than a class. Also, this
     * meta-annotation causes annotations to be inherited only from superclasses; annotations on implemented interfaces have no effect.</i>
     * <p/>
     * depends on TypeAnnotationsScanner and SubTypesScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    /**
     * get types annotated with a given annotation, both classes and annotations
     * <p>
     * {@link java.lang.annotation.Inherited} is honored according to given honorInherited.
     * <p>
     * when honoring @Inherited, meta-annotation should only effect annotated super classes and it's sub types
     * <p>
     * when not honoring @Inherited, meta annotation effects all subtypes, including annotations interfaces and classes
     * <p>
     * <i>Note that this (@Inherited) meta-annotation type has no effect if the annotated type is used for anything other than a class. Also, this
     * meta-annotation causes annotations to be inherited only from superclasses; annotations on implemented interfaces have no effect.</i>
     * <p/>
     * depends on TypeAnnotationsScanner and SubTypesScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation, boolean honorInherited) {
        return reflections.getTypesAnnotatedWith(annotation, honorInherited);
    }

    /**
     * get types annotated with a given annotation, both classes and annotations, including annotation member values matching
     * <p>
     * {@link java.lang.annotation.Inherited} is honored
     * <p/>
     * depends on TypeAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    /**
     * get types annotated with a given annotation, both classes and annotations, including annotation member values matching
     * <p>
     * {@link java.lang.annotation.Inherited} is honored according to given honorInherited
     * <p/>
     * depends on TypeAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation, boolean honorInherited) {
        return reflections.getTypesAnnotatedWith(annotation, honorInherited);
    }

    /**
     * get all methods annotated with a given annotation
     * <p/>
     * depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getMethodsAnnotatedWith(annotation);
    }

    /**
     * get all methods annotated with a given annotation, including annotation member values matching
     * <p/>
     * depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Method> getMethodsAnnotatedWith(final Annotation annotation) {
        return reflections.getMethodsAnnotatedWith(annotation);
    }

    /**
     * get methods with parameter types matching given {@code types}
     */
    public Set<Method> getMethodsMatchParams(Class<?>... types) {
        return reflections.getMethodsMatchParams(types);
    }

    /**
     * get methods with return type match given type
     */
    public Set<Method> getMethodsReturn(Class returnType) {
        return reflections.getMethodsReturn(returnType);
    }

    /**
     * get methods with parameter type match first parameter {@code from}, and return type match type {@code to}
     */
    public Set<Method> getConverters(Class<?> from, Class<?> to) {
        return reflections.getConverters(from, to);
    }

    /**
     * get methods with any parameter annotated with given annotation
     */
    public Set<Method> getMethodsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
        return reflections.getMethodsWithAnyParamAnnotated(annotation);
    }

    /**
     * get methods with any parameter annotated with given annotation, including annotation member values matching
     */
    public Set<Method> getMethodsWithAnyParamAnnotated(Annotation annotation) {
        return reflections.getMethodsWithAnyParamAnnotated(annotation);
    }

    /**
     * get all constructors annotated with a given annotation
     * <p/>
     * depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Constructor> getConstructorsAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getConstructorsAnnotatedWith(annotation);
    }

    /**
     * get all constructors annotated with a given annotation, including annotation member values matching
     * <p/>
     * depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Constructor> getConstructorsAnnotatedWith(final Annotation annotation) {
        return reflections.getConstructorsAnnotatedWith(annotation);
    }

    /**
     * get constructors with parameter types matching given {@code types}
     */
    public Set<Constructor> getConstructorsMatchParams(Class<?>... types) {
        return reflections.getConstructorsMatchParams(types);
    }

    /**
     * get constructors with any parameter annotated with given annotation
     */
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
        return reflections.getConstructorsWithAnyParamAnnotated(annotation);
    }

    /**
     * get constructors with any parameter annotated with given annotation, including annotation member values matching
     */
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(Annotation annotation) {
        return reflections.getConstructorsWithAnyParamAnnotated(annotation);
    }

    /**
     * get all fields annotated with a given annotation
     * <p/>
     * depends on FieldAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Field> getFieldsAnnotatedWith(final Class<? extends Annotation> annotation) {
        return reflections.getFieldsAnnotatedWith(annotation);
    }

    /**
     * get all methods annotated with a given annotation, including annotation member values matching
     * <p/>
     * depends on FieldAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Field> getFieldsAnnotatedWith(final Annotation annotation) {
        return reflections.getFieldsAnnotatedWith(annotation);
    }

    /**
     * get resources relative paths where simple name (key) matches given namePredicate
     * <p>
     * depends on ResourcesScanner configured, otherwise an empty set is returned
     */
    public Set<String> getResources(final Predicate<String> namePredicate) {
        return reflections.getResources(namePredicate);
    }

    /**
     * get resources relative paths where simple name (key) matches given regular expression
     * <p>
     * depends on ResourcesScanner configured, otherwise an empty set is returned
     * <p>
     * <pre>
     * Set&lt;String&gt; xmls = reflections.getResources(&quot;.*\\.xml&quot;);
     * </pre>
     */
    public Set<String> getResources(final Pattern pattern) {
        return reflections.getResources(pattern);
    }
}
