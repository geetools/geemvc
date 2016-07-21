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

import com.cb.geemvc.Str;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class ErrorsTagSupport extends FormFieldTagSupport {
    protected String name;

    @Override
    public void doTag() throws JspException, IOException {

        validationErrors();

        if (hasError(name)) {
            String errorMsg = errorMessage(name);

            if (!Str.isEmpty(errorMsg))
                jspContext.getOut().write(errorMsg);
        }
    }

    @Override
    protected void appendTagAttributes(JspWriter writer) throws JspException {
    }

    @Override
    protected void appendTagBody(JspWriter writer) throws JspException {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
