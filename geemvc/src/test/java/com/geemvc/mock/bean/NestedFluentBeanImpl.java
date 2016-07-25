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

public class NestedFluentBeanImpl implements NestedFluentBean {
    private Id id = null;

    private String code = null;

    private List<String> tags = null;

    private int count;

    private boolean status;

    private NestedFluentBean nestedBean = null;

    private List<NestedFluentBean> nestedBeans = null;

    private Map<String, NestedFluentBean> mappedNestedBeans = null;

    private NestedFluentBean[] nestedBeanArray = null;

    private String[] tagsArray = null;

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public NestedFluentBean setId(Id id) {
        this.id = id;
        return this;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public NestedFluentBean setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public NestedFluentBean setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public NestedFluentBean setCount(int count) {
        this.count = count;
        return this;
    }

    @Override
    public boolean isStatus() {
        return status;
    }

    @Override
    public NestedFluentBean setStatus(boolean status) {
        this.status = status;
        return this;
    }

    @Override
    public NestedFluentBean getNestedBean() {
        return nestedBean;
    }

    @Override
    public NestedFluentBean setNestedBean(NestedFluentBean nestedBean) {
        this.nestedBean = nestedBean;
        return this;
    }

    @Override
    public List<NestedFluentBean> getNestedBeans() {
        return nestedBeans;
    }

    @Override
    public NestedFluentBean setNestedBeans(List<NestedFluentBean> nestedBeans) {
        this.nestedBeans = nestedBeans;
        return this;
    }

    @Override
    public Map<String, NestedFluentBean> getMappedNestedBeans() {
        return mappedNestedBeans;
    }

    @Override
    public NestedFluentBean setMappedNestedBeans(Map<String, NestedFluentBean> mappedNestedBeans) {
        this.mappedNestedBeans = mappedNestedBeans;
        return this;
    }

    @Override
    public NestedFluentBean[] getNestedBeanArray() {
        return nestedBeanArray;
    }

    @Override
    public NestedFluentBean setNestedBeanArray(NestedFluentBean[] nestedBeanArray) {
        this.nestedBeanArray = nestedBeanArray;
        return this;
    }

    @Override
    public String[] getTagsArray() {
        return tagsArray;
    }

    @Override
    public NestedFluentBean setTagsArray(String[] tagsArray) {
        this.tagsArray = tagsArray;
        return this;
    }

    @Override
    public String toString() {
        return "NestedFluentBeanImpl [id=" + id + ", code=" + code + ", tags=" + tags + ", count=" + count + ", status=" + status + ", nestedBean=" + nestedBean + ", nestedBeans=" + nestedBeans + ", mappedNestedBeans=" + mappedNestedBeans
                + ", nestedBeanArray=" + Arrays.toString(nestedBeanArray) + ", tagsArray=" + Arrays.toString(tagsArray) + "]";
    }
}
