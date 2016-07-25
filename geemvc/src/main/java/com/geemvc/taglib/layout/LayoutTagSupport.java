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

package com.geemvc.taglib.layout;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.geemvc.taglib.HtmlTagSupport;

public class LayoutTagSupport extends HtmlTagSupport {
    @Override
    public void doTag() throws JspException, IOException {
        // Simply gets the body content and writes the result to the response. The section tag itself decides whether
        // the original layout version is rendered or the overridden "use" layout one.

        // Set attributes that may have been added dynamically to the "use" layout tag.
        setAttributes();

        String layout = getBodyContent();
        jspContext.getOut().write(layout);
    }

    protected void setAttributes() {
        PageContext pageCtx = ((PageContext) jspContext);
        LayoutContext layoutCtx = (LayoutContext) pageCtx.getAttribute(LayoutContext.KEY, PageContext.REQUEST_SCOPE);

        Map<String, Object> useLayoutAttributes = layoutCtx.getAttributes();

        for (Entry<String, Object> entry : useLayoutAttributes.entrySet()) {
            pageCtx.setAttribute(entry.getKey(), entry.getValue());
        }
    }
}