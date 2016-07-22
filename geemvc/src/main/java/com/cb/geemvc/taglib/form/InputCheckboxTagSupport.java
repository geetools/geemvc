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

import com.cb.geemvc.Char;
import com.cb.geemvc.Str;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class InputCheckboxTagSupport extends OptionTagSupport {
    protected String id;

    protected String name;

    protected Object value;

    protected String optionLabel;

    protected String optionLabelClass;

    public InputCheckboxTagSupport() {
        dynamicAttributes.put("type", "checkbox");
    }

    @Override
    public void doTag() throws JspException {
        String name = getName();
        String id = getId();

        if (id == null)
            id = toElementId(name);

        try {
            writePreFieldBlock(id, name, value);
            writeTag(jspContext.getOut(), "input", false);
            writePostFieldBlock(name);
        } catch (JspException e) {
            throw e;
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    public void writePreFieldBlock(String elementId, String name, Object value) throws JspException, IOException {
        if (fieldOnly)
            return;

        JspWriter writer = jspContext.getOut();

        writer.write("<fieldset id=\"");
        writer.write("fs-");
        writer.write(elementId);
        writer.write("\"");
        writer.write(" class=\"form-field");

        if (!Str.isEmpty(getGroupClass())) {
            writer.write(Char.SPACE);
            writer.write(getGroupClass());
        }

        if (hasError(name)) {
            writer.write(" has-error");
        }

        if (hasNotice(name)) {
            writer.write(" has-notice");
        }

        writer.write("\">\n");


        writer.write("<label");
        if (!Str.isEmpty(getLabelClass())) {
            writer.write(" class=\"");
            writer.write(getLabelClass());
            writer.write("\"");
        }
        writer.write(">\n");
        writer.write(Str.isEmpty(getLabel()) ? "&nbsp;" : getLabel());
        writer.write("</label>");


        writer.write("<div");

        if (!Str.isEmpty(getWrapperClass())) {
            writer.write(" class=\"");
            writer.write(getWrapperClass());
            writer.write("\"");
        }

        writer.write(">\n");

        writer.write("<div class=\"");
        writer.write((String) dynamicAttributes.get("type"));
        writer.write("\">");

        writeLabel(name, value);
    }

    @Override
    public void writePostFieldBlock(String fieldName) throws JspException, IOException {
        if (fieldOnly)
            return;

        JspWriter writer = jspContext.getOut();

        if (!Str.isEmpty(optionLabel)) {
            writer.write(this.optionLabel);
        }

        writer.write("</label>\n");
        writer.write("</div>\n");

        String hint = getHint();

        if (hint == null)
            hint = hint(fieldName);

        if (!Str.isEmpty(hint)) {
            writer.write("<p class=\"hint");

            if (!Str.isEmpty(getHintClass())) {
                writer.write(Char.SPACE);
                writer.write(getHintClass());
            }

            writer.write("\">\n");

            writer.write(hint);
            writer.write("</p>\n");
        }

        FormTagSupport formTag = formTag();
        boolean displayFieldErrors = formTag != null && formTag.isFieldErrors();

        if (displayFieldErrors && hasError(fieldName)) {
            writer.write("<small class=\"error");

            if (!Str.isEmpty(getErrorClass())) {
                writer.write(Char.SPACE);
                writer.write(getErrorClass());
            }

            writer.write("\">\n");

            writer.write(errorMessage(fieldName));
            writer.write("</small>\n");
        }

        boolean displayFieldNotices = formTag != null && formTag.isFieldNotices();

        if (displayFieldNotices && hasNotice(fieldName)) {
            writer.write("<span class=\"notice");

            if (!Str.isEmpty(getNoticeClass())) {
                writer.write(Char.SPACE);
                writer.write(getNoticeClass());
            }

            writer.write("\">\n");

            writer.write(noticeMessage(fieldName));
            writer.write("</span>\n");
        }

        writer.write("</div>\n");

        writer.write("</fieldset>\n");
    }

    @Override
    protected void appendTagAttributes(JspWriter writer) throws JspException {
        try {
            String name = getName();
            String id = getId();
            String value = getValue() == null ? null : String.valueOf(getValue());

            if (id == null)
                id = toElementId(name, value);

            // Id attribute.
            writer.write(Char.SPACE);
            writer.write("id");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write(id);
            writer.write(Char.DOUBLE_QUOTE);

            // Name attribute.
            writer.write(Char.SPACE);
            writer.write("name");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write(name);
            writer.write(Char.DOUBLE_QUOTE);

            // Value attribute.
            writer.write(Char.SPACE);
            writer.write("value");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write(value == null ? Str.EMPTY : value);
            writer.write(Char.DOUBLE_QUOTE);

            if (isValueSelected(value)) {
                writer.write(Char.SPACE);
                writer.write("checked");
                writer.write(Char.EQUALS);
                writer.write(Char.DOUBLE_QUOTE);
                writer.write("checked");
                writer.write(Char.DOUBLE_QUOTE);
            }

            String classValue = (String) dynamicAttributes.get("class");

            if (classValue == null && !fieldOnly) {
                FormTagSupport formTag = formTag();
                classValue = formTag.fieldClass;
            }

            writer.write(Char.SPACE);
            writer.write("class");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write("form-el");

            if (!Str.isEmpty(classValue)) {
                writer.write(Char.SPACE);
                writer.write(classValue);
            }

            writer.write(Char.DOUBLE_QUOTE);

        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    public void writeLabel(String name, Object value) throws JspException {
        String id = getId();

        InputLabelTagSupport labelTagSupport = new InputLabelTagSupport();
        labelTagSupport.setJspContext(jspContext);
        labelTagSupport.setName(name);
        labelTagSupport.setValue(value);
        labelTagSupport.setCloseTag(false);

        if (!Str.isEmpty(id))
            labelTagSupport.setDynamicAttribute(null, "for", id);

        if (!Str.isEmpty(optionLabelClass))
            labelTagSupport.setDynamicAttribute(null, "class", optionLabelClass);

        if (!Str.isEmpty(optionLabel)) {
            labelTagSupport.setLabel("");
        }

        labelTagSupport.doTag();
    }

    protected boolean isValueSelected(Object optionValue) {
        return isValueSelected(optionValue, predefinedValue(getName()));
    }

    @Override
    protected void writeCloseTag(JspWriter writer, String tagName, boolean hasTagBody) throws JspException {
        try {
            writer.write(Char.SPACE);
            writer.write(Char.GREATER_THAN);
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public String getOptionLabelClass() {
        return optionLabelClass;
    }

    public void setOptionLabelClass(String optionLabelClass) {
        this.optionLabelClass = optionLabelClass;
    }
}
