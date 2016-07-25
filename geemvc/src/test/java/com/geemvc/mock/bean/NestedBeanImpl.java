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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.geemvc.mock.Id;

public class NestedBeanImpl implements NestedBean {
    private Id id = null;

    private String code = null;

    private List<String> tags = null;

    private int count;

    private boolean status;

    private NestedBean nestedBean = null;

    private List<NestedBean> nestedBeans = null;

    private Map<String, NestedBean> mappedNestedBeans = null;

    private NestedBean[] nestedBeanArray = null;

    private String[] tagsArray = null;

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public void setId(Id id) {
        this.id = id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean isStatus() {
        return status;
    }

    @Override
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public NestedBean getNestedBean() {
        return nestedBean;
    }

    @Override
    public void setNestedBean(NestedBean nestedBean) {
        this.nestedBean = nestedBean;
    }

    @Override
    public List<NestedBean> getNestedBeans() {
        return nestedBeans;
    }

    @Override
    public void setNestedBeans(List<NestedBean> nestedBeans) {
        this.nestedBeans = nestedBeans;
    }

    @Override
    public Map<String, NestedBean> getMappedNestedBeans() {
        return mappedNestedBeans;
    }

    @Override
    public void setMappedNestedBeans(Map<String, NestedBean> mappedNestedBeans) {
        this.mappedNestedBeans = mappedNestedBeans;
    }

    @Override
    public NestedBean[] getNestedBeanArray() {
        return nestedBeanArray;
    }

    @Override
    public void setNestedBeanArray(NestedBean[] nestedBeanArray) {
        this.nestedBeanArray = nestedBeanArray;
    }

    @Override
    public String[] getTagsArray() {
        return tagsArray;
    }

    @Override
    public void setTagsArray(String[] tagsArray) {
        this.tagsArray = tagsArray;
    }

    @Override
    public String toString() {
        return "NestedBeanImpl [id=" + id + ", code=" + code + ", tags=" + tags + ", count=" + count + ", status=" + status + ", nestedBean=" + nestedBean + ", nestedBeans=" + nestedBeans + ", mappedNestedBeans=" + mappedNestedBeans
                + ", nestedBeanArray=" + Arrays.toString(nestedBeanArray) + ", tagsArray=" + Arrays.toString(tagsArray) + "]";
    }
}
