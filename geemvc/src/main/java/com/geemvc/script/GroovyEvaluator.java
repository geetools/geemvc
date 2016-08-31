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

import java.util.Map;
import java.util.Set;

import com.geemvc.annotation.Evaluator;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

@Evaluator("groovy:")
public class GroovyEvaluator extends AbstractEvaluator implements ScriptEvaluator {
    protected String mappedExpression = null;

    @Override
    public GroovyEvaluator build(String expression) {
        this.mappedExpression = expression;

        if (expression.trim().startsWith("groovy:"))
            expression = expression.substring(7);

        super.build(expression);

        return this;
    }

    @Override
    public boolean isResponsible() {
        return mappedExpression != null && mappedExpression.startsWith("groovy:");
    }

    @Override
    public boolean matches(EvaluatorContext ctx) {
        Map<String, ?> values = ctx.values();

        Binding binding = new Binding();

        if (values != null && !values.isEmpty()) {
            Set<String> keys = values.keySet();

            for (String name : keys) {
                if (ctx.isArray(name)) {
                    String[] val = (String[]) values.get(name);
                    binding.setVariable(name, val == null ? null : val.length == 1 ? val[0] : val);
                } else {
                    binding.setVariable(name, ctx.value(name));
                }
            }
        } else {
            binding.setVariable("req", ctx.requestContext().getRequest());
        }

        Boolean result = false;

        try {
            GroovyShell shell = new GroovyShell(binding);

            result = (Boolean) shell.evaluate(expression);

            if (result != null && result == true)
                ctx.resolve(mappedExpression);
        } catch (Throwable t) {
            // System.out.println(t.getMessage());
        }

        return result == null ? false : result;
    }
}
