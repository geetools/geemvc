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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.cb.geemvc.Char;

public class InputImageTagSupport extends InputTagSupport {
    protected String src;

    public InputImageTagSupport() {
        dynamicAttributes.put("type", "image");
    }

    protected void appendTagAttributes(JspWriter writer) throws JspException {
        super.appendTagAttributes(writer);

        try {
            writer.write(Char.SPACE);
            writer.write("src");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write(getSrc());
            writer.write(Char.DOUBLE_QUOTE);
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
