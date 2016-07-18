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
import com.cb.geemvc.taglib.GeemvcTagSupport;
import com.cb.geemvc.taglib.HtmlTagSupport;
import com.cb.geemvc.validation.Error;
import com.cb.geemvc.validation.Errors;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspTag;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.MessageFormat;

/**
 * Created by Michael on 05.07.2016.
 */
public class FormFieldTagSupport extends HtmlTagSupport {
    protected String label;
    protected String groupClass;
    protected String labelClass;
    protected String wrapperClass;
    protected String hintClass;
    protected String errorClass;

    public void writePreFieldBlock(String elementId, String name, Object value, String error) throws JspException, IOException {
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

        writer.write("\">\n");

        writeLabel(name, value);

        writer.write("<div");

        if (!Str.isEmpty(getWrapperClass())) {
            writer.write(" class=\"");
            writer.write(getWrapperClass());
            writer.write("\"");
        }

        writer.write(">\n");

        // wrappedField.append("<label class=\"control-label\" for=\"").append(elementId).append("\">").append(label).append("</label>\n")
        // .append(formFieldHTML);

    }

    public void writePostFieldBlock(String name, String hint) throws JspException, IOException {
        if (fieldOnly)
            return;

        JspWriter writer = jspContext.getOut();

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

        if (hasError(name)) {
            writer.write("<small class=\"error");

            if (!Str.isEmpty(getErrorClass())) {
                writer.write(Char.SPACE);
                writer.write(getErrorClass());
            }

            writer.write("\">\n");

            writer.write(errorMessage(name));
            writer.write("</small>\n");
        }

        writer.write("</div>\n");

        writer.write("</fieldset>\n");
    }

    public void writeLabel(String name, Object value) throws JspException {
        String id = getId();

        InputLabelTagSupport labelTagSupport = new InputLabelTagSupport();
        labelTagSupport.setJspContext(jspContext);
        labelTagSupport.setName(name);
        labelTagSupport.setValue(value);

        if (!Str.isEmpty(id))
            labelTagSupport.setDynamicAttribute(null, "for", id);

        if (!Str.isEmpty(getLabelClass()))
            labelTagSupport.setDynamicAttribute(null, "class", getLabelClass());

        if (!Str.isEmpty(label)) {
            labelTagSupport.setLabel(this.label);
        }

        labelTagSupport.doTag();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGroupClass() {
        if (groupClass == null) {
            FormTagSupport formTag = formTag();

            if (formTag != null)
                return formTag.getFieldGroupClass();
        }

        return groupClass;
    }

    public void setGroupClass(String groupClass) {
        this.groupClass = groupClass;
    }

    public String getLabelClass() {
        if (labelClass == null) {
            FormTagSupport formTag = formTag();

            if (formTag != null)
                return formTag.getFieldLabelClass();
        }

        return labelClass;
    }

    public void setLabelClass(String labelClass) {
        this.labelClass = labelClass;
    }

    public String getWrapperClass() {
        if (wrapperClass == null) {
            FormTagSupport formTag = formTag();

            if (formTag != null)
                return formTag.getFieldWrapperClass();
        }

        return wrapperClass;
    }

    public void setWrapperClass(String wrapperClass) {
        this.wrapperClass = wrapperClass;
    }

    public String getHintClass() {
        if (hintClass == null) {
            FormTagSupport formTag = formTag();

            if (formTag != null)
                return formTag.getFieldHintClass();
        }

        return hintClass;
    }

    public void setHintClass(String hintClass) {
        this.hintClass = hintClass;
    }

    public String getErrorClass() {
        if (errorClass == null) {
            FormTagSupport formTag = formTag();

            if (formTag != null)
                return formTag.getFieldErrorClass();
        }

        return errorClass;
    }

    public void setErrorClass(String errorClass) {
        this.errorClass = errorClass;
    }


    protected boolean hasError(String fieldName) {
        Errors errors = validationErrors();
        return errors != null && errors.exist(fieldName);
    }

    protected String errorMessage(String fieldName) {
        Errors errors = validationErrors();

        FormTagSupport formTag = formTag();
        Error error = errors.get(fieldName);
        String resolvedErrorMessage = null;

        if (error != null) {
            String errorMsgKey = new StringBuilder(formTag.getName())
                    .append(Char.DOT).append(fieldName).append(Char.DOT).append(error.message()).toString();

            try {
                resolvedErrorMessage = messageResolver.resolve(errorMsgKey, requestContext());
            } catch (Throwable t) {

            }

            if (resolvedErrorMessage == null) {
                errorMsgKey = new StringBuilder(fieldName)
                        .append(Char.DOT).append(error.message()).toString();

                try {
                    resolvedErrorMessage = messageResolver.resolve(errorMsgKey, requestContext());
                } catch (Throwable t) {

                }

                if (resolvedErrorMessage == null) {
                    try {
                        resolvedErrorMessage = messageResolver.resolve(error.message(), requestContext());
                    } catch (Throwable t) {

                    }
                }
            }

            String resolvedFieldName = null;

            if (label != null) {
                resolvedFieldName = label;
            } else {
                try {
                    resolvedFieldName = messageResolver.resolve(fieldName, requestContext());
                } catch (Throwable t) {
                    resolvedFieldName = fieldName;
                }
            }

            Object[] args = null;

            if (resolvedFieldName != null) {
                args = new Object[1 + (error.args() == null ? 0 : error.args().length)];
                Array.set(args, 0, resolvedFieldName);

                if (error.args() != null && error.args().length > 0) {
                    System.arraycopy(error.args(), 0, args, 1, error.args().length);
                }
            }

            if (args != null && args.length > 0)
                resolvedErrorMessage = MessageFormat.format(resolvedErrorMessage, args);
        }

        return resolvedErrorMessage == null ? error.message() : resolvedErrorMessage;
    }

    protected FormTagSupport formTag() {
//        JspTag parentTag = getParent();

//        if(parentTag == null)
//            return null;

        GeemvcTagSupport currentTagSupport = this;
        JspTag parentTag = null;
        FormTagSupport formTagSupport = null;

        while ((parentTag = currentTagSupport.getParent()) != null) {
            if (parentTag instanceof FormTagSupport) {
                formTagSupport = (FormTagSupport) parentTag;
                break;
            } else if (parentTag instanceof GeemvcTagSupport) {
                currentTagSupport = (GeemvcTagSupport) ((GeemvcTagSupport) parentTag).getParent();
            } else {
                break;
            }
        }

        return formTagSupport;
    }
}
