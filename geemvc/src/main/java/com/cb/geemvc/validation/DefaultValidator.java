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

import com.cb.geemvc.Bindings;
import com.cb.geemvc.bind.MethodParam;
import com.cb.geemvc.handler.RequestHandler;
import com.cb.geemvc.helper.Paths;
import com.cb.geemvc.reflect.ReflectionProvider;
import com.cb.geemvc.validation.annotation.CheckBean;
import com.cb.geemvc.validation.annotation.On;
import com.google.inject.Inject;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.util.*;

public class DefaultValidator extends AbstractValidator implements Validator {

    protected final Validations validations;
    protected final ReflectionProvider reflectionProvider;
    protected final Paths paths;

    @Inject
    public DefaultValidator(Validations validations, ReflectionProvider reflectionProvider, Paths paths) {
        super();
        this.validations = validations;
        this.reflectionProvider = reflectionProvider;
        this.paths = paths;
    }

    public Object validate(RequestHandler requestHandler, ValidationContext validationCtx, Errors e) {

        // Validate request using validation annotations located directly in request-handler.
        validateHandler(requestHandler, validationCtx, e);

        // Validate request using validation annotations located directly at request-handler parameters.
        validateHandlerParameters(requestHandler, validationCtx, e);

        // Validate request using bean property validations of handler parameter.
        validatePropertiesOfBeanParam(requestHandler, validationCtx, e);

        // Validate request using bean property validations of handler parameter.
        return validateBeanParamUsingCustomValidator(requestHandler, validationCtx, e);
    }

    protected Object validateBeanParamUsingCustomValidator(RequestHandler requestHandler, ValidationContext validationCtx, Errors e) {
        Object returnValue = null;

        List<MethodParam> methodParams = requestHandler.methodParams();
        Map<String, Object> typedValues = validationCtx.typedValues();

        // Attempt to find validations for all method handler parameters.
        for (MethodParam methodParam : methodParams) {
            Annotation[] methodParamAnnotations = methodParam.annotations();

            // If param has no annotations no need to continue as no validations have obviously been defined.
            if (methodParamAnnotations == null || methodParamAnnotations.length == 0)
                continue;

            boolean annotationValidExists = annotationValidExists(methodParamAnnotations);
            Object value = typedValues.get(methodParam.name());

            if (includeInBeanValidation(methodParam.getClass())) {

                On onAnnotation = validations.onAnnotation(methodParamAnnotations);
                String[] on = onAnnotation == null ? null : onAnnotation.value();

                if (on != null && !paths.isValidForRequest(on, requestHandler, validationCtx.requestCtx())) {
                    continue;
                }

                Set<Validator> beanValidators = validations.forBean(methodParam.getClass());

                if (beanValidators != null && !beanValidators.isEmpty()) {
                    for (Validator validator : beanValidators) {
                        CheckBean checkBean = validator.getClass().getAnnotation(CheckBean.class);

                        // if @Valid annotation exists in handler method for the current parameter and the validator has not been narrowed down
                        // to a specific path, validate bean.
                        if (annotationValidExists && checkBean.on() == null || checkBean.on().length == 0) {
                            returnValue = validator.validate(requestHandler, validationCtx, e);
                        } else if (paths.isValidForRequest(checkBean.on(), requestHandler, validationCtx.requestCtx())) {
                            returnValue = validator.validate(requestHandler, validationCtx, e);
                        }
                    }
                }
            }
        }

        return returnValue;
    }

    protected void validatePropertiesOfBeanParam(RequestHandler requestHandler, ValidationContext validationCtx, Errors e) {
        List<MethodParam> methodParams = requestHandler.methodParams();
        Map<String, Object> typedValues = validationCtx.typedValues();

        for (MethodParam methodParam : methodParams) {
            Annotation[] methodParamAnnotations = methodParam.annotations();
            boolean annotationValidExists = annotationValidExists(methodParamAnnotations);
            Object value = typedValues.get(methodParam.name());

            if (annotationValidExists && value != null && includeInBeanValidation(value.getClass())) {
                List<Validation> beanFieldValidations = validations.forBeanFields(value.getClass(), methodParam.name());
                validate(beanFieldValidations, requestHandler, validationCtx, e);
            }
        }
    }

    protected void validateHandlerParameters(RequestHandler requestHandler, ValidationContext validationCtx, Errors e) {
        List<MethodParam> methodParams = requestHandler.methodParams();
        Map<String, Object> typedValues = validationCtx.typedValues();

        List<Validation> methodParamValidations = validations.forMethodParams(methodParams);
        validate(methodParamValidations, requestHandler, validationCtx, e);
    }

    protected void validateHandler(RequestHandler requestHandler, ValidationContext validationCtx, Errors e) {
        List<Validation> handlerValidations = validations.forHandler(requestHandler);
        validate(handlerValidations, requestHandler, validationCtx, e);
    }

    protected void validate(List<Validation> validationList, RequestHandler requestHandler, ValidationContext validationCtx, Errors e) {
        if (validationList != null && validationList.size() > 0) {
            for (Validation validation : validationList) {
                ValidationAdapter<? extends Annotation> validationAdapter = validation.adapter();

                if (validation.on() != null && !paths.isValidForRequest(validation.on(), requestHandler, validationCtx.requestCtx())) {
                    continue;
                }

                if (validationAdapter.incudeInValidation(validation.annotation(), requestHandler, validationCtx)) {
                    validationAdapter.validate(validation.annotation(), validation.name(), validationCtx, e);
                }
            }
        }
    }

    protected boolean annotationValidExists(Annotation[] methodParamAnnotations) {
        if (methodParamAnnotations == null || methodParamAnnotations.length == 0)
            return false;

        for (Annotation annotation : methodParamAnnotations) {
            if (annotation.annotationType() == Valid.class) {
                return true;
            }
        }

        return false;
    }

    protected boolean includeInBeanValidation(Class<?> type) {
        if (reflectionProvider.isSimpleType(type)
                || ServletRequest.class.isAssignableFrom(type)
                || ServletResponse.class.isAssignableFrom(type)
                || ServletContext.class.isAssignableFrom(type)
                || HttpSession.class.isAssignableFrom(type)
                || Cookie[].class.isAssignableFrom(type)
                || Locale.class.isAssignableFrom(type)
                || Errors.class.isAssignableFrom(type)
                || Bindings.class.isAssignableFrom(type)
                || Map.class.isAssignableFrom(type)
                || Collection.class.isAssignableFrom(type)) {
            return false;
        }

        return true;
    }
}