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

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import com.geemvc.bind.MethodParam;
import com.geemvc.handler.RequestHandler;
import com.geemvc.validation.annotation.On;

public interface Validations {
    List<Validation> forHandler(RequestHandler requestHandler);

    Set<Validator> forBean(Class<?> type);

    List<Validation> forMethodParams(List<MethodParam> methodParams);

    List<Validation> forBeanFields(Class<?> type, String name);

    List<Validation> toValidations(Annotation[] annotations, String propertyName, String[] on);

    On onAnnotation(Annotation[] annotations);

    List<Annotation> validationAnnotations(Annotation[] annotations);
}
