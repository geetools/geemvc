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

package com.geemvc.reader.bean.adapter;

import com.geemvc.annotation.Adapter;
import com.geemvc.reader.bean.AbstractBeanReader;
import com.geemvc.reader.bean.BeanReaderAdapter;
import com.google.inject.Singleton;

@Adapter
@Singleton
public class DefaultBeanReaderAdapter extends AbstractBeanReader implements BeanReaderAdapter<Object> {
    @Override
    public Object lookup(String expression, Object beanInstance) {
        return _lookup(expression, beanInstance);
    }
}
