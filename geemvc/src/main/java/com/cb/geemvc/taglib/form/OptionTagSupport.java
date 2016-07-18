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

package com.cb.geemvc.taglib.form;

import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import jodd.typeconverter.TypeConversionException;

import com.cb.geemvc.Char;
import com.cb.geemvc.converter.SimpleConverter;
import com.cb.geemvc.taglib.HtmlTagSupport;

public class OptionTagSupport extends FormFieldTagSupport {
    protected Object value;

    @Override
    public void doTag() throws JspException {
        writeTag(jspContext.getOut(), "option", true);
    }

    protected void appendTagAttributes(JspWriter writer) throws JspException {
        try {
            // Value attribute.
            writer.write(Char.SPACE);
            writer.write("value");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write(String.valueOf(value));
            writer.write(Char.DOUBLE_QUOTE);

            if (isValueSelected(value)) {
                writer.write(Char.SPACE);
                writer.write("selected");
                writer.write(Char.EQUALS);
                writer.write(Char.DOUBLE_QUOTE);
                writer.write("selected");
                writer.write(Char.DOUBLE_QUOTE);
            }
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    protected boolean isValueSelected(Object optionValue) {
        SelectTagSupport selectTag = (SelectTagSupport) getParent();

        return isValueSelected(optionValue, selectTag.predefinedValue(selectTag.getName()));
    }

    @SuppressWarnings("rawtypes")
    protected boolean isValueSelected(Object optionValue, Object selectedValue) {
        if (selectedValue == null)
            return false;

        if (selectedValue instanceof Collection) {
            for (Object selectedValueItem : (Collection) selectedValue) {
                if (optionMatches(optionValue, selectedValueItem))
                    return true;
            }

            return false;
        } else if (selectedValue.getClass().isArray()) {
            for (Object selectedValueItem : (Object[]) selectedValue) {
                if (optionMatches(optionValue, selectedValueItem))
                    return true;
            }

            return false;
        } else if (selectedValue instanceof Enumeration) {
            Enumeration enumerationValues = (Enumeration) selectedValue;

            while (enumerationValues.hasMoreElements()) {
                Object selectedValueItem = (Object) enumerationValues.nextElement();

                if (optionMatches(optionValue, selectedValueItem))
                    return true;
            }

            return false;
        } else {
            return optionMatches(optionValue, selectedValue);
        }
    }

    protected boolean optionMatches(Object optionValue, Object selectedValue) {
        SimpleConverter converter = injector.getInstance(SimpleConverter.class);

        Class<?> selectedValueClass = selectedValue.getClass();
        Class<?> optionValueClass = optionValue.getClass();

        if (selectedValueClass == optionValueClass)
            return optionValue.equals(selectedValue);

        if (converter.canConvert(selectedValueClass)) {
            try {
                Object convertedOptionValue = converter.fromString(String.valueOf(optionValue), selectedValueClass);
                return convertedOptionValue.equals(selectedValue);
            } catch (TypeConversionException e) {
                // Conversion failed. Try String comparison next.
            }
        }

        return String.valueOf(selectedValue).equals(String.valueOf(value));
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
