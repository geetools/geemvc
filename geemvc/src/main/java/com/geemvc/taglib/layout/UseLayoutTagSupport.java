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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.geemvc.taglib.HtmlTagSupport;

public class UseLayoutTagSupport extends HtmlTagSupport {
    protected String layout;

    @Override
    public void doTag() throws JspException, IOException {
        try {
            // Layout context for storing processed sections.
            LayoutContext layoutCtx = new LayoutContext();

            layoutCtx.setAttributes(this.dynamicAttributes);

            // Adds the layout context to the request-scope so that the section tag can put its result into the
            // underlying map.
            PageContext pageContext = ((PageContext) jspContext);
            pageContext.setAttribute(LayoutContext.KEY, layoutCtx, PageContext.REQUEST_SCOPE);

            // Evaluates the body content of the "use" layout tag so that all the sections are processed. Here we
            // remember all the rendered sections in the layout-context for when the actual layout gets processed.
            getBodyContent();

            // Includes the layout and writes everything to the response. Sections that have been overridden in the
            // "use" layout tag will be rendered instead of the equivalent counterparts in the layout.
            pageContext.include(layout, false);

        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }
}
