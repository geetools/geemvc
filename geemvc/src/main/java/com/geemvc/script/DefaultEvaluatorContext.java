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

package com.geemvc.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.geemvc.Char;
import com.geemvc.RequestContext;
import com.geemvc.Str;
import com.geemvc.helper.Strings;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultEvaluatorContext implements EvaluatorContext {
    protected Map<String, ?> values;
    protected Map<String, ?> valuesWithLowerCaseKeys;
    protected RequestContext requestCtx;
    protected List<String> resolvedExpressions;
    protected boolean isIgnoreCaseKeyMatch = false;

    @Inject
    protected Injector injector;

    @Override
    public EvaluatorContext build(Map<String, ?> values) {
        this.values = values;

        return this;
    }

    @Override
    public EvaluatorContext build(Map<String, ?> values, boolean isIgnoreCaseKeyMatch) {
        this.values = values;
        this.isIgnoreCaseKeyMatch = isIgnoreCaseKeyMatch;

        if (isIgnoreCaseKeyMatch)
            this.valuesWithLowerCaseKeys = injector.getInstance(Strings.class).keysToLowerCase(values);

        return this;
    }

    @Override
    public EvaluatorContext append(RequestContext requestCtx) {
        this.requestCtx = requestCtx;
        return this;
    }

    @Override
    public RequestContext requestContext() {
        return requestCtx;
    }

    @Override
    public Map<String, ?> values() {
        return values;
    }

    @Override
    public String[] values(String name) {
        Object o = values.get(name);
        return isArray(name) ? (String[]) o : new String[]{(String) o};
    }

    @Override
    public String[] values(String name, boolean ignoreKeyCase) {
        Object o = ignoreKeyCase ? valuesWithLowerCaseKeys.get(name.toLowerCase()) : values.get(name);
        return isArray(name, ignoreKeyCase) ? (String[]) o : new String[]{(String) o};
    }

    @Override
    public Object value(String name) {
        if (isArray(name)) {
            throw new RuntimeException("Use values(String name) as we are dealing with an array");
        } else {
            return values.get(name);
        }
    }

    @Override
    public String value(String name, boolean ignoreKeyCase) {
        if (isArray(name)) {
            throw new RuntimeException("Use values(String name) as we are dealing with an array");
        } else {
            return ignoreKeyCase ? (String) valuesWithLowerCaseKeys.get(name.toLowerCase()) : (String) values.get(name);
        }
    }

    @Override
    public boolean containsKey(String name) {
        return values.containsKey(name);
    }

    @Override
    public boolean containsKeyIgnoreCase(String name) {
        return name != null && valuesWithLowerCaseKeys.containsKey(name.toLowerCase());
    }

    @Override
    public int length(String name) {
        Object o = values.get(name);
        return isArray(name) ? ((String[]) o).length : o == null ? 0 : 1;
    }

    @Override
    public int length(String name, boolean ignoreKeyCase) {
        if (name == null)
            return 0;

        Object o = ignoreKeyCase ? valuesWithLowerCaseKeys.get(name.toLowerCase()) : values.get(name);
        return isArray(name) ? ((String[]) o).length : o == null ? 0 : 1;
    }

    @Override
    public boolean isArray(String name) {
        Object o = values.get(name);
        return o != null && (o instanceof String[]);
    }

    @Override
    public boolean isArray(String name, boolean ignoreKeyCase) {
        if (name == null)
            return false;

        Object o = ignoreKeyCase ? valuesWithLowerCaseKeys.get(name.toLowerCase()) : values.get(name);
        return o != null && (o instanceof String[]);
    }

    @Override
    public boolean isIgnoreCaseKeyMatch() {
        return isIgnoreCaseKeyMatch;
    }

    @Override
    public List<String> resolvedExpressions() {
        return resolvedExpressions;
    }

    public EvaluatorContext resolve(String mappedExpression) {
        resolve(mappedExpression, false, false);
        return this;
    }

    public EvaluatorContext resolve(String mappedExpression, boolean isStaticMatch, boolean isNegateMatch) {
        if (resolvedExpressions == null)
            resolvedExpressions = new ArrayList<>();

        StringBuilder prefix = new StringBuilder(Str.SQUARE_BRACKET_OPEN);

        if (isStaticMatch) {
            if (isNegateMatch)
                prefix.append(Char.EXCLAMATION_MARK);

            prefix.append(Char.EQUALS);
        } else {
            prefix.append(Char.DOLLAR);
        }

        prefix.append(Char.SQUARE_BRACKET_CLOSE).append(Char.COLON).append(mappedExpression);

        resolvedExpressions.add(prefix.toString());

        return this;
    }
}
