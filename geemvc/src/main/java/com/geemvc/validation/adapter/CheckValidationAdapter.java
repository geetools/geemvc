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

package com.geemvc.validation.adapter;

import com.geemvc.Str;
import com.geemvc.annotation.Adapter;
import com.geemvc.handler.RequestHandler;
import com.geemvc.helper.Paths;
import com.geemvc.script.Evaluator;
import com.geemvc.script.EvaluatorContext;
import com.geemvc.script.RegexEvaluator;
import com.geemvc.script.DefaultSimpleEvaluator;
import com.geemvc.validation.AbstractValidator;
import com.geemvc.validation.Errors;
import com.geemvc.validation.ValidationAdapter;
import com.geemvc.validation.ValidationContext;
import com.geemvc.validation.annotation.Check;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Adapter
@Singleton
public class CheckValidationAdapter extends AbstractValidator implements ValidationAdapter<Check> {

    protected final Paths paths;

    @Inject
    public CheckValidationAdapter(Paths paths) {
        this.paths = paths;
    }

    @Override
    public boolean incudeInValidation(Check checkAnnotation, RequestHandler requestHandler, ValidationContext validationCtx) {
        return paths.isValidForRequest(checkAnnotation.on(), requestHandler, validationCtx.requestCtx()) && isWhenValidForRequest(checkAnnotation, validationCtx);
    }

    protected boolean isWhenValidForRequest(Check check, ValidationContext validationCtx) {
        String[] whenExpressions = check.when();

        if (whenExpressions == null || whenExpressions.length == 0)
            return true;

        for (String whenExpression : whenExpressions) {
            if (Str.isEmpty(whenExpression))
                continue;

            Evaluator evaluator = evaluatorFactory.find(whenExpression);
            EvaluatorContext evalCtx = null;

            // The simple and regex evaluators expect the request parameter map.
            if (evaluator instanceof DefaultSimpleEvaluator || evaluator instanceof RegexEvaluator) {
                evalCtx = injector.getInstance(EvaluatorContext.class).build(validationCtx.requestCtx().getParameterMap()).append(validationCtx.requestCtx());
            } else {
                // All other will get the converted typed map values.
                evalCtx = injector.getInstance(EvaluatorContext.class).build(validationCtx.typedValues()).append(validationCtx.requestCtx());
            }

            if (evaluator.matches(evalCtx)) {
                System.out.println("--- WHEN expression '" + whenExpression + "' matches request parameters!");
                return true;
            } else {
                System.out.println("--- WHEN expression '" + whenExpression + "' does NOT match request parameters!");
            }
        }

        return false;
    }

    @Override
    public void validate(Check checkAnnotation, String name, ValidationContext validationCtx, Errors errors) {
        String[] parameters = checkAnnotation.param();

        if (parameters.length == 0 && !Str.isEmpty(name))
            parameters = new String[] { name };

        if (parameters.length > 0) {
            for (String param : parameters) {
                Object value = validationCtx.value(param);

                if (!validateRequired(checkAnnotation.required(), value)) {
                    errors.add(param, "validation.error.required");
                } else if (!validateMin(checkAnnotation.min(), value) && !validateMax(checkAnnotation.max(), value)) {
                    errors.add(param, "validation.error.between", value, checkAnnotation.min(), checkAnnotation.max());
                } else if (!validateMin(checkAnnotation.min(), value)) {
                    errors.add(param, "validation.error.min", value, checkAnnotation.min());
                } else if (!validateMax(checkAnnotation.max(), value)) {
                    errors.add(param, "validation.error.max", value, checkAnnotation.max());
                } else if (!validateMinLength(checkAnnotation.minLength(), value) && !validateMaxLength(checkAnnotation.maxLength(), value)) {
                    errors.add(param, "validation.error.betweenLength", value, checkAnnotation.minLength(), checkAnnotation.maxLength());
                } else if (!validateMinLength(checkAnnotation.minLength(), value)) {
                    errors.add(param, "validation.error.minLength", value, checkAnnotation.minLength());
                } else if (!validateMaxLength(checkAnnotation.maxLength(), value)) {
                    errors.add(param, "validation.error.maxLength", value, checkAnnotation.maxLength());
                } else if (!validateIs(checkAnnotation, validationCtx)) {
                    errors.add(param, "validation.error.is");
                }
            }
        } else if (!validateIs(checkAnnotation, validationCtx)) {
            errors.add(null, "validation.error.is");
        }
    }
}
