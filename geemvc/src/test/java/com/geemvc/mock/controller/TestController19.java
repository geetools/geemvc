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

import com.geemvc.Results;
import com.geemvc.annotation.Controller;
import com.geemvc.annotation.Request;
import com.geemvc.mock.Id;
import com.geemvc.mock.bean.RootBean;
import com.geemvc.view.bean.Result;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Controller
@Request("/controller19")
public class TestController19 {
    @Inject
    protected Injector injector;

    @Request("handler19a")
    public Result handler19a() {
        return Results.view("forward/to");
    }

    @Request("handler19b")
    public Result handler19b() {
        return Results.redirect("redirect/to");
    }

    @Request("handler19c")
    public Result handler19c() {
        return Results.view("forward/to").bind("var1", "value1").bind("var2", Id.valueOf("2")).bind("var3", injector.getInstance(RootBean.class));
    }
}
