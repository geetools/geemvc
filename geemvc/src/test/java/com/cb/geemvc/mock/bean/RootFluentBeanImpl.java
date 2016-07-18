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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cb.geemvc.mock.Id;

public class RootFluentBeanImpl implements RootFluentBean {
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

    private Id[] idsArray = null;

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public RootFluentBean setId(Id id) {
        this.id = id;
        return this;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public RootFluentBean setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public RootFluentBean setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public RootFluentBean setCount(int count) {
        this.count = count;
        return this;
    }

    @Override
    public boolean isStatus() {
        return status;
    }

    @Override
    public RootFluentBean setStatus(boolean status) {
        this.status = status;
        return this;
    }

    @Override
    public NestedFluentBean getNestedBean() {
        return nestedBean;
    }

    @Override
    public RootFluentBean setNestedBean(NestedFluentBean nestedBean) {
        this.nestedBean = nestedBean;
        return this;
    }

    @Override
    public List<NestedFluentBean> getNestedBeans() {
        return nestedBeans;
    }

    @Override
    public RootFluentBean setNestedBeans(List<NestedFluentBean> nestedBeans) {
        this.nestedBeans = nestedBeans;
        return this;
    }

    @Override
    public Map<String, NestedFluentBean> getMappedNestedBeans() {
        return mappedNestedBeans;
    }

    @Override
    public RootFluentBean setMappedNestedBeans(Map<String, NestedFluentBean> mappedNestedBeans) {
        this.mappedNestedBeans = mappedNestedBeans;
        return this;
    }

    @Override
    public NestedFluentBean[] getNestedBeanArray() {
        return nestedBeanArray;
    }

    @Override
    public RootFluentBean setNestedBeanArray(NestedFluentBean[] nestedBeanArray) {
        this.nestedBeanArray = nestedBeanArray;
        return this;
    }

    @Override
    public String[] getTagsArray() {
        return tagsArray;
    }

    @Override
    public RootFluentBean setTagsArray(String[] tagsArray) {
        this.tagsArray = tagsArray;
        return this;
    }

    @Override
    public Id[] getIdsArray() {
        return idsArray;
    }

    @Override
    public RootFluentBean setIdsArray(Id[] idsArray) {
        this.idsArray = idsArray;
        return this;
    }

    @Override
    public String toString() {
        return "RootBeanImpl [id=" + id + ", code=" + code + ", tags=" + tags + ", count=" + count + ", status=" + status + ", nestedBean=" + nestedBean + ", nestedBeans=" + nestedBeans + ", mappedNestedBeans=" + mappedNestedBeans
                + ", nestedBeanArray=" + Arrays.toString(nestedBeanArray) + ", tagsArray=" + Arrays.toString(tagsArray) + ", idsArray=" + Arrays.toString(idsArray) + "]";
    }
}
