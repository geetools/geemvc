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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class ErrorTagSupport extends FormFieldTagSupport {
    protected String name;

    @Override
    public void doTag() throws JspException, IOException {
        if (hasError(name)) {
            JspWriter writer = jspContext.getOut();

            writer.write("<small class=\"error");

            String classValue = (String) dynamicAttributes.get("class");

            if (classValue == null)
                classValue = getErrorClass();

            if (!Str.isEmpty(classValue)) {
                writer.write(Char.SPACE);
                writer.write(classValue);
            }

            writer.write("\">\n");

            writer.write(errorMessage(name));
            writer.write("</small>\n");
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
}
