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

package com.cb.geemvc.condition;

import java.util.HashSet;
import java.util.Set;

public class DefaultCondition implements Condition {
    private static final long serialVersionUID = -3066810334383934452L;

    protected static final String EQUALS = "=";
    protected static final String NOT_EQUALS = "!=";
    protected static final String GREATER_THAN = ">";
    protected static final String LESS_THAN = "<";
    protected static final String GREATER_EQUALS_THAN = ">=";
    protected static final String LESS_EQUALS_THAN = "<=";

    protected static Set<String> validOperators = new HashSet<>();

    static {
        validOperators.add(EQUALS);
        validOperators.add(NOT_EQUALS);
        validOperators.add(GREATER_THAN);
        validOperators.add(LESS_THAN);
        validOperators.add(GREATER_EQUALS_THAN);
        validOperators.add(LESS_EQUALS_THAN);
    }

    protected String operator = null;
    protected Object value = null;

    protected boolean isInitialized = false;

    public Condition build(String condition) {
        if (!isInitialized) {
            this.operator = operator;
            isInitialized = true;
        } else {
            throw new RuntimeException("DefaultCondition.build() can only be called once");
        }

        return this;
    }

    public Condition build(String operator, Object value) {
        if (!isInitialized) {
            this.operator = operator;
            isInitialized = true;
        } else {
            throw new RuntimeException("DefaultCondition.build() can only be called once");
        }

        return this;
    }

    public boolean conditionMatches() {
        return true;
    }
}
