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
import com.cb.geemvc.validation.Error;
import com.cb.geemvc.validation.Errors;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Set;

public class ErrorsTagSupport extends FormFieldTagSupport {
    protected String show;

    protected static final String SHOW_ALL = "all";
    protected static final String SHOW_FIELDS = "fields";
    protected static final String SHOW_GLOBAL = "global";

    protected static final String ERRORS_CONTAINER_KEY = "validation.errors.html.container";
    protected static final String ERRORS_MESSAGE_KEY = "validation.errors.html.message";

    @Override
    public void doTag() throws JspException, IOException {
        Errors errors = validationErrors();

        if (errors != null && !errors.isEmpty()) {
            Set<Error> errorSet = errors();

            if (errorSet != null && !errorSet.isEmpty()) {
                String containerHTML = null;
                String messageHTML = null;

                FormTagSupport formTag = formTag();

                if (formTag != null && formTag.getName() != null) {
                    // Get the HTML container to display the messages in.
                    String containerMsgKey = new StringBuilder(formTag.getName())
                            .append(Char.DOT).append(ERRORS_CONTAINER_KEY).toString();

                    containerHTML = messageResolver.resolve(containerMsgKey, requestContext(), true);

                    // Get the HTML message row to display the message in.
                    String messageMsgKey = new StringBuilder(formTag.getName())
                            .append(Char.DOT).append(ERRORS_MESSAGE_KEY).toString();

                    messageHTML = messageResolver.resolve(messageMsgKey, requestContext(), true);
                }

                // Get the standard HTML container if form specific one does not exist.
                if (containerHTML == null) {
                    containerHTML = messageResolver.resolve(ERRORS_CONTAINER_KEY, requestContext());
                }

                // Get the standard HTML message row if form specific one does not exist.
                if (messageHTML == null) {
                    messageHTML = messageResolver.resolve(ERRORS_MESSAGE_KEY, requestContext());
                }

                StringBuilder errorMessages = new StringBuilder();

                // Iterate though all errors and append them all together in a StringBuilder.
                for (Error error : errorSet) {
                    String resolvedErrorMsg = errorMessage(error);

                    if (!Str.isEmpty(resolvedErrorMsg)) {
                        errorMessages.append(String.format(messageHTML, resolvedErrorMsg)).append(Char.NEWLINE);
                    }
                }

                // Now add resolved messages to the error container.
                if (errorMessages.length() > 0) {
                    String html = String.format(containerHTML, errorMessages.toString());
                    jspContext.getOut().write(html);
                }
            }
        }
    }

    protected Set<Error> errors() {
        Errors errors = validationErrors();
        Set<Error> errorSet = null;

        if (errors != null && !errors.isEmpty()) {
            if (isShowAll()) {
                errorSet = errors.allErrors();
            } else if (isShowFields()) {
                errorSet = errors.fieldErrors();
            } else if (isShowGlobal()) {
                errorSet = errors.globalErrors();
            }
        }

        return errorSet;
    }

    protected boolean isShowAll() {
        return Str.trimEqualsIgnoreCase(SHOW_ALL, show);
    }

    protected boolean isShowFields() {
        return Str.trimEqualsIgnoreCase(SHOW_FIELDS, show);
    }

    protected boolean isShowGlobal() {
        return show == null || Str.trimEqualsIgnoreCase(SHOW_GLOBAL, show);
    }

    @Override
    protected void appendTagAttributes(JspWriter writer) throws JspException {
    }

    @Override
    protected void appendTagBody(JspWriter writer) throws JspException {
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
