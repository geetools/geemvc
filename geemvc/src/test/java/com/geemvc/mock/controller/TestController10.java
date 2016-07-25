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
@Request("/controller10")
public class TestController10 {
    @Request(headers = "Accept=application/json")
    public void handler10a() {

    }

    @Request(headers = "Accept=application/xml")
    public void handler10b() {

    }

    @Request(path = "/handler", headers = {"Accept=application/json", "version=^\\d+"})
    public void handler10c() {

    }

    @Request(path = "/handler", headers = {"Accept=application/json", "Accept=application/xml", "Accept=^text\\/.*", "Accept=image/*"})
    public void handler10d() {

    }

    @Request(path = "/handler", headers = {"orHeader=^(?iu:ONE|TWO|THREE|FOUR)$"})
    public void handler10e() {

    }

    @Request(headers = "headerExists")
    public void handler10f() {

    }

    @Request(headers = {"headerOneExists", "headerTwoExists"}, method = HttpMethod.PUT)
    public void handler10g() {

    }

    @Request(headers = {"headerOneExists", "headerTwoExists"}, method = HttpMethod.GET)
    public void handler10gg() {

    }

    @Request(headers = "booleanHeader!=true")
    public void handler10h() {

    }

    @Request(headers = {"booleanHeaderOne=true", "booleanHeaderTwo!=true"})
    public void handler10i() {

    }

    @Request(headers = {"booleanHeaderOne=true"})
    public void handler10ii() {

    }

    @Request(headers = {"jHeaderOne != ^notTr[ue]+", "jHeaderTwo!=tru*"})
    public void handler10j() {

    }

    @Request(headers = {"kHeaderOne != ^notTr[ue]+$", "kHeaderOne!=tru*"})
    public void handler10k() {

    }

    @Request(headers = {"js: headerOne > 100", "groovy: (headerTwo as int) > 200", "mvel: headerThree > 300"})
    public void handler10l() {

    }

    @Request(headers = {"js: headerOne == 'one'", "groovy: headerTwo == 'two'", "mvel: headerThree == 'three'"})
    public void handler10m() {

    }

    @Request(headers = {"js: headerOne != 'one' && headerOne != 101 && headerOne != 11", "groovy: headerTwo != 'two' && headerTwo != '202' && headerTwo != '22'", "mvel: headerThree != 'three' && headerThree != 303 && headerThree != 33"})
    public void handler10n() {

    }

    @Request(headers = {"js: headerOne <= 100 && headerOne == headerTwo", "groovy: (headerTwo as int) <= 100 && headerTwo == headerThree", "mvel: headerThree <= 100 && headerThree == headerOne"})
    public void handler10o() {

    }

    @Request(headers = {"js: headerOne == 100 || headerOne == 11", "groovy: (headerTwo as int) == 200 || (headerTwo as int) == 22", "mvel: headerThree == 300 || headerThree == 33"})
    public void handler10p() {

    }

    @Request(headers = {"js: 1 == 0 || (/handler[q]+/igm.test(headerOne))", "groovy: 1 == 0 || (headerOne ==~ /(?im)handler[q]+/)", "mvel: 1 == 0 || (headerOne ~= '(?im)handler[q]+')"})
    public void handler10q() {

    }
}
