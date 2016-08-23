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
import com.geemvc.view.bean.View;

public class Views {
    public static View forward(String to) {
        return Injectors.provide().getInstance(View.class).forward(to);
    }

    public static View redirect(String to) {
        return Injectors.provide().getInstance(View.class).redirect(to);
    }

    public static View stream(String contentType, InputStream inputStream) {
        return Injectors.provide().getInstance(View.class).stream(contentType, inputStream);
    }

    public static View stream(String contentType, Reader reader) {
        return Injectors.provide().getInstance(View.class).stream(contentType, reader);
    }

    public static View stream(String contentType, String output) {
        return Injectors.provide().getInstance(View.class).stream(contentType, output);
    }

    public static View stream(String contentType, Object result) {
        return Injectors.provide().getInstance(View.class).stream(contentType, result);
    }

    public static View status(Integer status) {
        return Injectors.provide().getInstance(View.class).status(status);
    }

    public static View status(Integer status, String message) {
        return Injectors.provide().getInstance(View.class).status(status, message);
    }
}
