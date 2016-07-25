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

package com.geemvc.matcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.geemvc.RequestContext;
import com.geemvc.script.Evaluator;
import com.geemvc.script.EvaluatorContext;
import com.geemvc.script.EvaluatorFactory;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultCookieMatcher implements CookieMatcher {
    private static final long serialVersionUID = 8682403475843946688L;

    // Headers mapped to best matching evaluator.
    protected Map<String, Evaluator> cookieEvaluators = null;

    @Inject
    protected Injector injector;

    protected final EvaluatorFactory evaluatorFactory;

    @Inject
    protected DefaultCookieMatcher(EvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    public CookieMatcher build(String... mappedCookies) {
        if (mappedCookies == null || mappedCookies.length == 0)
            return this;

        this.cookieEvaluators = new HashMap<>();

        for (String mappedCookie : mappedCookies) {
            Evaluator evaluator = evaluatorFactory.find(mappedCookie);

            if (evaluator != null) {
                cookieEvaluators.put(mappedCookie, evaluator);
            }
        }

        return this;
    }

    /**
     * Matches the request-path against the regular-expression that we created from the original path.
     */
    public boolean matches(RequestContext requestCtx, MatcherContext matcherCtx) {
        Map<String, String[]> requestCookies = requestCtx.getCookieMap();

        if (requestCookies == null || requestCookies.size() == 0) {
            if (cookieEvaluators == null || cookieEvaluators.size() == 0)
                return true;

            return false;
        }

        Set<String> keys = cookieEvaluators.keySet();

        for (String mappedExpression : keys) {
            Evaluator evaluator = cookieEvaluators.get(mappedExpression);

            EvaluatorContext evalCtx = injector.getInstance(EvaluatorContext.class).build(requestCookies).append(requestCtx);

            if (!evaluator.matches(evalCtx))
                return false;

            matcherCtx.resolve(evalCtx.resolvedExpressions());
        }

        return true;
    }
}
