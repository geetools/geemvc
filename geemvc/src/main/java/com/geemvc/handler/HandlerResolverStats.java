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

package com.geemvc.handler;

import java.io.Serializable;
import java.util.Collection;

public interface HandlerResolverStats extends Serializable {
    HandlerResolverStats build(Collection<String> resolvedParameters, Collection<String> resolvedHeaders, Collection<String> resolvedCookies, Collection<String> resolvedHandlesScripts);

    int numResolvedParameters();

    int numResolvedStaticParameters();

    int numResolvedStaticNegateParameters();

    int numResolvedDynamicParameters();

    int numResolvedHeaders();

    int numResolvedStaticHeaders();

    int numResolvedStaticNegateHeaders();

    int numResolvedDynamicHeaders();

    int numResolvedCookies();

    int numResolvedStaticCookies();

    int numResolvedStaticNegateCookies();

    int numResolvedDynamicCookies();

    int numResolvedHandlesScripts();
}
