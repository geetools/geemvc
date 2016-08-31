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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import com.geemvc.Str;
import com.geemvc.inject.Injectors;
import com.geemvc.script.Evaluator;
import com.geemvc.script.EvaluatorContext;
import com.geemvc.script.EvaluatorFactory;
import com.geemvc.script.RegexEvaluator;
import com.geemvc.script.DefaultSimpleEvaluator;
import com.geemvc.validation.annotation.Check;
import com.google.inject.Inject;
import com.google.inject.Injector;

public abstract class AbstractValidator {
    protected final EvaluatorFactory evaluatorFactory;

    @Inject
    protected Injector injector;

    protected AbstractValidator() {
        this.evaluatorFactory = Injectors.provide().getInstance(EvaluatorFactory.class);
    }

    protected boolean validateRequired(boolean isRequired, Object value) {
        if (!isRequired)
            return true;

        if (value instanceof String)
            return !Str.isEmpty((String) value);
        else
            return value != null;
    }

    protected boolean validateMin(Number min, Object value) {
        if (min == null || min.intValue() == -1)
            return true;

        if (value != null && value instanceof Number) {
            Number n = (Number) value;
            return n.doubleValue() >= min.doubleValue();
        }

        return false;
    }

    protected boolean validateMax(Number max, Object value) {
        if (max == null || max.intValue() == -1)
            return true;

        if (value != null && value instanceof Number) {
            Number n = (Number) value;
            return n.doubleValue() <= max.doubleValue();
        }

        return false;
    }

    @SuppressWarnings("rawtypes")
    protected boolean validateMinLength(Number minLength, Object value) {
        if (minLength == null || minLength.intValue() == -1)
            return true;

        if (value == null) {
            return false;
        }

        int minLen = minLength.intValue();

        if (value instanceof String) {
            return ((String) value).length() >= minLen;
        } else if (value instanceof StringBuilder) {
            return ((StringBuilder) value).length() >= minLen;
        } else if (value instanceof StringBuffer) {
            return ((StringBuffer) value).length() >= minLen;
        } else if (value instanceof Collection) {
            return ((Collection) value).size() >= minLen;
        } else if (value instanceof Map) {
            return ((Map) value).size() >= minLen;
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) >= minLen;
        }

        return false;
    }

    @SuppressWarnings("rawtypes")
    protected boolean validateMaxLength(Number maxLength, Object value) {
        if (maxLength == null || maxLength.intValue() == -1)
            return true;

        if (value == null) {
            return false;
        }

        int maxLen = maxLength.intValue();

        if (value instanceof String) {
            return ((String) value).length() <= maxLen;
        } else if (value instanceof StringBuilder) {
            return ((StringBuilder) value).length() <= maxLen;
        } else if (value instanceof StringBuffer) {
            return ((StringBuffer) value).length() <= maxLen;
        } else if (value instanceof Collection) {
            return ((Collection) value).size() <= maxLen;
        } else if (value instanceof Map) {
            return ((Map) value).size() <= maxLen;
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) <= maxLen;
        }

        return false;
    }

    protected boolean validateIs(Check check, ValidationContext validationCtx) {
        String isExpression = check.is();

        if (Str.isEmpty(isExpression))
            return true;

        Evaluator evaluator = evaluatorFactory.find(isExpression);
        EvaluatorContext evalCtx = null;

        // The simple and regex evaluators expect the request parameter map.
        if (evaluator instanceof DefaultSimpleEvaluator || evaluator instanceof RegexEvaluator) {
            evalCtx = injector.getInstance(EvaluatorContext.class).build(validationCtx.requestCtx().getParameterMap()).append(validationCtx.requestCtx());
        } else {
            // All other will get the converted typed map values.
            evalCtx = injector.getInstance(EvaluatorContext.class).build(validationCtx.typedValues()).append(validationCtx.requestCtx());
        }

        if (evaluator.matches(evalCtx)) {
            return true;
        }

        return false;
    }
}
