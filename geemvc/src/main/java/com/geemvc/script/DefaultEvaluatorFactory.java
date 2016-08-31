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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.geemvc.Str;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class DefaultEvaluatorFactory implements EvaluatorFactory {
    protected final ReflectionProvider reflectionProvider;
    // Test if the simple evaluator is sufficient or whether we need to use a dynamic scripting language.
    protected Pattern isSimplePattern = Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9_-]*[ ]*[!]?[=]{1}[ ]*[^=~]+");

    protected Map<String, Class<? extends Evaluator>> evaluators = new HashMap<>();

    @Inject
    protected Injector injector;

    @Inject
    public DefaultEvaluatorFactory(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
        build();
    }

    @SuppressWarnings("unchecked")
    protected void build() {
        Set<Class<?>> evaluatorClasses = reflectionProvider.locateEvaluators();

        for (Class<?> evalClass : evaluatorClasses) {
            add((Class<? extends Evaluator>) evalClass);
        }
    }

    protected void add(Class<? extends Evaluator> evalClass) {
        com.geemvc.annotation.Evaluator evalAnno = evalClass.getAnnotation(com.geemvc.annotation.Evaluator.class);

        if (evalAnno != null && !Str.isEmpty(evalAnno.value())) {
            evaluators.put(evalAnno.value(), evalClass);
        }
    }

    @Override
    public Evaluator get(String name, String expression) {
        Class<? extends Evaluator> evalClass = evaluators.get(name);

        return evalClass == null ? null : injector.getInstance(evalClass).build(expression);
    }

    @Override
    public Evaluator find(String expression) {
        Collection<Class<? extends Evaluator>> evalClasses = evaluators.values();

        for (Class<? extends Evaluator> evalClass : evalClasses) {
            Evaluator evaluator = injector.getInstance(evalClass).build(expression);

            if (evaluator.isResponsible()) {
                return evaluator;
            }
        }

        // Do simple check first.
        if (isSimpleExpression(expression)) {
            return injector.getInstance(SimpleEvaluator.class).build(expression);
        }

        Matcher m = isSimplePattern.matcher(expression);

        if (m.matches()) {
            return injector.getInstance(SimpleEvaluator.class).build(expression);
        } else {
            return injector.getInstance(ScriptEvaluator.class).build(expression);
        }
    }

    /**
     * Basic check so that regular expressions are only needed for more advanced cases.
     * 
     * @param expression
     * @return
     */
    protected boolean isSimpleExpression(String expression) {
        return !expression.contains(Str.EQUALS_2X)
                && !expression.contains(Str.LESS_THAN)
                && !expression.contains(Str.GREATER_THAN)
                && !expression.contains(Str.EQUALS_TILDE)
                && !expression.contains(Str.AMPERSAND_2X)
                && !expression.contains(Str.PIPE_2X);
    }
}
