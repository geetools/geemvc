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
@Request("/controller15")
public class TestController15 {
    @Request("/handler15")
    public void handler15a() {

    }

    @Request("/handler15/{param1}")
    public void handler15b() {

    }

    @Request("/handler15/{param1}/{param2}")
    public void handler15c() {

    }

    @Request("/handler15/{param1}-{param2}")
    public void handler15cc() {

    }

    @Request("/handler15/id/{param3:^\\d+}")
    public void handler15d() {

    }

    @Request("/handler15/id/{param3:^\\d+}/testme")
    public void handler15e() {

    }

    @Request("/handler15/id/{param3:^\\d+}/{param4}/testme")
    public void handler15f() {

    }

    @Request("{id}")
    public void handler15g() {

    }

    @Request(path = "{id}", method = HttpMethod.POST)
    public void handler15h() {

    }

    @Request(path = "{id}", method = HttpMethod.POST, headers = "Accept=application/json")
    public void handler15i() {

    }

    @Request(path = "{id}", method = HttpMethod.POST, headers = "Accept  =  application/xml")
    public void handler15j() {

    }

    @Request(path = "{id}", method = HttpMethod.POST, params = "param1=val1", headers = "Accept=application/xml")
    public void handler15k() {

    }

    @Request(path = "{id}", method = HttpMethod.POST, params = "param1=val1")
    public void handler15l() {

    }

    @Request("/handler15/test-regex/{regex-param:\\d+}")
    public void handler15m() {

    }
}
