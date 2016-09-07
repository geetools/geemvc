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

package com.geemvc.view.bean;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public interface Result {
    Result view(String path);

    String view();

    Result redirect(String to);

    String redirect();

    Result handler(String path);

    Result handler(String path, String httpMethod);

    String handlerPath();

    String httpMethod();

    Result handler(Class<?> controllerClass, String handlerMethod);

    Class<?> controllerClass();

    String handlerMethod();

    Result uniqueHandler(String name);

    String uniqueHandler();

    Result bind(String name, Object value);

    Result bind(Map<String, Object> bindings);

    Object binding(String name);

    boolean containsBinding(String name);

    boolean hasBindings();

    Map<String, Object> bindings();

    Result flash(String name, Object value);

    Result flash(Map<String, Object> flashBindings);

    Map<String, Object> flashMap();

    Result param(String name, Object value);

    Result param(Map<String, Object> parameters);

    Object param(String name);

    boolean containsParam(String name);

    boolean hasParameters();

    Map<String, Object> parameters();

    Result stream(String contentType, InputStream inputStream);

    Result stream(String contentType, Reader reader);

    Result stream(String contentType, String output);

    Result stream(String contentType, Object result);

    String contentType();

    InputStream stream();

    Reader reader();

    String output();

    Result filename(String filename);

    String filename();

    Object result();

    void result(Object result);

    Result characterEncoding(String characterEncoding);

    String characterEncoding();

    Result lastModified(long lastModified);

    long lastModified();

    Result length(long length);

    long length();

    Result attachment(boolean attachment);

    boolean attachment();

    Result rangeSupport(boolean rangeSupport);

    boolean rangeSupport();

    Integer status();

    String message();

    Result status(Integer status);

    Result status(Integer status, String message);
}
