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

public class SelectTagSupport extends FormFieldTagSupport {
    protected String id;

    protected String name;

    protected Object value;

    @Override
    public void doTag() throws JspException {
        String name = getName();
        String id = getId();

        if (id == null)
            id = toElementId(name);

        try {
            writePreFieldBlock(id, name, value, "");
            writeTag(jspContext.getOut(), "select", true);
            writePostFieldBlock(name, "");
        } catch (JspException e) {
            throw e;
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    protected void appendTagAttributes(JspWriter writer) throws JspException {
        try {
            String name = getName();
            String id = getId();

            if (id == null)
                id = toElementId(name);

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
}
