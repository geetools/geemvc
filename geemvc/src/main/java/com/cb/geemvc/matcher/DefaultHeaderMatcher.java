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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.Str;
import com.cb.geemvc.helper.Strings;
import com.cb.geemvc.script.Evaluator;
import com.cb.geemvc.script.EvaluatorContext;
import com.cb.geemvc.script.EvaluatorFactory;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultHeaderMatcher implements HeaderMatcher {
    private static final long serialVersionUID = 8682403475843946688L;

    // Headers mapped to best matching evaluator.
    private Map<String, Evaluator> headerEvaluators = null;

    protected Set<String> matchAny = new HashSet<>(Arrays.asList("content-type", "accept"));

    @Inject
    private Injector injector;

    private final EvaluatorFactory evaluatorFactory;

    @Inject
    protected DefaultHeaderMatcher(EvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    public HeaderMatcher build(String... mappedHeaders) {
        if (mappedHeaders == null || mappedHeaders.length == 0)
            return this;

        String[] mappedHeadersWithLowerCaseKeys = injector.getInstance(Strings.class).keysToLowerCase(mappedHeaders);

        this.headerEvaluators = new HashMap<>();

        for (String mappedHeader : mappedHeaders) {
            Evaluator evaluator = evaluatorFactory.find(mappedHeader);

            if (evaluator != null) {
                headerEvaluators.put(mappedHeader, evaluator);
            }
        }

        return this;
    }

    /**
     * Matches the request-path against the regular-expression that we created from the original path.
     */
    public boolean matches(RequestContext requestCtx, MatcherContext matcherCtx) {
        Map<String, String[]> requestHeaders = requestCtx.getHeaderMap();

        if (requestHeaders == null || requestHeaders.size() == 0) {
            if (headerEvaluators == null || headerEvaluators.size() == 0)
                return true;

            return false;
        }

        Set<String> keys = headerEvaluators.keySet();
        Set<String> matchedHeaderNames = new HashSet<>();
        Set<String> mappedHeaderNames = new HashSet<>();

        for (String mappedExpression : keys) {
            Evaluator evaluator = headerEvaluators.get(mappedExpression);

            EvaluatorContext evalCtx = injector.getInstance(EvaluatorContext.class).build(requestHeaders, true).append(requestCtx);

            String name = headerName(mappedExpression);

            mappedHeaderNames.add(name);

            if (evaluator.matches(evalCtx)) {
                matchedHeaderNames.add(name);
            } else {
                if (!matchAny.contains(name))
                    return false;
            }

            if (evalCtx.resolvedExpressions() != null)
                matcherCtx.resolve(evalCtx.resolvedExpressions());
        }

        return mappedHeaderNames.size() == matchedHeaderNames.size();
    }

    protected String headerName(String header) {
        if (header == null || !header.contains(Str.EQUALS))
            return null;

        return header.substring(0, header.indexOf(Str.EQUALS)).trim().toLowerCase();
    }
}
