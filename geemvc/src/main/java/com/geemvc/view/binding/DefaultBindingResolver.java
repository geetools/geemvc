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

package com.geemvc.view.binding;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.geemvc.Str;
import com.geemvc.cache.Cache;
import com.geemvc.helper.Paths;
import com.geemvc.intercept.When;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.matcher.PathMatcher;
import com.geemvc.reflect.ReflectionProvider;
import com.geemvc.view.binding.annotation.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultBindingResolver implements BindingResolver {
    protected final ReflectionProvider reflectionProvider;
    protected final Paths paths;

    @Inject
    protected Injector injector;

    @Inject
    protected Cache cache;

    @Logger
    protected Log log;

    protected static final String RESOLVED_BINDINGS_CACHE_KEY = "geemvc/resolvedBindings/%s";

    @Inject
    public DefaultBindingResolver(ReflectionProvider reflectionProvider, Paths paths) {
        this.reflectionProvider = reflectionProvider;
        this.paths = paths;
    }

    @Override
    public Set<Bindable> resolveBindings(BindingContext bindingCtx) {
        String handlerResultViewPath = bindingCtx.result().view();

        String cacheKey = String.format(RESOLVED_BINDINGS_CACHE_KEY, handlerResultViewPath);

        Set<Bindable> bindings = (Set<Bindable>) cache.get(DefaultBindingResolver.class, cacheKey, () -> {

            log.trace("Looking for view bindings.");

            Set<Bindable> viewBindings = reflectionProvider.locateBindings();

            log.trace("Found {} view bindings.", () -> viewBindings == null ? 0 : viewBindings.size());

            Set<Bindable> matchingViewBindings = new LinkedHashSet<>();

            log.trace("Filtering bindings for view path '{}'.", () -> handlerResultViewPath);

            for (Bindable viewBinding : viewBindings) {
                Binding binding = viewBinding.getClass().getAnnotation(Binding.class);

                if (isViewPathValid(handlerResultViewPath, binding)) {
                    matchingViewBindings.add(viewBinding);
                }
            }

            log.trace("Found {} matching bindings for view path '{}'.", () -> matchingViewBindings == null ? 0 : matchingViewBindings.size(), () -> handlerResultViewPath);

            return matchingViewBindings;
        });

        Set<Bindable> filteredBindings = new HashSet<>();

        for (Bindable viewBinding : bindings) {
            Binding binding = viewBinding.getClass().getAnnotation(Binding.class);

            if ((binding.when() == When.ALWAYS || (binding.when() == When.NO_ERRORS && bindingCtx.errors().isEmpty())
                    || (binding.when() == When.HAS_ERRORS && !bindingCtx.errors().isEmpty())) && viewBinding.isBindable(bindingCtx)) {
                filteredBindings.add(viewBinding);
            }
        }

        return filteredBindings;
    }

    protected boolean isViewPathValid(String handlerResultViewPath, Binding binding) {
        if (binding.view().length == 0 && binding.ignore().length == 0)
            return true;

        boolean matches = false;

        // The pathMatcher expects a slash at the beginning.
        if (!handlerResultViewPath.startsWith(Str.SLASH))
            handlerResultViewPath = Str.SLASH + handlerResultViewPath;

        for (String bindingViewPath : binding.view()) {
            PathMatcher pathMatcher = injector.getInstance(PathMatcher.class).build(bindingViewPath, Str.EMPTY);

            if (pathMatcher.matches(handlerResultViewPath)) {
                matches = true;
            }
        }

        if (matches == true && binding.ignore().length > 0) {
            for (String bindingIgnoreViewPath : binding.ignore()) {
                PathMatcher pathMatcher = injector.getInstance(PathMatcher.class).build(bindingIgnoreViewPath, Str.EMPTY);

                if (pathMatcher.matches(handlerResultViewPath)) {
                    matches = false;
                }
            }
        }

        return matches;
    }
}
