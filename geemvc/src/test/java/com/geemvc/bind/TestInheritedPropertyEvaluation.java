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

package com.geemvc.bind;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import com.geemvc.mock.bean.NestedBean;
import com.geemvc.mock.converter.IdConverter;
import jodd.typeconverter.TypeConverterManager;

import org.junit.Test;

import com.geemvc.mock.Id;
import com.geemvc.mock.bean.InheritedRootBean;
import com.geemvc.test.BaseTest;

public class TestInheritedPropertyEvaluation extends BaseTest {
    @Test
    public void testSettingNestedString() {
        InheritedRootBean rootBean = instance(InheritedRootBean.class);

        assertNotNull(rootBean);

        PropertyNode node = instance(PropertyNode.class).build("nestedBean.nestedBean.code", rootBean.getClass());

        assertNotNull(node);

        node.set(rootBean, "my_code");

        assertNotNull(rootBean.getNestedBean());
        assertNotNull(rootBean.getNestedBean().getNestedBean());
        assertNotNull(rootBean.getNestedBean().getNestedBean().getCode());
        assertEquals("my_code", rootBean.getNestedBean().getNestedBean().getCode());
    }

    @Test
    public void testSettingNestedCustomObject() {
        TypeConverterManager.get().register(Id.class, new IdConverter());

        InheritedRootBean rootBean = instance(InheritedRootBean.class);

        assertNotNull(rootBean);

        PropertyNode node = instance(PropertyNode.class).build("nestedBean.nestedBean.id", rootBean.getClass());

        assertNotNull(node);

        node.set(rootBean, "12345");

        assertNotNull(rootBean.getNestedBean());
        assertNotNull(rootBean.getNestedBean().getNestedBean());
        assertNotNull(rootBean.getNestedBean().getNestedBean().getId());
        assertEquals(Id.valueOf("12345"), rootBean.getNestedBean().getNestedBean().getId());
    }

    @Test
    public void testSettingNestedPrimitive() {
        InheritedRootBean rootBean = instance(InheritedRootBean.class);

        assertNotNull(rootBean);

        PropertyNode node = instance(PropertyNode.class).build("nestedBean.nestedBean.count", rootBean.getClass());

        assertNotNull(node);

        node.set(rootBean, 1000);

        assertNotNull(rootBean.getNestedBean());
        assertNotNull(rootBean.getNestedBean().getNestedBean());
        assertEquals(1000, rootBean.getNestedBean().getNestedBean().getCount());
    }

    @Test
    public void testSettingNestedStringCollection() {
        InheritedRootBean rootBean = instance(InheritedRootBean.class);

        assertNotNull(rootBean);

        PropertyNode node = instance(PropertyNode.class).build("nestedBean.nestedBean.tags", rootBean.getClass());

        assertNotNull(node);

        node.set(rootBean, "one", "nestedBean.nestedBean.tags[]");
        node.set(rootBean, "two", "nestedBean.nestedBean.tags[]");
        node.set(rootBean, "three", "nestedBean.nestedBean.tags[]");
        node.set(rootBean, "four", "nestedBean.nestedBean.tags[]");

        assertNotNull(rootBean.getNestedBean());
        assertNotNull(rootBean.getNestedBean().getNestedBean());
        assertNotNull(rootBean.getNestedBean().getNestedBean().getTags());
        assertEquals(4, rootBean.getNestedBean().getNestedBean().getTags().size());
        assertEquals(new ArrayList<>(Arrays.asList(new String[]{"one", "two", "three", "four"})), rootBean.getNestedBean().getNestedBean().getTags());
    }

    @Test
    public void testSettingNestedMultipleCollections1() {
        InheritedRootBean rootBean = instance(InheritedRootBean.class);

        assertNotNull(rootBean);

        PropertyNode node = instance(PropertyNode.class).build("nestedBeans.nestedBean.tags", rootBean.getClass());

        assertNotNull(node);

        node.set(rootBean, "oneX", "nestedBeans[0].nestedBean.tags[]");
        node.set(rootBean, "twoX", "nestedBeans[0].nestedBean.tags[]");
        node.set(rootBean, "threeX", "nestedBeans[1].nestedBean.tags[]");
        node.set(rootBean, "fourX", "nestedBeans[1].nestedBean.tags[]");

        assertNotNull(rootBean.getNestedBeans());
        assertEquals(2, rootBean.getNestedBeans().size());

        assertNotNull(rootBean.getNestedBeans().get(0).getNestedBean());
        assertNotNull(rootBean.getNestedBeans().get(0).getNestedBean().getTags());
        assertEquals(2, rootBean.getNestedBeans().get(0).getNestedBean().getTags().size());
        assertNotNull(rootBean.getNestedBeans().get(1).getNestedBean());
        assertNotNull(rootBean.getNestedBeans().get(1).getNestedBean().getTags());
        assertEquals(2, rootBean.getNestedBeans().get(1).getNestedBean().getTags().size());

        assertEquals(new ArrayList<>(Arrays.asList(new String[]{"oneX", "twoX"})), rootBean.getNestedBeans().get(0).getNestedBean().getTags());
        assertEquals(new ArrayList<>(Arrays.asList(new String[]{"threeX", "fourX"})), rootBean.getNestedBeans().get(1).getNestedBean().getTags());
    }

