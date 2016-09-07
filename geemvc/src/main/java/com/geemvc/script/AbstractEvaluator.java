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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;

public abstract class AbstractEvaluator implements Evaluator {
    protected String expression = null;

    protected Pattern selfPropertiesPattern = Pattern.compile("self\\.([_a-z]\\w*)+");

    @Inject
    protected ReflectionProvider reflectionProvider;

    @Logger
    protected Log log;

    public Evaluator build(String expression) {
        if (expression != null)
            this.expression = expression.trim();

        return this;
    }

    protected Object findSelfBean(String expression, EvaluatorContext ctx) {
        // No self variables exist to analyze.
        if (!expression.contains("self."))
            return null;

        Set<String> selfProperties = new HashSet<>();

        // Extract all self variable names.
        Matcher m = selfPropertiesPattern.matcher(expression);
        while (m.find()) {
            selfProperties.add(m.group(1).trim());
        }

        Map<String, ?> values = ctx.values();

        Object selfBeanObject = null;
        int numFoundBeanProperties = 0;

        if (values != null && !values.isEmpty()) {
            for (Map.Entry<String, ?> entry : values.entrySet()) {
                String name = entry.getKey();

                if (!ctx.isArray(name)) {
                    Object val = ctx.value(name);

                    // If the current value is a simple type there is nothing to inspect.
                    if (reflectionProvider.isSimpleType(val.getClass()))
                        continue;

                    Class<?> beanType = val.getClass();

                    // Here we make sure that all "self" variables exist in the bean.
                    numFoundBeanProperties = 0;
                    for (String selfPropertyName : selfProperties) {
                        if (reflectionProvider.getField(beanType, selfPropertyName) != null) {
                            numFoundBeanProperties++;
                        }
                    }

                    // If the number of self properties match the number of found bean properties, we can be pretty sure to have found the correct
                    // bean.
                    if (numFoundBeanProperties == selfProperties.size()) {
                        if (selfBeanObject == null) {
                            selfBeanObject = val;
                        } else {
                            // If however we have found another bean with all the properties matching, we cannot safely resolve the "self" object.
                            throw new IllegalStateException("Unable to resolve a unique bean for the 'self' declaration in the expression '" + expression + "' as more than one bean in the request have the properties " + selfProperties
                                    + ". Try using the bean name instead, e.g. beanName.myProperty instead of self.myProperty.");
                        }

                        break;
                    }
                }
            }
        }

        // Not good, we are unable to find a matching bean for the self declaration in the expression.
        if (selfBeanObject == null)
            throw new IllegalStateException("Unable to resolve a unique bean for the 'self' declaration in the expression '" + expression + "' as no bean in the request has all the matching properties " + selfProperties
                    + ". Try using the bean name instead, e.g. beanName.myProperty instead of self.myProperty.");

        return selfBeanObject;
    }
}
