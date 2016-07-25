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

import com.geemvc.Char;
import com.geemvc.bind.MethodParam;
import com.geemvc.handler.RequestHandler;
import com.geemvc.helper.Annotations;
import com.geemvc.reflect.ReflectionProvider;
import com.geemvc.validation.annotation.On;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultValidations implements Validations {

    protected final ValidationAdapterFactory validationAdapterFactory;
    protected final ReflectionProvider reflectionProvider;
    protected final Annotations annotations;

    @Inject
    protected Injector injector;

    @Inject
    public DefaultValidations(ValidationAdapterFactory validationAdapterFactory, ReflectionProvider reflectionProvider, Annotations annotations) {
        this.validationAdapterFactory = validationAdapterFactory;
        this.reflectionProvider = reflectionProvider;
        this.annotations = annotations;
    }

    @Override
    public List<Validation> forHandler(RequestHandler requestHandler) {
        Map<ValidationAdapterKey, ValidationAdapter<? extends Annotation>> validationAdapters = reflectionProvider.locateValidationAdapters();

        Class<?> controllerClass = requestHandler.controllerClass();
        Method handlerMethod = requestHandler.handlerMethod();

        List<Validation> allValidations = new ArrayList<>();
        List<Annotation> allAnnotations = new ArrayList<>();

        if (controllerClass.getAnnotations().length > 0)
            allAnnotations.addAll(annotations.explodeAnnotations(controllerClass.getAnnotations()));

        if (handlerMethod.getAnnotations().length > 0)
            allAnnotations.addAll(annotations.explodeAnnotations(handlerMethod.getAnnotations()));

        List<Validation> validations = toValidations(allAnnotations.toArray(new Annotation[allAnnotations.size()]), null, null);

        return allValidations;
    }

    @Override
    public Set<Validator> forBean(Class<?> type) {
        return reflectionProvider.locateBeanValidators(type);
    }


    @Override
    public List<Validation> forMethodParams(List<MethodParam> methodParams) {
        List<Validation> allValidations = new ArrayList<>();

        for (MethodParam methodParam : methodParams) {
            Annotation[] methodParamAnnotations = methodParam.annotations();

            if (methodParamAnnotations == null || methodParamAnnotations.length == 0)
                continue;

            On onAnnotation = onAnnotation(methodParam.annotations());
            String[] on = onAnnotation == null ? null : onAnnotation.value();

            List<Validation> validations = toValidations(methodParamAnnotations, methodParam.name(), on);
            allValidations.addAll(validations);
        }

        return allValidations;
    }

    @Override
    public List<Validation> forBeanFields(Class<?> type, String name) {
        List<Validation> allValidations = new ArrayList<>();
        Set<Field> fields = reflectionProvider.getMutableFields(type);

        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();

            if (fieldAnnotations.length == 0)
                continue;

            String propertyName = new StringBuilder(name).append(Char.DOT).append(field.getName()).toString();
            On onAnnotation = field.getAnnotation(On.class);
            String[] on = onAnnotation == null ? null : onAnnotation.value();

            List<Validation> validations = toValidations(fieldAnnotations, propertyName, on);
            allValidations.addAll(validations);
        }

        return allValidations;
    }

    @Override
    public List<Validation> toValidations(Annotation[] annotationArr, String propertyName, String[] on) {
        if (annotationArr == null || annotationArr.length == 0)
            return null;

        Map<ValidationAdapterKey, ValidationAdapter<? extends Annotation>> validationAdapters = reflectionProvider.locateValidationAdapters();

        List<Validation> validations = new ArrayList<>();

        List<Annotation> validationAnnotations = validationAnnotations(annotationArr);

        if (validationAnnotations != null && !validationAnnotations.isEmpty()) {
            for (Annotation validationAnnotation : validationAnnotations) {
                ValidationAdapter<? extends Annotation> adapter = validationAdapters.get(injector.getInstance(ValidationAdapterKey.class).build(annotations.toType(validationAnnotation)));
                validations.add(injector.getInstance(Validation.class).build(propertyName, validationAnnotation, adapter, on));
            }
        }

        return validations;
    }

    @Override
    public On onAnnotation(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0)
            return null;

        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == On.class)
                return (On) annotation;
        }

        return null;
    }

    @Override
    public List<Annotation> validationAnnotations(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0)
            return null;

        List<Annotation> explodedAnnotations = this.annotations.explodeAnnotations(annotations);
        List<Annotation> filteredAnnotations = new ArrayList<>();

        for (Annotation annotation : explodedAnnotations) {
            if (validationAdapterFactory.exists(annotation.annotationType())) {
                filteredAnnotations.add(annotation);
            }
        }

        return filteredAnnotations;
    }
}
