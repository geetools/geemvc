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

import com.geemvc.HttpMethod;
import com.geemvc.annotation.Controller;
import com.geemvc.annotation.Request;

@Controller
@Request("/controller13")
public class TestController13 {
    @Request(path = "/handler13", params = "cmd=update", headers = "Accept=application/json", cookies = "group=one", method = HttpMethod.GET)
    public void handler13a() {

    }

    @Request(path = "/handler13", params = "cmd=update", headers = "Accept=application/xml", cookies = "group=one", method = HttpMethod.GET)
    public void handler13b() {

    }

    @Request(path = "/handler13", params = "cmd=update", headers = "Accept=application/json", cookies = "group=two", method = HttpMethod.GET)
    public void handler13c() {

    }

    @Request(path = "/handler13", params = "cmd=delete", headers = "Accept=application/json", cookies = "group=one", method = HttpMethod.GET)
    public void handler13d() {

    }

    @Request(path = "/handler13", params = "cmd=update", headers = "Accept=application/json", cookies = "group=one", method = HttpMethod.PUT)
    public void handler13e() {

    }

    @Request(path = "/handler13", headers = "Accept=application/json", cookies = "group=one", method = HttpMethod.GET)
    public void handler13f() {

    }

    @Request(path = "/handler13", headers = "Accept=application/json", method = HttpMethod.GET)
    public void handler13g() {

    }

    @Request(path = "/handler13", method = HttpMethod.GET)
    public void handler13h() {

    }

    @Request(path = "/handler13", method = HttpMethod.POST)
    public void handler13i() {

    }

    @Request(path = "/")
    public void handler13j() {

    }

    @Request(path = "/handler13/specific/path")
    public void handler13kA() {

    }

    @Request(path = "^/handler13/.+")
    public void handler13kB() {

    }

    @Request(path = "/handler13/same/path", headers = "Accept=application/json")
    public void handler13lA() {

    }

    @Request(path = "/handler13/same/path", headers = "Accept=application/json", params = "cmd=update")
    public void handler13lB() {

    }

    @Request(path = "/handler13/same/path2", params = "cmd=^\\w+")
    public void handler13mA() {

    }

    @Request(path = "/handler13/same/path2", params = "cmd=update")
    public void handler13mB() {

    }

    @Request(path = "/handler13/same/path3", params = "cmd=/^test\\d+/", priority = 1)
    public void handler13nA() {

    }

    @Request(path = "/handler13/same/path3", params = "cmd=/^test[0-9]+/", priority = 2)
    public void handler13nB() {

    }
}
