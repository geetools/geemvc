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

import com.cb.geemvc.annotation.Controller;
import com.cb.geemvc.annotation.Request;

@Controller
@Request(ignore = {"/catch/all", "/shared/path", "^/controller[^6].*"})
public class TestController6 {
    @Request(path = "**", priority = 4)
    public void handler6a() {

    }
}
