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
@Request("/controller9")
public class TestController9 {
    @Request(params = "cmd=update")
    public void handler9a() {

    }

    @Request(params = "cmd=delete")
    public void handler9b() {

    }

    @Request(path = "/handler", params = {"cmd=delete", "id=^\\d+"})
    public void handler9c() {

    }

    @Request(path = "/handler", params = {"param=one", "param=two", "param=^thr.+", "param=fou*"})
    public void handler9d() {

    }

    @Request(path = "/handler", params = {"orParam=^(?iu:ONE|TWO|THREE|FOUR)$"})
    public void handler9e() {

    }

    @Request(params = "paramExists")
    public void handler9f() {

    }

    @Request(params = {"paramOneExists", "paramTwoExists"}, method = HttpMethod.PUT)
    public void handler9g() {

    }

    @Request(params = {"paramOneExists", "paramTwoExists"}, method = HttpMethod.GET)
    public void handler9gg() {

    }

    @Request(params = "booleanParam!=true")
    public void handler9h() {

    }

    @Request(params = {"booleanParamOne=true", "booleanParamTwo!=true"})
    public void handler9i() {

    }

    @Request(params = {"booleanParamOne=true"})
    public void handler9ii() {

    }

    @Request(params = {"jParamOne != ^notTr[ue]+", "jParamTwo!=tru*"})
    public void handler9j() {

    }

    @Request(params = {"kParamOne != ^notTr[ue]+$", "kParamOne!=tru*"})
    public void handler9k() {

    }

    @Request(params = {"js: paramOne > 100", "groovy: (paramTwo as int) > 200", "mvel: paramThree > 300"})
    public void handler9l() {

    }

    @Request(params = {"js: paramOne == 'one'", "groovy: paramTwo == 'two'", "mvel: paramThree == 'three'"})
    public void handler9m() {

    }

    @Request(params = {"js: paramOne != 'one' && paramOne != 101 && paramOne != 11", "groovy: paramTwo != 'two' && paramTwo != '202' && paramTwo != '22'", "mvel: paramThree != 'three' && paramThree != 303 && paramThree != 33"})
    public void handler9n() {

    }

    @Request(params = {"js: paramOne <= 100 && paramOne == paramTwo", "groovy: (paramTwo as int) <= 100 && paramTwo == paramThree", "mvel: paramThree <= 100 && paramThree == paramOne"})
    public void handler9o() {

    }

    @Request(params = {"js: paramOne == 100 || paramOne == 11", "groovy: (paramTwo as int) == 200 || (paramTwo as int) == 22", "mvel: paramThree == 300 || paramThree == 33"})
    public void handler9p() {

    }

    @Request(params = {"js: 1 == 0 || (/handler[q]+/igm.test(paramOne))", "groovy: 1 == 0 || (paramOne ==~ /(?im)handler[q]+/)", "mvel: 1 == 0 || (paramOne ~= '(?im)handler[q]+')"})
    public void handler9q() {

    }
}
