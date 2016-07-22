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

package com.cb.geemvc.i18n.notice;

import java.util.Arrays;

public class DefaultNotice implements Notice {
    protected String field;
    protected String message;
    protected Object[] args;

    @Override
    public Notice build(String message) {
        this.message = message;
        return this;
    }

    @Override
    public Notice build(String message, Object... args) {
        this.message = message;
        this.args = args;
        return this;
    }

    @Override
    public Notice build(String field, String message) {
        this.field = field;
        this.message = message;
        return this;
    }

    @Override
    public Notice build(String field, String message, Object... args) {
        this.field = field;
        this.message = message;
        this.args = args;
        return this;
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public Object[] args() {
        return args;
    }

    @Override
    public String toString() {
        return "DefaultNotice [field=" + field + ", message=" + message + ", args=" + Arrays.toString(args) + "]";
    }
}
