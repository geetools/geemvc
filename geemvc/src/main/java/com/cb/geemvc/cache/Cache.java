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

package com.cb.geemvc.cache;

import java.util.concurrent.Callable;
import java.util.function.Function;

public interface Cache {
    void put(Object key, Object value);

    void put(Object cacheKey, Object key, Object value);

    Object putIfAbsent(Object key, Object value);

    Object putIfAbsent(Object cacheKey, Object key, Object value);

    Object get(Object key);

//    Object get(Object key, Function function);

    Object get(Object key, Callable function);

    Object get(Object cacheKey, Object key);

//    Object get(Object cacheKey, Object key, Function function);

    Object get(Object cacheKey, Object key, Callable function);

    boolean containsKey(Object key);

    boolean containsKey(Object cacheKey, Object key);

    void remove(Object key);

    void remove(Object cacheKey, Object key);

    void clear();

    void clear(Object cacheKey);
}
