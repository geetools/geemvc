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

package com.geemvc.taglib.form;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.geemvc.Char;
import com.geemvc.Str;
import com.geemvc.reflect.ReflectionProvider;

import jodd.bean.BeanUtil;

public class OptionsTagSupport extends OptionTagSupport {
    // Expression for value in map or collection.
    protected String value;

    // Expression for key/label in map.
    protected String key;

    // Expression for key/label in collection.
    protected String label;

    // Map, collection or array of values to use for map.
    protected Object values;

    protected Boolean isMapKeySimpleType;

    protected Boolean isMapValueSimpleType;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void doTag() throws JspException {
        try {
            if (values instanceof Collection) {
                writeOptionsFromCollection((Collection<?>) values);
            } else if (values instanceof Enumeration) {
                Collection<?> collectionValues = Collections.list((Enumeration) values);
                writeOptionsFromCollection(collectionValues);
            } else if (values instanceof Map) {
                writeOptionsFromMap((Map<?, ?>) values);
            } else if (values.getClass().isArray()) {
                writeOptionsFromCollection(Arrays.asList((Object[]) values));
            } else if (values instanceof String) {
                Object foundValues = attribute((String) values);

                if (foundValues != null) {
                    if (foundValues instanceof Collection) {
                        Collection<?> collectionValues = (Collection<?>) foundValues;
                        writeOptionsFromCollection(collectionValues);
                    } else if (foundValues instanceof Enumeration) {
                        Collection<?> collectionValues = Collections.list((Enumeration) foundValues);
                        writeOptionsFromCollection(collectionValues);
                    } else if (foundValues instanceof Map) {
                        writeOptionsFromMap((Map<?, ?>) foundValues);
                    } else if (foundValues.getClass().isArray()) {
                        Collection<?> collectionValues = Arrays.asList((Object[]) foundValues);
                        writeOptionsFromCollection(collectionValues);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void writeOptionsFromCollection(Collection<?> collectionValues) throws IOException, JspException {
        JspWriter writer = jspContext.getOut();

        ReflectionProvider reflectionProvider = injector.getInstance(ReflectionProvider.class);

        Boolean isSimpleType = null;

        for (Object obj : collectionValues) {
            if (isSimpleType == null)
                isSimpleType = reflectionProvider.isSimpleType(obj.getClass());

            Object optionValue = null;
            Object optionLabel = null;

            if (isSimpleType) {
                optionValue = obj;
                optionLabel = obj;
            } else {
                if (value == null)
                    throw new JspException("When creating select-options from a collection of beans you must specifiy the attribute of that bean to use for the value. Please check you select-options for '" + parentName() + "'");

                optionValue = BeanUtil.getProperty(obj, value);
                optionLabel = BeanUtil.getProperty(obj, label == null ? value : label);
            }

            writeTag(jspContext.getOut(), "option", false, false);

            writer.write(Char.SPACE);
            writer.write("value");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write(String.valueOf(optionValue));
            writer.write(Char.DOUBLE_QUOTE);

            if (isValueSelected(optionValue)) {
                writer.write(Char.SPACE);
                writer.write("selected");
                writer.write(Char.EQUALS);
                writer.write(Char.DOUBLE_QUOTE);
                writer.write("selected");
                writer.write(Char.DOUBLE_QUOTE);
            }

            writer.write(Char.GREATER_THAN);

            // See if there is a translated version of the label in the message properties.
            String i18nOptionLabel = messageResolver.resolve(new StringBuilder(optionValue.getClass().getName()).append(Char.DOT).append(optionValue).toString(), requestContext(), true);

            if (i18nOptionLabel == null)
                i18nOptionLabel = messageResolver.resolve(new StringBuilder(optionValue.getClass().getSimpleName()).append(Char.DOT).append(optionValue).toString(), requestContext(), true);

            if (!Str.isEmpty(i18nOptionLabel)) {
                writer.write(i18nOptionLabel);
            } else {
                writer.write(optionLabel == null ? String.valueOf(optionValue) : String.valueOf(optionLabel));
            }

            writeCloseTag(writer, "option", true);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void writeOptionsFromMap(Map mapValues) throws IOException, JspException {
        JspWriter writer = jspContext.getOut();

        ReflectionProvider reflectionProvider = injector.getInstance(ReflectionProvider.class);

        Set<Entry> mapEntries = mapValues.entrySet();

        for (Entry<?, ?> entry : mapEntries) {
            if (isMapKeySimpleType == null)
                isMapKeySimpleType = reflectionProvider.isSimpleType(entry.getKey().getClass());

            if (isMapValueSimpleType == null)
                isMapValueSimpleType = reflectionProvider.isSimpleType(entry.getValue().getClass());

            Object optionValue = optionValue(entry);
            String optionLabel = optionLabel(entry);

            writeTag(jspContext.getOut(), "option", false, false);

            writer.write(Char.SPACE);
            writer.write("value");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write(String.valueOf(optionValue));
            writer.write(Char.DOUBLE_QUOTE);

            if (isValueSelected(optionValue)) {
                writer.write(Char.SPACE);
                writer.write("selected");
                writer.write(Char.EQUALS);
                writer.write(Char.DOUBLE_QUOTE);
                writer.write("selected");
                writer.write(Char.DOUBLE_QUOTE);
            }

            writer.write(Char.GREATER_THAN);
            writer.write(optionLabel == null ? String.valueOf(optionValue) : optionLabel);

            writeCloseTag(writer, "option", true);
        }
    }

    @SuppressWarnings("rawtypes")
    protected Object optionValue(Entry mapEntry) {
        boolean optionValueIsMapKey = value == null || value.startsWith("key::");

        if (optionValueIsMapKey) {
            String propertyName = value == null ? null : value.startsWith("key::") ? value.substring(5).trim() : value;

            return mapKey(mapEntry, propertyName);
        } else {
            String propertyName = value == null ? null : value.startsWith("value::") ? value.substring(7).trim() : value;

            return mapValue(mapEntry, propertyName);
        }
    }

    @SuppressWarnings("rawtypes")
    protected String optionLabel(Entry mapEntry) {
        boolean optionLabelIsMapValue = label == null || label.startsWith("value::");

        if (optionLabelIsMapValue) {
            String propertyName = label == null ? null : label.startsWith("value::") ? label.substring(7).trim() : label;
            Object val = mapValue(mapEntry, propertyName);

            return val == null ? null : String.valueOf(val);
        } else {
            String propertyName = label == null ? null : label.startsWith("key::") ? label.substring(5).trim() : label;
            Object key = mapKey(mapEntry, propertyName);

            return key == null ? null : String.valueOf(key);
        }
    }

    @SuppressWarnings("rawtypes")
    protected Object mapKey(Entry mapEntry, String properyName) {
        Object mapKey = null;

        if (isMapKeySimpleType || "this".equals(properyName)) {
            mapKey = mapEntry.getKey();
        } else {
            if (properyName != null)
                mapKey = BeanUtil.getProperty(mapEntry.getKey(), properyName);
        }

        return mapKey;
    }

    @SuppressWarnings("rawtypes")
    protected Object mapValue(Entry mapEntry, String properyName) {
        Object mapValue = null;

        if (isMapValueSimpleType || "this".equals(properyName)) {
            mapValue = mapEntry.getValue();
        } else {
            if (properyName != null)
                mapValue = BeanUtil.getProperty(mapEntry.getValue(), properyName);
        }

        return mapValue;
    }

    protected void appendTagAttributes(JspWriter writer) throws JspException {

    }

    protected String parentName() {
        SelectTagSupport selectTag = (SelectTagSupport) getParent();
        return selectTag.getName();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValues() {
        return values;
    }

    public void setValues(Object values) {
        this.values = values;
    }
}
