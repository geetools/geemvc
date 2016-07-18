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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cb.geemvc.annotation.Controller;
import com.cb.geemvc.annotation.Request;
import com.cb.geemvc.bind.param.annotation.Cookie;
import com.cb.geemvc.bind.param.annotation.Header;
import com.cb.geemvc.bind.param.annotation.Param;
import com.cb.geemvc.bind.param.annotation.PathParam;
import com.cb.geemvc.mock.Id;
import com.cb.geemvc.mock.type.FromStringType;
import com.cb.geemvc.mock.type.StringConstructorType;
import com.cb.geemvc.mock.type.ValueOfType;

@Controller
@Request("/controller17")
public class TestController17 {
    @Request(path = "handler17a/{id}")
    public void handler17a(@PathParam("id") Long id) {

    }

    @Request(path = "handler17b")
    public void handler17b(@Param("id") Long id) {

    }

    @Request(path = "handler17c")
    public void handler17c(@Header("id") Long id) {

    }

    @Request(path = "handler17d")
    public void handler17d(@Cookie("id") Long id) {

    }

    @Request(path = "handler17e/{id}", produces = "application/json")
    public void handler17e(@PathParam("id") int id, @Param("price") BigDecimal price, @Header("Accept") String accept, @Cookie("rememberMe") boolean rememberMe) {

    }

    @Request(path = "handler17f")
    public void handler17f(@Param("ids") int[] ids, @Header("Accept") Set<String> accepts, @Param("tags") String[] tags, @Param("tags2") Collection<String> tags2, @Param("prices") BigDecimal[] prices, @Param("special-prices") List<Double> specialPrices) {

    }

    @Request(path = "handler17g/{param1}")
    public void handler17g(@PathParam("param1") ValueOfType p1, @Param("param2") FromStringType p2, @Cookie("param3") StringConstructorType p3) {

    }

    @Request(path = "handler17h")
    public void handler17h(@Param("myMap") Map<String, String> map) {

    }

    @Request(path = "handler17i")
    public void handler17i(@Param("myMap") Map<String, String[]> map) {

    }

    @Request(path = "handler17j")
    public void handler17j(@Param("myMap") Map<String, List<String>> map) {

    }

    @Request(path = "handler17k")
    public void handler17k(@Param("myMap") Map<String, List<Integer>> map) {

    }

    @Request(path = "handler17l")
    public void handler17l(@Param("myMap") Map<Id, String> map) {

    }

    @Request(path = "handler17m")
    public void handler17m(@Param("myMap") Map<Id, Double[]> map) {

    }

    @Request(path = "handler17n")
    public void handler17n(@Param("myMap1") Map<Long, Id[]> map, @Param("myMap2") Map<Long, List<Id>> map2) {

    }

    @Request(path = "handler17o")
    public void handler17o(@Param("myListOfMaps") List<Map<String, String>> listOfMaps) {

    }

    @Request(path = "handler17p")
    public void handler17p(@Param("myListOfMaps") List<Map<Id, Integer>> listOfMaps) {

    }
}
