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

package com.geemvc.mock.validator;

import com.geemvc.handler.RequestHandler;
import com.geemvc.mock.bean.Person;
import com.geemvc.validation.Errors;
import com.geemvc.validation.ValidationContext;
import com.geemvc.validation.Validator;
import com.geemvc.validation.annotation.CheckBean;

@CheckBean(type = Person.class, on = "POST /createPerson")
public class PersonValidator implements Validator {

    @Override
    public Object validate(RequestHandler requestHandler, ValidationContext validationCtx, Errors e) {
        Person p = (Person) validationCtx.typedValues().get("person");

        if (p.getAge() < 20)
            e.add("person.age", "error.validation.minage", p.getAge(), 20);

        return "forward: form.ftl";
    }
}
