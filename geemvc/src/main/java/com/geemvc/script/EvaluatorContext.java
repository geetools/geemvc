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

import java.util.List;
import java.util.Map;

import com.geemvc.RequestContext;

public interface EvaluatorContext {
    EvaluatorContext build(Map<String, ?> values);

    EvaluatorContext build(Map<String, ?> values, boolean isLowerCaseKeyMatch);

    EvaluatorContext append(RequestContext requestCtx);

    RequestContext requestContext();

    Map<String, ?> values();

    String[] values(String name);

    String[] values(String name, boolean ignoreKeyCase);

    Object value(String name);

    String value(String name, boolean ignoreKeyCase);

    boolean containsKey(String name);

    boolean containsKeyIgnoreCase(String name);

    int length(String name);

    int length(String name, boolean ignoreKeyCase);

    boolean isArray(String name);

    boolean isArray(String name, boolean ignoreKeyCase);

    boolean isIgnoreCaseKeyMatch();

    List<String> resolvedExpressions();

    EvaluatorContext resolve(String mappedExpression);

    EvaluatorContext resolve(String mappedExpression, boolean isStaticMatch, boolean isNegateMatch);
}
