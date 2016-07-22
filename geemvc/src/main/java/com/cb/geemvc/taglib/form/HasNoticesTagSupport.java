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

import com.cb.geemvc.i18n.notice.Notice;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Set;

public class HasNoticesTagSupport extends NoticesTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        Set<Notice> noticeSet = noticeSet();

        if (noticeSet != null && !noticeSet.isEmpty()) {
            appendTagBody(jspContext.getOut());
        }
    }

    @Override
    protected void appendTagAttributes(JspWriter writer) throws JspException {
    }

    @Override
    protected void appendTagBody(JspWriter writer) throws JspException {
        try {
            if (jspBody != null)
                jspBody.invoke(writer);
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }
}