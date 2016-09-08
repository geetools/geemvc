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

import com.geemvc.Char;
import com.geemvc.Str;
import com.geemvc.i18n.message.CompositeMessageResolver;
import com.geemvc.taglib.HtmlTagSupport;
import org.apache.commons.io.IOUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.StringWriter;

public class LabelTagSupport extends HtmlTagSupport {
    protected String name;

    protected Object value;

    protected String label;

    protected boolean closeTag = true;

    protected final CompositeMessageResolver messageResolver;

    public LabelTagSupport() {
        this.messageResolver = injector.getInstance(CompositeMessageResolver.class);
    }

    @Override
    public void doTag() throws JspException {
        writeTag(jspContext.getOut(), "label", true, closeTag);
    }

    @Override
    protected void appendTagAttributes(JspWriter writer) throws JspException {
        try {
            String forValue = (String) dynamicAttributes.get("for");

            if (forValue == null) {
                String name = getName();
                Object value = getValue();

                if (name != null)
                    forValue = toElementId(name, value == null ? null : String.valueOf(value));
            }

            if (!Str.isEmpty(forValue)) {
                // Id attribute.
                writer.write(Char.SPACE);
                writer.write("for");
                writer.write(Char.EQUALS);
                writer.write(Char.DOUBLE_QUOTE);
                writer.write(forValue);
                writer.write(Char.DOUBLE_QUOTE);
            }

            String classValue = (String) dynamicAttributes.get("class");

            if (!Str.isEmpty(classValue)) {
                writer.write(Char.SPACE);
                writer.write("class");
                writer.write(Char.EQUALS);
                writer.write(Char.DOUBLE_QUOTE);
                writer.write(classValue);
                writer.write(Char.DOUBLE_QUOTE);
            }

        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    @Override
    protected void appendTagBody(JspWriter writer) throws JspException {
        StringWriter bodyWriter = null;

        try {
            if (jspBody != null && label == null) {
                bodyWriter = new StringWriter();
                jspBody.invoke(bodyWriter);
                label = bodyWriter.toString();
            }

            if (label == null) {
                label = messageResolver.resolve(name, requestContext(), true);
            }

            if (label != null)
                writer.write(label);
            
        } catch (Throwable t) {
            throw new JspException(t);
        } finally {
            IOUtils.closeQuietly(bodyWriter);
        }
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCloseTag() {
        return closeTag;
    }

    public void setCloseTag(boolean closeTag) {
        this.closeTag = closeTag;
    }
}
