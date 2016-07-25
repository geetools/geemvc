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

package com.geemvc;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadStash {
    protected static final String REQUEST_STACK_KEY = "REQUEST_STACK";
    protected static final Integer GLOBAL_MAP_KEY = 0;

    protected static ThreadLocal<Map<Integer, Map<Object, Object>>> STASH = new ThreadLocal<Map<Integer, Map<Object, Object>>>() {
        protected Map<Integer, Map<Object, Object>> initialValue() {
            return new HashMap<Integer, Map<Object, Object>>();
        }
    };

    /**
     * Adds a value for the provided key to the current local map.
     */
    public static void put(Object key, Object value) {
        localMap().put(key, value);
    }

    /**
     * Return a value for the provided key using the current local map.
     */
    public static Object get(Object key) {
        return localMap().get(key);
    }

    /**
     * Cleans up the current local map.
     */
    public static void clear() {
        clearLocalMap();
    }

    /**
     * Cleans up the current local map. If all local maps have been cleaned up, we remove the complete stash for this thread.
     */
    public static void cleanup() {
        clearLocalMap();

        if (requestStackIsEmpty())
            STASH.remove();
    }

    /**
     * Adds a new element to the request stack. As more than 1 request can take place in a single Thread,
     * we give each one its own thread local space.
     */
    public static void prepare(RequestContext requestCtx) {
        String requestURL = new StringBuilder(requestCtx.getMethod())
                .append(Char.SPACE).append(requestCtx.getPath())
                .append(Char.QUESTION_MARK).append(((HttpServletRequest) requestCtx.getRequest()).getQueryString()).toString();

        requestStack().add(0, requestURL.hashCode());
    }

    /**
     * Returns the current request stack list, creating a new one if it does not exist yet.
     */
    protected static List<Integer> requestStack() {
        return requestStack(true);
    }

    /**
     * Returns the current request stack list, optionally creating a new one if it does not exist yet.
     */
    protected static List<Integer> requestStack(boolean isCreate) {
        Map<Object, Object> globalMap = globalMap();
        List<Integer> reqStack = (List<Integer>) globalMap.get(REQUEST_STACK_KEY);

        if (reqStack == null && isCreate) {
            reqStack = new ArrayList<>();
            globalMap.put(REQUEST_STACK_KEY, reqStack);
        }

        return reqStack;
    }

    /**
     * Returns a global map that is not specific to the current request.
     */
    protected static Map<Object, Object> globalMap() {
        Map<Integer, Map<Object, Object>> stackMaps = (Map<Integer, Map<Object, Object>>) STASH.get();
        Map<Object, Object> globalMap = stackMaps.get(GLOBAL_MAP_KEY);

        if (globalMap == null) {
            globalMap = new HashMap<>();
            stackMaps.put(GLOBAL_MAP_KEY, globalMap);
        }

        return globalMap;
    }

    /**
     * Returns a local map specific to the current request.
     */
    protected static Map<Object, Object> localMap() {
        List<Integer> reqStack = requestStack();
        Integer currentStackKey = reqStack.get(0);

        Map<Integer, Map<Object, Object>> stackMaps = STASH.get();
        Map<Object, Object> localMap = stackMaps.get(currentStackKey);

        if (localMap == null) {
            localMap = new HashMap<>();
            stackMaps.put(currentStackKey, localMap);
        }

        return localMap;
    }

    /**
     * Clears the local map, specific to the current request.
     */
    protected static void clearLocalMap() {
        List<Integer> reqStack = requestStack();

        if (reqStack == null || reqStack.isEmpty())
            return;

        Integer currentStackKey = reqStack.remove(0);

        if (currentStackKey != null) {
            Map<Integer, Map<Object, Object>> stackMaps = STASH.get();
            stackMaps.remove(currentStackKey);
        }
    }

    /**
     * Checks if the stack is empty or if there are still local maps that exist for other requests.
     */
    protected static boolean requestStackIsEmpty() {
        List<Integer> reqStack = requestStack(false);
        return reqStack == null || reqStack.isEmpty();
    }
}