    @Test
    public void testSettingNestedMultipleCollections2() {
        TypeConverterManager.get().register(Id.class, new IdConverter());

        InheritedRootBean rootBean = instance(InheritedRootBean.class);

        assertNotNull(rootBean);

        PropertyNode node1 = instance(PropertyNode.class).build("nestedBeans.nestedBeans.id", rootBean.getClass());

        assertNotNull(node1);

        node1.set(rootBean, 12345, "nestedBeans[0].nestedBeans[0].id");
        node1.set(rootBean, 67890, "nestedBeans[1].nestedBeans[0].id");

        PropertyNode node2 = instance(PropertyNode.class).build("nestedBeans.nestedBeans.code", rootBean.getClass());

        assertNotNull(node2);

        node2.set(rootBean, "my_code", "nestedBeans[2].nestedBeans[0].code");

        PropertyNode node3 = instance(PropertyNode.class).build("nestedBeans.nestedBeans.count", rootBean.getClass());

        assertNotNull(node3);

        node3.set(rootBean, 2222, "nestedBeans[3].nestedBeans[0].count");

        assertNotNull(rootBean.getNestedBeans());
        assertEquals(4, rootBean.getNestedBeans().size());
        assertNotNull(rootBean.getNestedBeans().get(0));
        assertNotNull(rootBean.getNestedBeans().get(0).getNestedBeans());
        assertEquals(1, rootBean.getNestedBeans().get(0).getNestedBeans().size());
        assertEquals(Id.valueOf("12345"), rootBean.getNestedBeans().get(0).getNestedBeans().get(0).getId());
        assertNull(rootBean.getNestedBeans().get(0).getNestedBeans().get(0).getCode());
        assertEquals(0, rootBean.getNestedBeans().get(0).getNestedBeans().get(0).getCount());

        assertNotNull(rootBean.getNestedBeans().get(1));
        assertNotNull(rootBean.getNestedBeans().get(1).getNestedBeans());
        assertEquals(1, rootBean.getNestedBeans().get(1).getNestedBeans().size());
        assertEquals(Id.valueOf("67890"), rootBean.getNestedBeans().get(1).getNestedBeans().get(0).getId());
        assertNull(rootBean.getNestedBeans().get(1).getNestedBeans().get(0).getCode());
        assertEquals(0, rootBean.getNestedBeans().get(1).getNestedBeans().get(0).getCount());

        assertNotNull(rootBean.getNestedBeans().get(2));
        assertNotNull(rootBean.getNestedBeans().get(2).getNestedBeans());
        assertEquals(1, rootBean.getNestedBeans().get(2).getNestedBeans().size());
        assertNull(rootBean.getNestedBeans().get(2).getNestedBeans().get(0).getId());
        assertEquals("my_code", rootBean.getNestedBeans().get(2).getNestedBeans().get(0).getCode());
        assertEquals(0, rootBean.getNestedBeans().get(2).getNestedBeans().get(0).getCount());

        assertNotNull(rootBean.getNestedBeans().get(3));
        assertNotNull(rootBean.getNestedBeans().get(3).getNestedBeans());
        assertEquals(1, rootBean.getNestedBeans().get(3).getNestedBeans().size());
        assertNull(rootBean.getNestedBeans().get(3).getNestedBeans().get(0).getId());
        assertNull(rootBean.getNestedBeans().get(3).getNestedBeans().get(0).getCode());
        assertEquals(2222, rootBean.getNestedBeans().get(3).getNestedBeans().get(0).getCount());
    }

