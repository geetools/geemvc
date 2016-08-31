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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.geemvc.Char;

public class DefaultSimpleEvaluator extends AbstractEvaluator implements SimpleEvaluator {
    protected String mappedExpression = null;
    protected String targetKey = null;
    protected String targetValue = null;
    protected boolean isNegateCondition = false;
    protected boolean isExistsCondition = false;

    protected Pattern isRegexPattern = Pattern.compile("^.+=[ ]*\\/.+\\/[gi]*$");

    protected static final String NOT_EQUALS_OPERATOR = "!=";

    @Override
    public DefaultSimpleEvaluator build(String expression) {
        mappedExpression = expression;

        if (expression.trim().startsWith("simple:"))
            expression = expression.substring(7);

        super.build(expression);

        int operatorPos = expression.indexOf(NOT_EQUALS_OPERATOR);
        int operatorLen = 2;

        if (operatorPos == -1) {
            operatorPos = expression.indexOf(Char.EQUALS);
            operatorLen = 1;
        } else {
            isNegateCondition = true;
        }

        if (operatorPos != -1) {
            targetKey = expression.substring(0, operatorPos).trim();
            targetValue = expression.substring(operatorPos + operatorLen).trim();
        } else {
            targetKey = expression;
            isExistsCondition = true;
            return this;
        }

        return this;
    }

    @Override
    public boolean isResponsible() {
        boolean isSimple = expression != null
                && expression.startsWith("simple:")
                || (expression != null
                        && !expression.startsWith("groovy:")
                        && !expression.startsWith("js:")
                        && !expression.startsWith("mvel:")
                        && !expression.startsWith("regex:")
                        && !expression.contains("*")
                        && !expression.contains("=^")
                        && !expression.contains("!=^")
                        && !expression.contains("= ^")
                        && !expression.contains("!= ^"));

        if (isSimple && (expression.contains("=/") || expression.contains("= /"))) {
            Matcher m = isRegexPattern.matcher(expression);
            isSimple = !m.matches();
        }

        return isSimple;
    }

    @Override
    public boolean matches(EvaluatorContext ctx) {
        boolean isIgnoreCaseKeyMatch = ctx.isIgnoreCaseKeyMatch();
        boolean containsKey = isIgnoreCaseKeyMatch ? ctx.containsKeyIgnoreCase(targetKey) : ctx.containsKey(targetKey);

        if (isExistsCondition && containsKey) {
            ctx.resolve(mappedExpression, true, false);
            return true;
        }

        if (!containsKey)
            return false;

        Boolean foundValue = false;

        if (ctx.isArray(targetKey, isIgnoreCaseKeyMatch)) {
            String[] values = ctx.values(targetKey, isIgnoreCaseKeyMatch);

            if (values != null && values.length > 0) {
                for (String value : values) {
                    if (targetValue.equals(value)) {
                        foundValue = true;
                        break;
                    }
                }
            }
        } else {
            String value = ctx.value(targetKey, isIgnoreCaseKeyMatch);

            if (value != null && targetValue.equals(value)) {
                foundValue = true;
            }
        }

        if (!isNegateCondition && foundValue) {
            ctx.resolve(mappedExpression, true, false);
            return true;
        } else if (isNegateCondition && !foundValue) {
            ctx.resolve(mappedExpression, true, true);
            return true;
        }

        return false;
    }
}
