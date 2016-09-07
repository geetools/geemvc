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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mvel2.MVEL;

import com.geemvc.annotation.Evaluator;

@Evaluator("mvel:")
public class MvelEvaluator extends AbstractEvaluator implements ScriptEvaluator {
    protected String mappedExpression = null;

    @Override
    public MvelEvaluator build(String expression) {
        this.mappedExpression = expression;

        if (expression.trim().startsWith("mvel:"))
            expression = expression.substring(5);

        super.build(expression);

        return this;
    }

    @Override
    public boolean isResponsible() {
        return mappedExpression != null && mappedExpression.startsWith("mvel:");
    }

    @Override
    public boolean matches(EvaluatorContext ctx) {
        Map<String, ?> values = ctx.values();
        Map<String, Object> bindings = new HashMap<>();

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
            Serializable compiled = MVEL.compileExpression("import java.util.*; " + expression);

            result = (Boolean) MVEL.executeExpression(compiled, bindings);

            if (result != null && result == true)
                ctx.resolve(mappedExpression);
        } catch (Throwable t) {
            // System.out.println(t.getMessage());
        }

        return result == null ? false : result;
    }
}
