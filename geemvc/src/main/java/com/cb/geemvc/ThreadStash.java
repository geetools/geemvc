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

package com.cb.geemvc;

import java.util.HashMap;
import java.util.Map;

public class ThreadStash {
    protected static ThreadLocal<Map<Object, Object>> STASH = new ThreadLocal<Map<Object, Object>>() {
        protected Map<Object, Object> initialValue() {
            return new HashMap<Object, Object>();
        }
    };

    public static void put(Object key, Object value) {
        STASH.get().put(key, value);
    }

    public static Object get(Object key) {
        return STASH.get().get(key);
    }

    public static void clear() {
        STASH.get().clear();
    }

    public static void cleanup() {
        clear();
        STASH.remove();
    }
}
