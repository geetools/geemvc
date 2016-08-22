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

public interface View extends Map<String, Object> {
    View forward(String to);

    String forward();

    View redirect(String to);

    String redirect();

    View bind(String name, Object value);

    View bind(Map<String, Object> bindings);

    View flash(String name, Object value);

    View flash(Map<String, Object> flashBindings);

    Map<String, Object> flashMap();

    View stream(String contentType, InputStream inputStream);

    View stream(String contentType, Reader reader);

    View stream(String contentType, String output);

    View stream(String contentType, Object result);

    String contentType();

    InputStream stream();

    Reader reader();

    String output();

    View filename(String filename);

    String filename();

    Object result();

    void result(Object result);

    View characterEncoding(String characterEncoding);

    String characterEncoding();

    View lastModified(long lastModified);

    long lastModified();

    View length(long length);

    long length();

    View attachment(boolean attachment);

    boolean attachment();

    View rangeSupport(boolean rangeSupport);

    boolean rangeSupport();
}
