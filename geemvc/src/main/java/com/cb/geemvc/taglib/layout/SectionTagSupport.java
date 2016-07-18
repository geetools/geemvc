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

package com.cb.geemvc.taglib.layout;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspTag;

import com.cb.geemvc.taglib.HtmlTagSupport;

public class SectionTagSupport extends HtmlTagSupport {
    protected String name;

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageCtx = ((PageContext) jspContext);
        LayoutContext layoutCtx = (LayoutContext) pageCtx.getAttribute(LayoutContext.KEY, PageContext.REQUEST_SCOPE);

        JspTag parentLayoutTag = getParent();

        if (parentLayoutTag == null)
            throw new IllegalStateException(
                    "The section tag must have a parent tag of either 'LayoutTagSupport' (<h:layout><h:section name=\"content\"></h:section></h:layout>) or 'UseLayoutTagSupport' (<h:use layout=\"layout.jsp\"><h:section name=\"content\"></h:section></h:use).");

        if (parentLayoutTag instanceof LayoutTagSupport) {
            // Checks if this section has previously already been rendered by the "use" layout tag, which overrides the
            // original layout version.
            if (layoutCtx.hasSection(name))
                pageCtx.getOut().write(layoutCtx.section(name));

                // No overridden version exists, so we just render the one from the original layout.
            else
                pageCtx.getOut().write(getBodyContent());
        }
        // When a section is being rendered from within the "use" layout tag, we simply remember the result for later
        // when it comes to rendering the complete layout. This version will have precedence over the original layout
        // version.
        else if (parentLayoutTag instanceof UseLayoutTagSupport) {
            layoutCtx.putSection(name, getBodyContent());
        } else {
            throw new IllegalStateException("The section tag can only have 'LayoutTagSupport' (<h:layout></h:layout>) or 'UseLayoutTagSupport' (<h:use layout=\"layout.jsp\"></h:use) as its parent tag.");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
