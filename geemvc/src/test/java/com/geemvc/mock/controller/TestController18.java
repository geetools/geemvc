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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.geemvc.Bindings;
import com.geemvc.annotation.Controller;
import com.geemvc.annotation.Request;
import com.geemvc.bind.param.annotation.Data;
import com.geemvc.bind.param.annotation.Session;
import com.geemvc.mock.bean.Person;
import com.geemvc.validation.annotation.Check;

@Check(param = "person.addresses[0].zip", required = true, on = {"POST /createPerson"})
@Check(param = "person.addresses[0].streetLines", required = true, on = {"POST createPersonEvent"})
@Check(param = "person.addresses[0].city", required = true, on = {"POST createPerson", "createPersons"})
@Controller
@Request("/controller18")
public class TestController18 {
    @Check(param = {"person.forename", "person.surname", "person.age"}, required = true)
    @Check(param = "person.age", required = true, min = 18, max = 120)
    @Check(is = "js: person.age >= 10", message = "error.validation.minage")
    @Request(path = "createPerson", name = "createPersonEvent", onError = "redirect: /errorPage")
    public void createPerson(@Valid Person person, Bindings bindings) {
    }

    @Request("createPersons")
    public void createPersons(Collection<Person> persons) {

    }

    @Request(path = "updatePersons", name = "updatePersonsEvent")
    public void updatePersons(Map<String, Person> persons) {

    }

    @Request("updatePersons2")
    public void updatePersons2(Map<Long, Person> persons) {

    }

    @Request("updatePersons3")
    public void updatePersons3(Map<String, List<Person>> persons) {

    }

    @Request("updatePersons4")
    public void updatePersons4(Map<String, Person[]> persons) {

    }

    @Request("updatePersons5")
    public void updatePersons5(Person[] persons) {

    }

    @Request("updatePersons6")
    public void updatePersons6(List<Map<String, Person>> persons) {

    }

    @Request("personInSession")
    public void sessionPerson(@Session Person person) {

    }

    @Request("personFromDB/{id}")
    public void dbPerson(@Data("id") Person person) {

    }
}