    @Test
    public void testSettingNestedMultipleMaps1() {
        InheritedRootBean rootBean = instance(InheritedRootBean.class);

        assertNotNull(rootBean);

        PropertyNode node = instance(PropertyNode.class).build("nestedBeans.mappedNestedBeans.tags", rootBean.getClass());

        assertNotNull(node);

        node.set(rootBean, "one", "nestedBeans[0].mappedNestedBeans[a_tags].tags[]");
        node.set(rootBean, "two", "nestedBeans[0].mappedNestedBeans[a_tags].tags[]");
        node.set(rootBean, "three", "nestedBeans[0].mappedNestedBeans[b_tags].tags[]");
        node.set(rootBean, "four", "nestedBeans[0].mappedNestedBeans[b_tags].tags[]");

        assertNotNull(rootBean.getNestedBeans());
        assertEquals(1, rootBean.getNestedBeans().size());
        assertNotNull(rootBean.getNestedBeans().get(0).getMappedNestedBeans());
        assertEquals(2, rootBean.getNestedBeans().get(0).getMappedNestedBeans().size());
        assertNotNull(rootBean.getNestedBeans().get(0).getMappedNestedBeans().get("a_tags"));
        assertNotNull(rootBean.getNestedBeans().get(0).getMappedNestedBeans().get("b_tags"));
        assertNotNull(rootBean.getNestedBeans().get(0).getMappedNestedBeans().get("a_tags").getTags());
        assertNotNull(rootBean.getNestedBeans().get(0).getMappedNestedBeans().get("b_tags").getTags());
        assertEquals(2, rootBean.getNestedBeans().get(0).getMappedNestedBeans().get("a_tags").getTags().size());
        assertEquals(2, rootBean.getNestedBeans().get(0).getMappedNestedBeans().get("b_tags").getTags().size());
        assertEquals(new ArrayList<>(Arrays.asList(new String[]{"one", "two"})), rootBean.getNestedBeans().get(0).getMappedNestedBeans().get("a_tags").getTags());
        assertEquals(new ArrayList<>(Arrays.asList(new String[]{"three", "four"})), rootBean.getNestedBeans().get(0).getMappedNestedBeans().get("b_tags").getTags());
    }

    @Test
    public void testSettingNestedArray1() {
        InheritedRootBean rootBean = instance(InheritedRootBean.class);

        assertNotNull(rootBean);

        PropertyNode node = instance(PropertyNode.class).build("nestedBeanArray.tagsArray", rootBean.getClass());

        assertNotNull(node);

        node.set(rootBean, "one", "nestedBeanArray[0].tagsArray[]");
        node.set(rootBean, "two", "nestedBeanArray[0].tagsArray[]");
        node.set(rootBean, "three", "nestedBeanArray[0].tagsArray[]");
        node.set(rootBean, "four", "nestedBeanArray[0].tagsArray[]");

        assertNotNull(rootBean.getNestedBeanArray());
        assertEquals(1, rootBean.getNestedBeanArray().length);

        NestedBean[] arr = rootBean.getNestedBeanArray();

        assertNotNull(arr[0]);
        assertNotNull(arr[0].getTagsArray());
        assertEquals(4, arr[0].getTagsArray().length);
        assertArrayEquals(new String[]{"one", "two", "three", "four"}, arr[0].getTagsArray());
    }

    @Test
    public void testSettingNestedArray2() {
        TypeConverterManager.get().register(Id.class, new IdConverter());

        InheritedRootBean rootBean = instance(InheritedRootBean.class);

        assertNotNull(rootBean);

        PropertyNode node = instance(PropertyNode.class).build("idsArray", rootBean.getClass());

        assertNotNull(node);

        node.set(rootBean, "1", "idsArray[]");
        node.set(rootBean, "2", "idsArray[]");
        node.set(rootBean, "3", "idsArray[]");
        node.set(rootBean, "4", "idsArray[]");

        assertNotNull(rootBean.getIdsArray());
        assertEquals(4, rootBean.getIdsArray().length);

        assertArrayEquals(new Id[]{Id.valueOf("1"), Id.valueOf("2"), Id.valueOf("3"), Id.valueOf("4")}, rootBean.getIdsArray());
    }

    @Test
    public void testSettingSimpleValues() {
        TypeConverterManager.get().register(Id.class, new IdConverter());

        InheritedRootBean rootBean = instance(InheritedRootBean.class);

        assertNotNull(rootBean);

        PropertyNode node1 = instance(PropertyNode.class).build("code", rootBean.getClass());
        PropertyNode node2 = instance(PropertyNode.class).build("status", rootBean.getClass());
        PropertyNode node3 = instance(PropertyNode.class).build("count", rootBean.getClass());
        PropertyNode node4 = instance(PropertyNode.class).build("id", rootBean.getClass());

        assertNotNull(node1);
        assertNotNull(node2);
        assertNotNull(node3);
        assertNotNull(node4);

        node1.set(rootBean, "my_code");
        node2.set(rootBean, "true");
        node3.set(rootBean, "999", "count");
        node4.set(rootBean, "123", "id");

        assertEquals("my_code", rootBean.getCode());
        assertEquals(Boolean.TRUE, rootBean.isStatus());
        assertEquals(999, rootBean.getCount());
        assertEquals(Id.valueOf("123"), rootBean.getId());
    }
}
