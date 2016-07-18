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

import java.util.Map;

import com.cb.geemvc.Char;
import com.cb.geemvc.RequestContext;
import com.cb.geemvc.Str;
import com.cb.geemvc.bind.PropertyNode;
import com.cb.geemvc.inject.Injectors;
import com.google.inject.Injector;

public class DefaultValidationContext implements ValidationContext {
    protected RequestContext requestCtx = null;
    protected Map<String, Object> typedValues = null;

    @Override
    public ValidationContext build(RequestContext requestCtx, Map<String, Object> typedValues) {
        this.requestCtx = requestCtx;
        this.typedValues = typedValues;

        return this;
    }

    @Override
    public Map<String, Object> typedValues() {
        return typedValues;
    }

    @Override
    public RequestContext requestCtx() {
        return requestCtx;
    }

    @Override
    public Object value(String name) {
        // First try the standard way for simple (none-bean) values.
        Object value = typedValues.get(name);

        if (value != null)
            return value;

        String normalizedName = normalizeName(name);

        // Try again with normalized name before we attempt the more complex bean retrieval.
        if (name.indexOf(Char.DOT) == -1 && !Str.isEmpty(normalizedName)) {
            value = typedValues.get(normalizedName);

            if (value != null)
                return value;
        }

        if (name.indexOf(Char.DOT) != -1 || name.indexOf(Char.SQUARE_BRACKET_OPEN) != -1) {
            Object beanInstance = typedValues.get(normalizedName);

            String beanPropertyName = validExpression(name);
            Object val = beanPropertyValue(Str.isEmpty(beanPropertyName) ? normalizedName : beanPropertyName, beanInstance);

            return val;
        }

        return null;
    }

    protected String normalizeName(String expression) {
        int dotPos = expression.indexOf(Char.DOT);
        int squareBracketPos = expression.indexOf(Char.SQUARE_BRACKET_OPEN);

        // Seems to be a simple property name.
        if (dotPos == -1 && squareBracketPos == -1)
            return expression;

        int pos = dotPos != -1 && squareBracketPos != -1 ? Math.min(dotPos, squareBracketPos) : dotPos == -1 ? squareBracketPos : dotPos;

        return expression.substring(0, pos);
    }

    protected String validExpression(String expression) {
        int dotPos = expression.indexOf(Char.DOT);
        int squareBracketPos = expression.indexOf(Char.SQUARE_BRACKET_CLOSE);

        // Seems to be a simple property name.
        if (dotPos == -1 && squareBracketPos == -1)
            return expression;

        int pos = dotPos != -1 && squareBracketPos != -1 ? Math.min(dotPos, squareBracketPos) : dotPos == -1 ? squareBracketPos : dotPos;

        return expression.substring(pos + 1);
    }

    protected Object beanPropertyValue(String expression, Object beanInstance) {
        if (expression == null || beanInstance == null)
            return null;

        Injector injector = Injectors.provide();

        PropertyNode propertyNode = injector.getInstance(PropertyNode.class).build(expression, beanInstance.getClass());

        return propertyNode.get(beanInstance);
    }
}
