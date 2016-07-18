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

package com.cb.geemvc.mock.bean;

import java.util.List;
import java.util.Map;

import com.cb.geemvc.mock.Id;

public interface RootFluentBean {
    public Id getId();

    public RootFluentBean setId(Id id);

    public String getCode();

    public RootFluentBean setCode(String code);

    public List<String> getTags();

    public RootFluentBean setTags(List<String> tags);

    public int getCount();

    public RootFluentBean setCount(int count);

    public boolean isStatus();

    public RootFluentBean setStatus(boolean status);

    public NestedFluentBean getNestedBean();

    public RootFluentBean setNestedBean(NestedFluentBean nestedBean);

    public List<NestedFluentBean> getNestedBeans();

    public RootFluentBean setNestedBeans(List<NestedFluentBean> nestedBeans);

    public Map<String, NestedFluentBean> getMappedNestedBeans();

    public RootFluentBean setMappedNestedBeans(Map<String, NestedFluentBean> mappedNestedBeans);

    public NestedFluentBean[] getNestedBeanArray();

    public RootFluentBean setNestedBeanArray(NestedFluentBean[] nestedBeanArray);

    public String[] getTagsArray();

    public RootFluentBean setTagsArray(String[] tagsArray);

    public Id[] getIdsArray();

    public RootFluentBean setIdsArray(Id[] idsArray);
}
