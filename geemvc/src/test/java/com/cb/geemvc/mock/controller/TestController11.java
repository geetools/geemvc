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
@Request("/controller11")
public class TestController11 {
    @Request(cookies = "group=one")
    public void handler11a() {

    }

    @Request(cookies = "group=two")
    public void handler11b() {

    }

    @Request(path = "/handler", cookies = {"group=one", "rememberMe=^(0|1)"})
    public void handler11c() {

    }

    @Request(path = "/handler", cookies = {"rememberMe=^(?iu:0|1)$"})
    public void handler11e() {

    }

    @Request(cookies = "cookieExists")
    public void handler11f() {

    }

    @Request(cookies = {"cookieOneExists", "cookieTwoExists"}, method = HttpMethod.PUT)
    public void handler11g() {

    }

    @Request(cookies = {"cookieOneExists", "cookieTwoExists"}, method = HttpMethod.GET)
    public void handler11gg() {

    }

    @Request(cookies = "booleanCookie!=true")
    public void handler11h() {

    }

    @Request(cookies = {"booleanCookieOne=true", "booleanCookieTwo!=true"})
    public void handler11i() {

    }

    @Request(cookies = {"booleanCookieOne=true"})
    public void handler11ii() {

    }

    @Request(cookies = {"jCookieOne != ^notTr[ue]+", "jCookieTwo!=tru*"})
    public void handler11j() {

    }

    @Request(cookies = {"kCookieOne != ^notTr[ue]+$", "kCookieOne!=tru*"})
    public void handler11k() {

    }

    @Request(cookies = {"js: cookieOne > 100", "groovy: (cookieTwo as int) > 200", "mvel: cookieThree > 300"})
    public void handler11l() {

    }

    @Request(cookies = {"js: cookieOne == 'one'", "groovy: cookieTwo == 'two'", "mvel: cookieThree == 'three'"})
    public void handler11m() {

    }

    @Request(cookies = {"js: cookieOne != 'one' && cookieOne != 101 && cookieOne != 11", "groovy: cookieTwo != 'two' && cookieTwo != '202' && cookieTwo != '22'", "mvel: cookieThree != 'three' && cookieThree != 303 && cookieThree != 33"})
    public void handler11n() {

    }

    @Request(cookies = {"js: cookieOne <= 100 && cookieOne == cookieTwo", "groovy: (cookieTwo as int) <= 100 && cookieTwo == cookieThree", "mvel: cookieThree <= 100 && cookieThree == cookieOne"})
    public void handler11o() {

    }

    @Request(cookies = {"js: cookieOne == 100 || cookieOne == 11", "groovy: (cookieTwo as int) == 200 || (cookieTwo as int) == 22", "mvel: cookieThree == 300 || cookieThree == 33"})
    public void handler11p() {

    }

    @Request(cookies = {"js: 1 == 0 || (/handler[q]+/igm.test(cookieOne))", "groovy: 1 == 0 || (cookieOne ==~ /(?im)handler[q]+/)", "mvel: 1 == 0 || (cookieOne ~= '(?im)handler[q]+')"})
    public void handler11q() {

    }

    @Request(cookies = {"js: 1 == 0 || ( /handler[r]+/igm.test( Java.from( req.cookies ).filter( function(c) c.name == 'cookieOne' )[0].value ) )",
            "groovy: 1 == 0 || ( req.cookies.findAll({ c -> c.name == 'cookieOne' })[0].value ==~ /(?im)handler[r]+/ )", "mvel: 1 == 0 || (($ in Arrays.asList(req.cookies) if $.name == 'cookieOne')[0].value ~= '(?im)handler[r]+')"})
    public void handler11r() {

    }
}
