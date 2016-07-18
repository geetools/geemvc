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

package com.cb.geemvc.mock.controller;

import com.cb.geemvc.HttpMethod;
import com.cb.geemvc.annotation.Controller;
import com.cb.geemvc.annotation.Request;

@Controller
@Request("/controller8")
public class TestController8 {
    @Request("/handler/get-and-post")
    public void handler8a() {

    }

    @Request(path = "/handler", method = HttpMethod.GET)
    public void handler8b() {

    }

    @Request(path = "/handler", method = HttpMethod.POST)
    public void handler8c() {

    }

    @Request(path = "/handler", method = HttpMethod.PUT)
    public void handler8d() {

    }

    @Request(path = "/handler", method = HttpMethod.DELETE)
    public void handler8e() {

    }

    @Request(path = "/handler", method = HttpMethod.OPTIONS)
    public void handler8f() {

    }

    @Request(path = "/handler", method = HttpMethod.HEAD)
    public void handler8g() {

    }

    @Request(path = "/handler", method = HttpMethod.TRACE)
    public void handler8h() {

    }
}
