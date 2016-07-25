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

import javax.ws.rs.core.MediaType;

import com.geemvc.HttpMethod;
import com.geemvc.annotation.Controller;
import com.geemvc.annotation.Request;

@Controller
@Request("/controller16")
public class TestController16 {
    @Request(path = "{id}", consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void handler16a() {

    }

    @Request(path = "{id}", method = HttpMethod.POST, consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void handler16b() {

    }

    @Request(path = "handler16", consumes = {MediaType.APPLICATION_XML}, produces = {MediaType.APPLICATION_JSON})
    public void handler16c() {

    }

    @Request(path = "handler16", consumes = {MediaType.APPLICATION_JSON}, produces = {MediaType.APPLICATION_XML})
    public void handler16d() {

    }

    @Request(path = "handler16", consumes = {MediaType.APPLICATION_JSON}, produces = {MediaType.TEXT_HTML})
    public void handler16e() {

    }
}
