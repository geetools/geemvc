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

package com.geemvc.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.geemvc.TestEnum;
import com.geemvc.mock.bean.Person;
import org.junit.Test;

import com.geemvc.test.BaseTest;

public class ReflectionProviderTest extends BaseTest {
    private String field1 = null;
    private List<String> field2 = null;
    private Set<Person> field3 = null;
    private Person field4 = null;
    private Map<String, Person> field5 = null;
    private Map<String, List<Person>> field6 = null;
    private List<Map<String, Person>> field7 = null;
    private Locale field8 = null;
    private Date field9 = null;
    private BigDecimal field10 = null;
    private Long[] field11 = null;
    private Person[] field12 = null;

    @Test
    public void testPrimaryType() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        assertEquals(String.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field1").getType(), rp.getField(ReflectionProviderTest.class, "field1").getGenericType()));
        assertEquals(String.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field2").getType(), rp.getField(ReflectionProviderTest.class, "field2").getGenericType()));
        assertEquals(Person.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field3").getType(), rp.getField(ReflectionProviderTest.class, "field3").getGenericType()));
        assertEquals(Person.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field4").getType(), rp.getField(ReflectionProviderTest.class, "field4").getGenericType()));
        assertEquals(Person.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field5").getType(), rp.getField(ReflectionProviderTest.class, "field5").getGenericType()));
        assertEquals(Person.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field6").getType(), rp.getField(ReflectionProviderTest.class, "field6").getGenericType()));
        assertEquals(Person.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field7").getType(), rp.getField(ReflectionProviderTest.class, "field7").getGenericType()));
        assertEquals(Locale.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field8").getType(), rp.getField(ReflectionProviderTest.class, "field8").getGenericType()));
        assertEquals(Date.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field9").getType(), rp.getField(ReflectionProviderTest.class, "field9").getGenericType()));
        assertEquals(BigDecimal.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field10").getType(), rp.getField(ReflectionProviderTest.class, "field10").getGenericType()));
        assertEquals(Long.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field11").getType(), rp.getField(ReflectionProviderTest.class, "field11").getGenericType()));
        assertEquals(Person.class, rp.getPrimaryType(rp.getField(ReflectionProviderTest.class, "field12").getType(), rp.getField(ReflectionProviderTest.class, "field12").getGenericType()));
    }

    @Test
    public void testSimpleType() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        assertTrue(rp.isSimpleType(int.class));
        assertTrue(rp.isSimpleType(int[].class));
        assertTrue(rp.isSimpleType(String.class));
        assertTrue(rp.isSimpleType(BigDecimal.class));
        assertTrue(rp.isSimpleType(Long.class));
        assertTrue(rp.isSimpleType(boolean.class));
        assertTrue(rp.isSimpleType(Date.class));
        assertTrue(rp.isSimpleType(Locale.class));
        assertTrue(rp.isSimpleType(URL.class));
        assertTrue(rp.isSimpleType(URI.class));
        assertTrue(rp.isSimpleType(char.class));
        assertTrue(rp.isSimpleType(char[].class));
        assertTrue(rp.isSimpleType(byte[].class));
        assertTrue(rp.isSimpleType(TestEnum.class));
        assertTrue(!rp.isSimpleType(Person.class));
        assertTrue(!rp.isSimpleType(Person[].class));
    }
}
