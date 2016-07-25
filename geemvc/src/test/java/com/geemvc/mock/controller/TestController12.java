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
@Request("/controller12")
public class TestController12 {
    @Request(handles = "js: 1 == 0 || ( /handler[a]+/igm.test( Java.from( req.cookies ).filter( function(c) c.name == 'cookieOne' )[0].value ) )")
    public void handler12a() {

    }

    @Request(handles = "groovy: 1 == 0 || ( req.cookies.findAll({ c -> c.name == 'cookieOne' })[0].value ==~ /(?im)handler[b]+/ )")
    public void handler12b() {

    }

    @Request(handles = "mvel: 1 == 0 || (($ in Arrays.asList(req.cookies) if $.name == 'cookieOne')[0].value ~= '(?im)handler[c]+')")
    public void handler12c() {

    }

    @Request(handles = "js: 1 == 0 || (/handler[d]+/igm.test(req.parameterMap['paramOne'][0]))")
    public void handler12d() {

    }

    @Request(handles = "groovy: 1 == 0 || (req.parameterMap['paramOne'][0] ==~ /(?im)handler[e]+/)")
    public void handler12e() {

    }

    @Request(handles = "mvel: 1 == 0 || (req.parameterMap['paramOne'][0] ~= '(?im)handler[f]+')")
    public void handler12f() {

    }

    @Request(handles = "js: 1 == 0 || (/handler[g]+/igm.test(req.getHeader('headerOne')))")
    public void handler12g() {

    }

    @Request(handles = "groovy: 1 == 0 || (req.getHeader('headerOne') ==~ /(?im)handler[h]+/)")
    public void handler12h() {

    }

    @Request(handles = "mvel: 1 == 0 || (req.getHeader('headerOne') ~= '(?im)handler[i]+')")
    public void handler12i() {

    }

}
