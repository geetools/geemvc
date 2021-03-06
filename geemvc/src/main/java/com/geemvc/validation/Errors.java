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

package com.geemvc.validation;

import java.lang.*;
import java.util.Set;

public interface Errors {
    void add(String field, String message, Object... args);

    void add(String message, Object... args);

    boolean exist(String field);

    Error get(String field);

    Set<Error> globalErrors();

    Set<Error> allErrors();

    Set<Error> fieldErrors();

    boolean isEmpty();

    static final String GLOBAL_ERROR_KEY = "__globalErrors";
}
