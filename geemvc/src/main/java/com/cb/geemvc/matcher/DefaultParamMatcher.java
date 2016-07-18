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

public class DefaultParamMatcher implements ParamMatcher {
    private static final long serialVersionUID = 8682403475843946688L;

    // Parameters mapped to best matching evaluator.
    private Map<String, Evaluator> paramEvaluators = null;

    @Inject
    private Injector injector;

    private final EvaluatorFactory evaluatorFactory;

    @Inject
    protected DefaultParamMatcher(EvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    public ParamMatcher build(String... mappedParameters) {
        if (mappedParameters == null || mappedParameters.length == 0)
            return this;

        this.paramEvaluators = new HashMap<>();

        for (String mappedParam : mappedParameters) {
            Evaluator evaluator = evaluatorFactory.find(mappedParam);

            if (evaluator != null) {
                paramEvaluators.put(mappedParam, evaluator);
            }
        }

        return this;
    }

    /**
     * Matches the request-path against the regular-expression that we created from the original path.
     */
    public boolean matches(RequestContext requestCtx, MatcherContext matcherCtx) {
        Map<String, String[]> requestParameters = requestCtx.getParameterMap();

        if (requestParameters == null || requestParameters.size() == 0) {
            if (paramEvaluators == null || paramEvaluators.size() == 0)
                return true;

            return false;
        }

        Set<String> keys = paramEvaluators.keySet();

        for (String mappedExpression : keys) {
            Evaluator evaluator = paramEvaluators.get(mappedExpression);

            EvaluatorContext evalCtx = injector.getInstance(EvaluatorContext.class).build(requestParameters).append(requestCtx);

            if (!evaluator.matches(evalCtx))
                return false;

            matcherCtx.resolve(evalCtx.resolvedExpressions());
        }

        return true;
    }
}
