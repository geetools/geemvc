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

package com.cb.geemvc.handler;

import java.util.Collection;

public class DefaultHandlerResolverStats implements HandlerResolverStats {
    private static final long serialVersionUID = 139759354949935010L;

    private Collection<String> resolvedParameters = null;
    private Collection<String> resolvedHeaders = null;
    private Collection<String> resolvedCookes = null;
    private Collection<String> resolvedHandlesScripts = null;

    private boolean isInitialized = false;

    public HandlerResolverStats build(Collection<String> resolvedParameters, Collection<String> resolvedHeaders, Collection<String> resolvedCookes, Collection<String> resolvedHandlesScripts) {
        if (!isInitialized) {
            this.resolvedParameters = resolvedParameters;
            this.resolvedHeaders = resolvedHeaders;
            this.resolvedCookes = resolvedCookes;
            this.resolvedHandlesScripts = resolvedHandlesScripts;

            isInitialized = true;
        } else {
            throw new RuntimeException("HandlerResolverStats.build() can only be called once");
        }

        return this;
    }

    public int numResolvedParameters() {
        return resolvedParameters == null ? 0 : resolvedParameters.size();
    }

    public int numResolvedStaticParameters() {
        if (resolvedParameters == null)
            return 0;

        return (int) resolvedParameters.stream().filter((p) -> {
            return p.startsWith("[=]:") || p.startsWith("[!=]:");
        }).count();
    }

    public int numResolvedStaticNegateParameters() {
        if (resolvedParameters == null)
            return 0;

        return (int) resolvedParameters.stream().filter((p) -> {
            return p.startsWith("[!=]:");
        }).count();
    }

    public int numResolvedDynamicParameters() {
        if (resolvedParameters == null)
            return 0;

        return (int) resolvedParameters.stream().filter((p) -> {
            return p.startsWith("[$]:");
        }).count();
    }

    public int numResolvedHeaders() {
        return resolvedHeaders == null ? 0 : resolvedHeaders.size();
    }

    public int numResolvedStaticHeaders() {
        if (resolvedHeaders == null)
            return 0;

        return (int) resolvedHeaders.stream().filter((p) -> {
            return p.startsWith("[=]:") || p.startsWith("[!=]:");
        }).count();
    }

    public int numResolvedStaticNegateHeaders() {
        if (resolvedHeaders == null)
            return 0;

        return (int) resolvedHeaders.stream().filter((p) -> {
            return p.startsWith("[!=]:");
        }).count();
    }

    public int numResolvedDynamicHeaders() {
        if (resolvedHeaders == null)
            return 0;

        return (int) resolvedHeaders.stream().filter((p) -> {
            return p.startsWith("[$]:");
        }).count();
    }

    public int numResolvedCookies() {
        return resolvedCookes == null ? 0 : resolvedCookes.size();
    }

    public int numResolvedStaticCookies() {
        if (resolvedCookes == null)
            return 0;

        return (int) resolvedCookes.stream().filter((p) -> {
            return p.startsWith("[=]:") || p.startsWith("[!=]:");
        }).count();
    }

    public int numResolvedStaticNegateCookies() {
        if (resolvedCookes == null)
            return 0;

        return (int) resolvedCookes.stream().filter((p) -> {
            return p.startsWith("[!=]:");
        }).count();
    }

    public int numResolvedDynamicCookies() {
        if (resolvedCookes == null)
            return 0;

        return (int) resolvedCookes.stream().filter((p) -> {
            return p.startsWith("[$]:");
        }).count();
    }

    public int numResolvedHandlesScripts() {
        return resolvedHandlesScripts == null ? 0 : resolvedHandlesScripts.size();
    }

    @Override
    public String toString() {
        return "DefaultHandlerResolverStats [resolvedParameters=" + resolvedParameters + ", resolvedHeaders=" + resolvedHeaders + ", resolvedCookes=" + resolvedCookes + ", resolvedHandlesScripts=" + resolvedHandlesScripts + ", isInitialized="
                + isInitialized + ", numResolvedParameters=" + numResolvedParameters() + ", numResolvedStaticParameters=" + numResolvedStaticParameters() + ", numResolvedStaticNegateParameters=" + numResolvedStaticNegateParameters()
                + ", numResolvedDynamicParameters=" + numResolvedDynamicParameters() + ", numResolvedHeaders=" + numResolvedHeaders() + ", numResolvedStaticHeaders=" + numResolvedStaticHeaders() + ", numResolvedStaticNegateHeaders="
                + numResolvedStaticNegateHeaders() + ", numResolvedDynamicHeaders=" + numResolvedDynamicHeaders() + ", numResolvedCookes=" + numResolvedCookies() + ", numResolvedStaticCookes=" + numResolvedStaticCookies()
                + ", numResolvedStaticNegateCookes=" + numResolvedStaticNegateCookies() + ", numResolvedDynamicCookes=" + numResolvedDynamicCookies() + ", numResolvedHandlesScripts=" + numResolvedHandlesScripts() + "]";
    }
}
