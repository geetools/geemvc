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

package com.geemvc.mock.bean;

import java.util.List;
import java.util.Map;

import com.geemvc.mock.Id;

public interface NestedFluentBean {
    public Id getId();

    public NestedFluentBean setId(Id id);

    public String getCode();

    public NestedFluentBean setCode(String code);

    public List<String> getTags();

    public NestedFluentBean setTags(List<String> tags);

    public int getCount();

    public NestedFluentBean setCount(int count);

    public boolean isStatus();

    public NestedFluentBean setStatus(boolean status);

    public NestedFluentBean getNestedBean();

    public NestedFluentBean setNestedBean(NestedFluentBean nestedBean);

    public List<NestedFluentBean> getNestedBeans();

    public NestedFluentBean setNestedBeans(List<NestedFluentBean> nestedBeans);

    public Map<String, NestedFluentBean> getMappedNestedBeans();

    public NestedFluentBean setMappedNestedBeans(Map<String, NestedFluentBean> mappedNestedBeans);

    public NestedFluentBean[] getNestedBeanArray();

    public NestedFluentBean setNestedBeanArray(NestedFluentBean[] nestedBeanArray);

    public String[] getTagsArray();

    public NestedFluentBean setTagsArray(String[] tagsArray);
}
