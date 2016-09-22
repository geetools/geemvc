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

import com.geemvc.Char;
import com.geemvc.Str;
import com.geemvc.bind.PropertyNode;
import com.google.inject.Inject;
import com.google.inject.Injector;

public abstract class AbstractBeanConverter {

    @Inject
    protected Injector injector;

    public void _bindProperty(Object beanInstance, String expression, List<String> value) {
        if (value != null && value.size() > 1) {
            for (String val : value) {
                _bindProperty(beanInstance, expression, val);
            }
        } else if (value != null) {
            _bindProperty(beanInstance, expression, value.get(0));
        }
    }

    public void _bindProperty(Object beanInstance, String expression, String value) {
        int dotPos = expression.indexOf(Char.DOT);
        String propertyExpression = expression.substring(dotPos + 1);

        PropertyNode propertyNode = injector.getInstance(PropertyNode.class).build(propertyExpression, beanInstance.getClass());

        if (propertyExpression.contains(Str.SQUARE_BRACKET_OPEN)) {
            propertyNode.set(beanInstance, value, propertyExpression);
        } else {
            propertyNode.set(beanInstance, value);
        }
    }

    public void _bindProperties(List<String> values, String beanName, Object beanInstance) {
        if (beanName != null) {
            for (String val : values) {
                int equalsPos = val.indexOf(Char.EQUALS);
                String propertyExpression = val.substring(0, equalsPos);
                String properyValue = val.substring(equalsPos + 1);

                _bindProperty(beanInstance, propertyExpression, properyValue);
            }
        }
    }

    public Object _fromStrings(List<String> values, String beanName, Class<?> beanType) {
        Object beanInstance = null;

        if (beanName != null && beanType != null) {
            beanInstance = _newInstance(beanType);

            for (String val : values) {
                int equalsPos = val.indexOf(Char.EQUALS);
                String propertyExpression = val.substring(0, equalsPos);
                String properyValue = val.substring(equalsPos + 1);

                _bindProperty(beanInstance, propertyExpression, properyValue);
            }
        }

        return beanInstance;
    }

    public Object _fromStrings(List<String> values, String beanName, Class<?> beanType, int index) {
        Object beanInstance = null;

        if (beanName != null && beanType != null) {
            beanInstance = _newInstance(beanType);

            for (String val : values) {
                int equalsPos = val.indexOf(Char.EQUALS);
                String propertyExpression = val.substring(0, equalsPos);

                if (propertyExpression.startsWith(new StringBuilder(beanName).append(Char.SQUARE_BRACKET_OPEN).append(index).append(Char.SQUARE_BRACKET_CLOSE).toString())) {
                    String properyValue = val.substring(equalsPos + 1);

                    _bindProperty(beanInstance, propertyExpression, properyValue);
                }
            }
        }

        return beanInstance;
    }

    public Object _fromStrings(List<String> values, String beanName, Class<?> beanType, int index, String mapKey) {
        Object beanInstance = null;

        if (beanName != null && beanType != null) {
            beanInstance = _newInstance(beanType);

            for (String val : values) {
                int equalsPos = val.indexOf(Char.EQUALS);
                String propertyExpression = val.substring(0, equalsPos);

                if (propertyExpression.startsWith(new StringBuilder(beanName).append(Char.SQUARE_BRACKET_OPEN).append(index).append(Char.SQUARE_BRACKET_CLOSE).append(Char.SQUARE_BRACKET_OPEN).append(mapKey).append(Char.SQUARE_BRACKET_CLOSE)
                        .toString())) {
                    String properyValue = val.substring(equalsPos + 1);

                    _bindProperty(beanInstance, propertyExpression, properyValue);
                }
            }
        }

        return beanInstance;
    }

    public Object _fromStrings(List<String> values, String beanName, Class<?> beanType, String mapKey) {
        Object beanInstance = null;

        if (beanName != null && beanType != null) {
            beanInstance = _newInstance(beanType);

            for (String val : values) {
                int equalsPos = val.indexOf(Char.EQUALS);
                String propertyExpression = val.substring(0, equalsPos);

                if (propertyExpression.startsWith(new StringBuilder(beanName).append(Char.SQUARE_BRACKET_OPEN).append(mapKey).append(Char.SQUARE_BRACKET_CLOSE).toString())) {
                    String properyValue = val.substring(equalsPos + 1);

                    _bindProperty(beanInstance, propertyExpression, properyValue);
                }
            }
        }

        return beanInstance;
    }

    public Object _fromStrings(List<String> values, String beanName, Class<?> beanType, String mapKey, int index) {
        Object beanInstance = null;

        if (beanName != null && beanType != null) {
            beanInstance = _newInstance(beanType);

            for (String val : values) {
                int equalsPos = val.indexOf(Char.EQUALS);
                String propertyExpression = val.substring(0, equalsPos);

                if (propertyExpression.startsWith(new StringBuilder(beanName).append(Char.SQUARE_BRACKET_OPEN).append(mapKey).append(Char.SQUARE_BRACKET_CLOSE).append(Char.SQUARE_BRACKET_OPEN).append(index).append(Char.SQUARE_BRACKET_CLOSE)
                        .toString())) {
                    String properyValue = val.substring(equalsPos + 1);

                    _bindProperty(beanInstance, propertyExpression, properyValue);
                }
            }
        }

        return beanInstance;
    }

    public Object _newInstance(Class<?> beanType) {
        return injector.getInstance(beanType);
    }
}
