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
import com.geemvc.i18n.notice.Notice;
import com.geemvc.i18n.notice.Notices;
import com.geemvc.taglib.GeemvcTagSupport;
import com.geemvc.taglib.HtmlTagSupport;
import com.geemvc.validation.Error;
import com.geemvc.validation.Errors;

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
    protected String hint;
    protected String groupClass;
    protected String labelClass;
    protected String wrapperClass;
    protected String hintClass;
    protected String errorClass;
    protected String noticeClass;

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

        writeLabel(name, value);

        writer.write("<div");

        if (!Str.isEmpty(getWrapperClass())) {
            writer.write(" class=\"");
            writer.write(getWrapperClass());
            writer.write("\"");
        }

        writer.write(">\n");
    }

    public void writePostFieldBlock(String fieldName) throws JspException, IOException {
        if (fieldOnly)
            return;

        JspWriter writer = jspContext.getOut();

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

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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

    public void setNoticeClass(String noticeClass) {
        this.noticeClass = noticeClass;
    }

    public String getNoticeClass() {
        if (noticeClass == null) {
            FormTagSupport formTag = formTag();

            if (formTag != null)
                return formTag.getFieldNoticeClass();
        }

        return noticeClass;
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

        if (Str.isEmpty(fieldName) || errors == null || errors.isEmpty())
            return null;

        FormTagSupport formTag = formTag();
        Error error = errors.get(fieldName);
        String resolvedErrorMessage = null;

        if (error != null) {
            String errorMsgKey = null;

            if (formTag != null && !Errors.GLOBAL_ERROR_KEY.equals(fieldName)) {
                errorMsgKey = new StringBuilder(formTag.getName())
                        .append(Char.DOT).append(fieldName).append(Char.DOT).append(error.message()).toString();

                resolvedErrorMessage = messageResolver.resolve(errorMsgKey, requestContext(), true);
            }

            if (resolvedErrorMessage == null && !Errors.GLOBAL_ERROR_KEY.equals(fieldName)) {
                errorMsgKey = new StringBuilder(fieldName)
                        .append(Char.DOT).append(error.message()).toString();

                resolvedErrorMessage = messageResolver.resolve(errorMsgKey, requestContext(), true);
            }


            if (resolvedErrorMessage == null) {
                resolvedErrorMessage = messageResolver.resolve(error.message(), requestContext(), true);
            }

            String resolvedFieldName = null;

            if (label != null) {
                resolvedFieldName = label;
            } else if (!Errors.GLOBAL_ERROR_KEY.equals(fieldName)) {
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

            if (resolvedErrorMessage != null && args != null && args.length > 0)
                resolvedErrorMessage = MessageFormat.format(resolvedErrorMessage, args);

            if (resolvedErrorMessage == null && args != null && args.length > 0)
                resolvedErrorMessage = MessageFormat.format(error.message(), args);
        }

        return resolvedErrorMessage == null ? error.message() : resolvedErrorMessage;
    }

    protected String errorMessage(Error error) {
        if (error == null)
            return null;

        if (!Str.isEmpty(error.field()) && !Errors.GLOBAL_ERROR_KEY.equals(error.field()))
            return errorMessage(error.field());

        FormTagSupport formTag = formTag();
        String resolvedErrorMessage = null;

        String errorMsgKey = null;

        if (formTag != null) {
            errorMsgKey = new StringBuilder(formTag.getName())
                    .append(Char.DOT).append(error.message()).toString();

            resolvedErrorMessage = messageResolver.resolve(errorMsgKey, requestContext(), true);
        }

        if (resolvedErrorMessage == null) {
            resolvedErrorMessage = messageResolver.resolve(error.message(), requestContext(), true);
        }

        if (resolvedErrorMessage != null && error.args() != null && error.args().length > 0)
            resolvedErrorMessage = MessageFormat.format(resolvedErrorMessage, error.args());

        if (resolvedErrorMessage == null && error.args() != null && error.args().length > 0)
            resolvedErrorMessage = MessageFormat.format(error.message(), error.args());
        
        return resolvedErrorMessage == null ? error.message() : resolvedErrorMessage;
    }

    protected boolean hasNotice(String fieldName) {
        Notices notices = notices();
        return notices != null && notices.exist(fieldName);
    }

    protected String noticeMessage(String fieldName) {
        Notices notices = notices();

        if (Str.isEmpty(fieldName) || notices == null || notices.isEmpty())
            return null;

        FormTagSupport formTag = formTag();
        Notice notice = notices.get(fieldName);
        String resolvedNoticeMessage = null;

        if (notice != null) {
            String noticeMsgKey = null;

            if (formTag != null && !Notices.GLOBAL_NOTICE_KEY.equals(fieldName)) {
                noticeMsgKey = new StringBuilder(formTag.getName())
                        .append(Char.DOT).append(fieldName).append(Char.DOT).append(notice.message()).toString();

                resolvedNoticeMessage = messageResolver.resolve(noticeMsgKey, requestContext(), true);
            }

            if (resolvedNoticeMessage == null && !Notices.GLOBAL_NOTICE_KEY.equals(fieldName)) {
                noticeMsgKey = new StringBuilder(fieldName)
                        .append(Char.DOT).append(notice.message()).toString();

                resolvedNoticeMessage = messageResolver.resolve(noticeMsgKey, requestContext(), true);
            }

            if (resolvedNoticeMessage == null) {
                resolvedNoticeMessage = messageResolver.resolve(notice.message(), requestContext(), true);
            }

            if (resolvedNoticeMessage != null && notice.args() != null && notice.args().length > 0)
                resolvedNoticeMessage = MessageFormat.format(resolvedNoticeMessage, notice.args());

            if (resolvedNoticeMessage == null && notice.args() != null && notice.args().length > 0)
                resolvedNoticeMessage = MessageFormat.format(notice.message(), notice.args());
        }

        return resolvedNoticeMessage == null ? notice.message() : resolvedNoticeMessage;
    }

    protected String noticeMessage(Notice notice) {
        if (notice == null)
            return null;

        if (!Str.isEmpty(notice.field()) && !Notices.GLOBAL_NOTICE_KEY.equals(notice.field()))
            return noticeMessage(notice.field());

        FormTagSupport formTag = formTag();
        String resolvedNoticeMessage = null;

        String noticeMsgKey = null;

        if (formTag != null) {
            noticeMsgKey = new StringBuilder(formTag.getName())
                    .append(Char.DOT).append(notice.message()).toString();

            resolvedNoticeMessage = messageResolver.resolve(noticeMsgKey, requestContext(), true);
        }

        if (resolvedNoticeMessage == null) {
            resolvedNoticeMessage = messageResolver.resolve(notice.message(), requestContext(), true);
        }

        if (resolvedNoticeMessage != null && notice.args() != null && notice.args().length > 0)
            resolvedNoticeMessage = MessageFormat.format(resolvedNoticeMessage, notice.args());

        if (resolvedNoticeMessage == null && notice.args() != null && notice.args().length > 0)
            resolvedNoticeMessage = MessageFormat.format(notice.message(), notice.args());

        return resolvedNoticeMessage == null ? notice.message() : resolvedNoticeMessage;
    }

    protected String hint(String fieldName) {
        if (Str.isEmpty(fieldName))
            return null;

        FormTagSupport formTag = formTag();
        String resolvedHintMessage = null;

        String hintMsgKey = new StringBuilder(formTag.getName())
                .append(Char.DOT).append(fieldName).append(".hint").toString();

        resolvedHintMessage = messageResolver.resolve(hintMsgKey, requestContext(), true);

        if (resolvedHintMessage == null) {
            hintMsgKey = new StringBuilder(fieldName).append(".hint").toString();

            resolvedHintMessage = messageResolver.resolve(hintMsgKey, requestContext(), true);
        }

        return resolvedHintMessage;
    }

    protected FormTagSupport formTag() {
        GeemvcTagSupport currentTagSupport = this;
        JspTag parentTag = null;
        FormTagSupport formTagSupport = null;

        while ((parentTag = currentTagSupport.getParent()) != null) {
            if (parentTag instanceof FormTagSupport) {
                formTagSupport = (FormTagSupport) parentTag;
                break;
            } else if (parentTag instanceof GeemvcTagSupport) {
                currentTagSupport = (GeemvcTagSupport) parentTag;
            } else {
                break;
            }
        }

        return formTagSupport;
    }
}
