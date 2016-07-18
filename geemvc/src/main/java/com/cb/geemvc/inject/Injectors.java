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

package com.cb.geemvc.inject;

import com.google.inject.Injector;

public class Injectors {
    protected static final ThreadLocal<InjectorProvider> injectorProviderThreadLocal = new ThreadLocal<>();

    public static void set(InjectorProvider injectorProvider) {
        injectorProviderThreadLocal.set(injectorProvider);
    }

    public static InjectorProvider get() {
        return injectorProviderThreadLocal.get();
    }

    public static Injector provide() {
        return injectorProviderThreadLocal.get().provide();
    }

    public static void clear() {
        injectorProviderThreadLocal.remove();
    }
}
