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

public interface NestedBean {
    public Id getId();

    public void setId(Id id);

    public String getCode();

    public void setCode(String code);

    public List<String> getTags();

    public void setTags(List<String> tags);

    public int getCount();

    public void setCount(int count);

    public boolean isStatus();

    public void setStatus(boolean status);

    public NestedBean getNestedBean();

    public void setNestedBean(NestedBean nestedBean);

    public List<NestedBean> getNestedBeans();

    public void setNestedBeans(List<NestedBean> nestedBeans);

    public Map<String, NestedBean> getMappedNestedBeans();

    public void setMappedNestedBeans(Map<String, NestedBean> mappedNestedBeans);

    public NestedBean[] getNestedBeanArray();

    public void setNestedBeanArray(NestedBean[] nestedBeanArray);

    public String[] getTagsArray();

    public void setTagsArray(String[] tagsArray);
}
