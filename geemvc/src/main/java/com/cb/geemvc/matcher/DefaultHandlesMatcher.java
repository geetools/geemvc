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

package com.cb.geemvc.matcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.script.Evaluator;
import com.cb.geemvc.script.EvaluatorContext;
import com.cb.geemvc.script.EvaluatorFactory;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultHandlesMatcher implements HandlesMatcher {
    private static final long serialVersionUID = 8682403475843946688L;

    // Headers mapped to best matching evaluator.
    protected Map<String, Evaluator> handlesEvaluators = null;

    protected final EvaluatorFactory evaluatorFactory;

    @Inject
    protected Injector injector;

    @Inject
    protected DefaultHandlesMatcher(EvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    public HandlesMatcher build(String... mappedHandles) {
        if (mappedHandles == null || mappedHandles.length == 0)
            return this;

        this.handlesEvaluators = new HashMap<>();

        for (String mappedHandle : mappedHandles) {
            Evaluator evaluator = evaluatorFactory.find(mappedHandle);

            if (evaluator != null) {
                handlesEvaluators.put(mappedHandle, evaluator);
            }
        }

        return this;
    }

    /**
     * Matches the request-path against the regular-expression that we created from the original path.
     */
    public boolean matches(RequestContext requestCtx, MatcherContext matcherCtx) {
        Set<String> keys = handlesEvaluators.keySet();

        for (String mappedExpression : keys) {
            Evaluator evaluator = handlesEvaluators.get(mappedExpression);

            EvaluatorContext evalCtx = injector.getInstance(EvaluatorContext.class).append(requestCtx);

            if (!evaluator.matches(evalCtx))
                return false;

            matcherCtx.resolve(evalCtx.resolvedExpressions());
        }

        return true;
    }
}
