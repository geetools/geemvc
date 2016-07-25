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

package com.geemvc.mock.controller;

import com.geemvc.annotation.Controller;
import com.geemvc.annotation.Request;

@Controller
@Request("/controller4")
public class TestController4 {
    @Request(path = "^/handler1/[0-9]+", priority = 2)
    public void handler4a() {

    }

    @Request(path = "^/handler1/[0-9]{1}", priority = 1)
    public void handler4b() {

    }

    @Request("^/api/v[0-9]+/handler1")
    public void handler4c() {

    }

    @Request("^/api/v[0-9]+/handler1/[a-z]+/[\\d]+")
    public void handler4d() {

    }

    @Request("^/api/v[0-9]+/handler1/[a-z]+/[\\d]+/xyz")
    public void handler4e() {

    }

    @Request("^/api/v[0-9]+/handler1/[a-z]+/[\\d]+/xy$")
    public void handler4f() {

    }

    @Request("^/api/v[0-9]+/handler2/.+")
    public void handler4g() {

    }

    @Request("^.*")
    public void handler4h() {

    }

}
