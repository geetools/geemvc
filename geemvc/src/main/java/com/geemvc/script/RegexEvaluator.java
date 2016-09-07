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

@com.geemvc.annotation.Evaluator("regex:")
public class RegexEvaluator extends DefaultSimpleEvaluator {
    protected Pattern mappedPattern = null;
    protected String mappedExpression = null;

    @Override
    public RegexEvaluator build(String expression) {
        String mappedExpression = new String(expression);

        if (expression.trim().startsWith("regex:"))
            expression = expression.substring(7);

        super.build(expression);

        this.mappedExpression = mappedExpression;

        return this;
    }

    @Override
    public boolean isResponsible() {
        if (mappedExpression == null)
            return false;

        if (mappedExpression.startsWith("regex:")
                || expression.contains("=/")
                || expression.contains("= /")
                || expression.contains("!=/")
                || expression.contains("!= /")) {

            Matcher m = isRegexPattern.matcher(expression);

            return m.matches();
        }

        return false;
    }

    @Override
    public boolean matches(EvaluatorContext ctx) {
        if (!ctx.containsKey(targetKey))
            return false;

        Boolean foundValue = false;

        if (ctx.isArray(targetKey)) {
            String[] values = ctx.values(targetKey);

            if (values != null && values.length > 0) {
                if (mappedPattern == null)
                    mappedPattern = toPattern(targetValue);

                for (String value : values) {
                    Matcher m = mappedPattern.matcher(value);

                    if (m.matches()) {
                        foundValue = true;
                        break;
                    }
                }
            }
        } else {
            Object value = ctx.value(targetKey);

            if (value != null) {
                if (mappedPattern == null)
                    mappedPattern = toPattern(targetValue);

                Matcher m = mappedPattern.matcher((String) value);

                if (m.matches()) {
                    foundValue = true;
                }
            }
        }

        if (!isNegateCondition && foundValue) {
            ctx.resolve(mappedExpression);
            return true;
        } else if (isNegateCondition && !foundValue) {
            ctx.resolve(mappedExpression);
            return true;
        }

        return false;
    }

    protected Pattern toPattern(String expression) {
        int firstSlashIdx = expression.indexOf("=/") + 1;

        if (firstSlashIdx == -1)
            firstSlashIdx = expression.indexOf("= /") + 2;

        int lastSlashIdx = expression.lastIndexOf(Char.SLASH);

        return Pattern.compile(expression.substring(firstSlashIdx + 1, lastSlashIdx).trim());
    }
}
