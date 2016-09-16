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

package com.geemvc.converter.bean;

import java.util.List;

public interface BeanConverterAdapter<T> {
    BeanConverterAdapter bindProperty(T bean, String expression, List<String> value);

    BeanConverterAdapter bindProperty(T bean, String expression, String value);

    BeanConverterAdapter bindProperties(List<String> values, String beanName, T beanInstance);

    T fromStrings(List<String> values, String beanName, Class<T> beanType);

    T fromStrings(List<String> values, String beanName, Class<T> beanType, int index);

    T fromStrings(List<String> values, String beanName, Class<T> beanType, String mapKey);

    T fromStrings(List<String> values, String beanName, Class<T> beanType, String mapKey, int index);

    T fromStrings(List<String> values, String beanName, Class<T> beanType, int index, String mapKey);

    T newInstance(Class<T> beanType);
}
