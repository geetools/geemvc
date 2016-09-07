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

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.geemvc.annotation.Evaluator;

@Evaluator("js:")
public class JavaScriptEvaluator extends AbstractEvaluator implements ScriptEvaluator {
    protected String mappedExpression = null;

    @Override
    public JavaScriptEvaluator build(String expression) {
        this.mappedExpression = expression;

        if (expression.trim().startsWith("js:"))
            expression = expression.substring(3);

        super.build(expression);

        return this;
    }

    @Override
    public boolean isResponsible() {
        return mappedExpression != null && mappedExpression.startsWith("js:");
    }

    @Override
    public boolean matches(EvaluatorContext ctx) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        Map<String, ?> values = ctx.values();

        Bindings bindings = engine.createBindings();

        if (values != null && !values.isEmpty()) {
            Set<String> keys = values.keySet();

            for (String name : keys) {
                if (ctx.isArray(name)) {
                    String[] val = (String[]) values.get(name);
                    bindings.put(name, val == null ? null : val.length == 1 ? val[0] : val);
                } else {
                    bindings.put(name, ctx.value(name));
                }
            }

            Object selfBean = findSelfBean(expression, ctx);

            if (selfBean != null)
                bindings.put("self", selfBean);
            
        } else {
            bindings.put("req", ctx.requestContext().getRequest());
        }

        Boolean result = false;

        try {
            result = (Boolean) engine.eval(expression, bindings);

            if (result != null && result == true)
                ctx.resolve(mappedExpression);
        } catch (Throwable t) {
            // System.out.println(t.getMessage());
        }

        return result == null ? false : result;
    }
}
