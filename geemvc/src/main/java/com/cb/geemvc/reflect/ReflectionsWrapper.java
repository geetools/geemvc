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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;

public interface ReflectionsWrapper {
    ReflectionsWrapper configure();

    /**
     * gets all sub types in hierarchy of a given type
     * <p/>
     * depends on SubTypesScanner configured, otherwise an empty set is returned
     */
    public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type);

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
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation);

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
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation, boolean honorInherited);

    /**
     * get types annotated with a given annotation, both classes and annotations, including annotation member values matching
     * <p>
     * {@link java.lang.annotation.Inherited} is honored
     * <p/>
     * depends on TypeAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation);

    /**
     * get types annotated with a given annotation, both classes and annotations, including annotation member values matching
     * <p>
     * {@link java.lang.annotation.Inherited} is honored according to given honorInherited
     * <p/>
     * depends on TypeAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation, boolean honorInherited);

    /**
     * get all methods annotated with a given annotation
     * <p/>
     * depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation);

    /**
     * get all methods annotated with a given annotation, including annotation member values matching
     * <p/>
     * depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Method> getMethodsAnnotatedWith(final Annotation annotation);

    /**
     * get methods with parameter types matching given {@code types}
     */
    public Set<Method> getMethodsMatchParams(Class<?>... types);

    /**
     * get methods with return type match given type
     */
    public Set<Method> getMethodsReturn(Class returnType);

    /**
     * get methods with parameter type match first parameter {@code from}, and return type match type {@code to}
     */
    public Set<Method> getConverters(Class<?> from, Class<?> to);

    /**
     * get methods with any parameter annotated with given annotation
     */
    public Set<Method> getMethodsWithAnyParamAnnotated(Class<? extends Annotation> annotation);

    /**
     * get methods with any parameter annotated with given annotation, including annotation member values matching
     */
    public Set<Method> getMethodsWithAnyParamAnnotated(Annotation annotation);

    /**
     * get all constructors annotated with a given annotation
     * <p/>
     * depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Constructor> getConstructorsAnnotatedWith(final Class<? extends Annotation> annotation);

    /**
     * get all constructors annotated with a given annotation, including annotation member values matching
     * <p/>
     * depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Constructor> getConstructorsAnnotatedWith(final Annotation annotation);

    /**
     * get constructors with parameter types matching given {@code types}
     */
    public Set<Constructor> getConstructorsMatchParams(Class<?>... types);

    /**
     * get constructors with any parameter annotated with given annotation
     */
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(Class<? extends Annotation> annotation);

    /**
     * get constructors with any parameter annotated with given annotation, including annotation member values matching
     */
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(Annotation annotation);

    /**
     * get all fields annotated with a given annotation
     * <p/>
     * depends on FieldAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Field> getFieldsAnnotatedWith(final Class<? extends Annotation> annotation);

    /**
     * get all methods annotated with a given annotation, including annotation member values matching
     * <p/>
     * depends on FieldAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Field> getFieldsAnnotatedWith(final Annotation annotation);

    /**
     * get resources relative paths where simple name (key) matches given namePredicate
     * <p>
     * depends on ResourcesScanner configured, otherwise an empty set is returned
     */
    public Set<String> getResources(final Predicate<String> namePredicate);

    /**
     * get resources relative paths where simple name (key) matches given regular expression
     * <p>
     * depends on ResourcesScanner configured, otherwise an empty set is returned
     * <p>
     * <pre>
     * Set&lt;String&gt; xmls = reflections.getResources(&quot;.*\\.xml&quot;);
     * </pre>
     */
    public Set<String> getResources(final Pattern pattern);
}
