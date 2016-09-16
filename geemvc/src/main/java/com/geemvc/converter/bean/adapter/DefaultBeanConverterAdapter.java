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

package com.geemvc.converter.bean.adapter;

import java.util.List;

import com.geemvc.annotation.Adapter;
import com.geemvc.converter.bean.AbstractBeanConverter;
import com.geemvc.converter.bean.BeanConverterAdapter;
import com.google.inject.Singleton;

@Adapter
@Singleton
public class DefaultBeanConverterAdapter extends AbstractBeanConverter implements BeanConverterAdapter<Object> {

    @Override
    public BeanConverterAdapter bindProperty(Object beanInstance, String expression, List<String> value) {
        super._bindProperty(beanInstance, expression, value);
        return this;
    }

    @Override
    public BeanConverterAdapter bindProperty(Object beanInstance, String expression, String value) {
        super._bindProperty(beanInstance, expression, value);
        return this;
    }

    @Override
    public BeanConverterAdapter bindProperties(List<String> values, String beanName, Object beanInstance) {
        super._bindProperties(values, beanName, beanInstance);
        return this;
    }

    @Override
    public Object fromStrings(List<String> values, String beanName, Class<Object> beanType) {
        return super._fromStrings(values, beanName, beanType);
    }

    @Override
    public Object fromStrings(List<String> values, String beanName, Class<Object> beanType, int index) {
        return super._fromStrings(values, beanName, beanType, index);
    }

    @Override
    public Object fromStrings(List<String> values, String beanName, Class<Object> beanType, int index, String mapKey) {
        return super._fromStrings(values, beanName, beanType, index, mapKey);
    }

    @Override
    public Object fromStrings(List<String> values, String beanName, Class<Object> beanType, String mapKey) {
        return super._fromStrings(values, beanName, beanType, mapKey);
    }

    @Override
    public Object fromStrings(List<String> values, String beanName, Class<Object> beanType, String mapKey, int index) {
        return super._fromStrings(values, beanName, beanType, mapKey, index);
    }

    @Override
    public Object newInstance(Class<Object> beanType) {
        return super._newInstance(beanType);
    }
}
