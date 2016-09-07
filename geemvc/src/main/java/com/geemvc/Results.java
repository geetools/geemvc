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

import java.io.InputStream;
import java.io.Reader;

import com.geemvc.inject.Injectors;
import com.geemvc.view.bean.Result;

public class Results {
    public static Result from(String value) {
        return Injectors.provide().getInstance(Result.class).from(value);
    }
    
    public static Result view(String path) {
        return Injectors.provide().getInstance(Result.class).view(path);
    }

    public static Result redirect(String to) {
        return Injectors.provide().getInstance(Result.class).redirect(to);
    }

    public static Result handler(String path) {
        return Injectors.provide().getInstance(Result.class).handler(path);
    }

    public static Result handler(String path, String httpMethod) {
        return Injectors.provide().getInstance(Result.class).handler(path, httpMethod);
    }

    public static Result handler(Class<?> controllerClass, String handlerMethod) {
        return Injectors.provide().getInstance(Result.class).handler(controllerClass, handlerMethod);
    }

    public static Result uniqueHandler(String name) {
        return Injectors.provide().getInstance(Result.class).uniqueHandler(name);
    }

    public static Result stream(String contentType, InputStream inputStream) {
        return Injectors.provide().getInstance(Result.class).stream(contentType, inputStream);
    }

    public static Result stream(String contentType, Reader reader) {
        return Injectors.provide().getInstance(Result.class).stream(contentType, reader);
    }

    public static Result stream(String contentType, String output) {
        return Injectors.provide().getInstance(Result.class).stream(contentType, output);
    }

    public static Result stream(String contentType, Object result) {
        return Injectors.provide().getInstance(Result.class).stream(contentType, result);
    }

    public static Result status(Integer status) {
        return Injectors.provide().getInstance(Result.class).status(status);
    }

    public static Result status(Integer status, String message) {
        return Injectors.provide().getInstance(Result.class).status(status, message);
    }
}
