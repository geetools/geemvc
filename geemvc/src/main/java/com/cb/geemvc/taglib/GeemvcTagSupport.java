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

package com.cb.geemvc.taglib;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;

import com.cb.geemvc.i18n.message.CompositeMessageResolver;
import com.cb.geemvc.i18n.notice.Notices;
import org.apache.commons.io.IOUtils;

import com.cb.geemvc.Char;
import com.cb.geemvc.RequestContext;
import com.cb.geemvc.Str;
import com.cb.geemvc.bind.PropertyNode;
import com.cb.geemvc.inject.Injectors;
import com.cb.geemvc.validation.Errors;
import com.cb.geemvc.view.GeemvcKey;
import com.google.common.base.CaseFormat;
import com.google.inject.Injector;

public class GeemvcTagSupport implements SimpleTag {
    protected String tagName = null;

    protected boolean hasTagBody;

    protected Map<String, Object> dynamicAttributes = new LinkedHashMap<>();

    protected String var;

    protected String scope = "page";

    protected boolean escapeHTML = false;

    protected boolean escapeJavascript = false;

    protected boolean escapeJson = false;

    protected boolean unescapeHTML = false;

    protected boolean unescapeJavascript = false;

    protected boolean unescapeJson = false;

    protected JspTag parent;

    protected JspContext jspContext;

    protected JspFragment jspBody;

    protected final Injector injector;

    protected final CompositeMessageResolver messageResolver;

    protected String ELEMENT_ID_PREFIX = "el-";

    protected static final String REQUEST_SCOPE = "request";

    protected static final String SESSION_SCOPE = "session";

    public GeemvcTagSupport() {
        this.injector = Injectors.provide();
        this.messageResolver = injector.getInstance(CompositeMessageResolver.class);
    }

    protected void writeTag(JspWriter writer, String tagName) throws JspException {
        writeTag(writer, tagName, false, true);
    }

    protected void writeTag(JspWriter writer, String tagName, boolean hasTagBody) throws JspException {
        writeTag(writer, tagName, hasTagBody, true);
    }

    protected void writeTag(JspWriter writer, String tagName, boolean hasTagBody, boolean closeTag) throws JspException {
        this.tagName = tagName;
        this.hasTagBody = hasTagBody;

        try {
            writer.write(Char.LESS_THAN);
            writer.write(tagName);
            appendTagAttributes(writer);
            writeTagAttributes(writer);

            if (hasTagBody) {
                writer.write(Char.GREATER_THAN);
                appendTagBody(writer);
            }

            if (closeTag)
                writeCloseTag(writer, tagName, hasTagBody);
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    protected void writeCloseTag(JspWriter writer, String tagName, boolean hasTagBody) throws JspException {
        try {
            if (hasTagBody) {
                writer.write(Char.LESS_THAN);
                writer.write(Char.SLASH);
                writer.write(tagName);
                writer.write(Char.GREATER_THAN);
            } else {
                writer.write(Char.SPACE);
                writer.write(Char.SLASH);
                writer.write(Char.GREATER_THAN);
            }
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    protected void appendTagBody(JspWriter writer) throws JspException {
        try {
            if (jspBody != null)
                jspBody.invoke(writer);
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    protected void appendTagAttributes(JspWriter writer) throws JspException {
        // Implement me.
    }

    protected void writeTagAttributes(JspWriter writer) throws JspException {
        Set<Entry<String, Object>> attributes = dynamicAttributes.entrySet();

        try {
            for (Entry<String, Object> attr : attributes) {
                writer.write(Char.SPACE);
                writer.write(attr.getKey());
                writer.write(Char.EQUALS);
                writer.write(Char.DOUBLE_QUOTE);
                writer.write(String.valueOf(attr.getValue()));
                writer.write(Char.DOUBLE_QUOTE);
            }
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    public Object predefinedValue(String name) {
        // First try the standard way for simple (none-bean) values.
        Object value = attribute(name);

        if (value != null)
            return value;

        String normalizedName = normalizeName(name);

        // Try again with normalized name before we attempt the more complex bean retrieval.
        if (name.indexOf(Char.DOT) == -1 && !Str.isEmpty(normalizedName)) {
            value = attribute(normalizedName);

            if (value != null)
                return value;
        }

        if (name.indexOf(Char.DOT) != -1 || name.indexOf(Char.SQUARE_BRACKET_OPEN) != -1) {
            Object beanInstance = attribute(normalizedName);

            String beanPropertyName = validExpression(name);
            Object val = beanPropertyValue(Str.isEmpty(beanPropertyName) ? normalizedName : beanPropertyName, beanInstance);

            return val;
        }

        return null;
    }

    protected String normalizeName(String expression) {
        int dotPos = expression.indexOf(Char.DOT);
        int squareBracketPos = expression.indexOf(Char.SQUARE_BRACKET_OPEN);

        // Seems to be a simple property name.
        if (dotPos == -1 && squareBracketPos == -1)
            return expression;

        int pos = dotPos != -1 && squareBracketPos != -1 ? Math.min(dotPos, squareBracketPos) : dotPos == -1 ? squareBracketPos : dotPos;

        return expression.substring(0, pos);
    }

    protected String validExpression(String expression) {
        int dotPos = expression.indexOf(Char.DOT);
        int squareBracketPos = expression.indexOf(Char.SQUARE_BRACKET_CLOSE);

        // Seems to be a simple property name.
        if (dotPos == -1 && squareBracketPos == -1)
            return expression;

        int pos = dotPos != -1 && squareBracketPos != -1 ? Math.min(dotPos, squareBracketPos) : dotPos == -1 ? squareBracketPos : dotPos;

        return expression.substring(pos + 1);
    }

    protected Object attribute(String name) {
        Object bean = jspContext.getAttribute(name, PageContext.PAGE_SCOPE);

        if (bean == null)
            bean = jspContext.getAttribute(name, PageContext.REQUEST_SCOPE);

        if (bean == null)
            bean = jspContext.getAttribute(name, PageContext.SESSION_SCOPE);

        return bean;
    }

    protected Object beanPropertyValue(String expression, Object beanInstance) {
        if (expression == null || beanInstance == null)
            return null;

        Injector injector = Injectors.provide();

        PropertyNode propertyNode = injector.getInstance(PropertyNode.class).build(expression, beanInstance.getClass());

        return propertyNode.get(beanInstance);
    }

    @Override
    public void doTag() throws JspException, IOException {

    }

    protected String toElementId(String name) {
        return toElementId(name, Str.EMPTY);
    }

    protected String toElementId(String name, String idSuffix) {
        StringBuilder id = new StringBuilder(ELEMENT_ID_PREFIX).append(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, name).replace(Char.SQUARE_BRACKET_OPEN, Char.HYPHEN).replace(Char.SQUARE_BRACKET_CLOSE, Char.HYPHEN)
                .replace(Char.DOT, Char.HYPHEN).replace(Char.SPACE, Char.HYPHEN).replace(Char.UNDERSCORE, Char.HYPHEN).replace(Str.HYPHEN_2x, Str.HYPHEN));

        if (id.charAt(id.length() - 1) == Char.HYPHEN)
            id.deleteCharAt(id.length() - 1);

        if (!Str.isEmpty(idSuffix)) {
            id.append(Char.HYPHEN).append(
                    idSuffix.toLowerCase().replace(Char.SQUARE_BRACKET_OPEN, Char.HYPHEN).replace(Char.SQUARE_BRACKET_CLOSE, Char.HYPHEN).replace(Char.DOT, Char.HYPHEN).replace(Char.SPACE, Char.HYPHEN).replace(Char.UNDERSCORE, Char.HYPHEN)
                            .replace(Str.HYPHEN_2x, Str.HYPHEN));
        }

        if (id.charAt(id.length() - 1) == Char.HYPHEN)
            id.deleteCharAt(id.length() - 1);

        return id.toString();
    }

    protected String getBodyContent() {
        StringWriter bodyWriter = new StringWriter();

        try {
            if (jspBody != null)
                jspBody.invoke(bodyWriter);

            return bodyWriter.toString();
        } catch (Throwable t) {
            throw new IllegalStateException(t);
        } finally {
            IOUtils.closeQuietly(bodyWriter);
        }
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    protected int scope() {
        if (scope == null)
            return PageContext.PAGE_SCOPE;

        switch (scope.toLowerCase().trim()) {
            case REQUEST_SCOPE:
                return PageContext.REQUEST_SCOPE;
            case SESSION_SCOPE:
                return PageContext.SESSION_SCOPE;
            default:
                return PageContext.PAGE_SCOPE;
        }
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isEscapeHTML() {
        return escapeHTML;
    }

    public void setEscapeHTML(boolean escapeHTML) {
        this.escapeHTML = escapeHTML;
    }

    public boolean isEscapeJavascript() {
        return escapeJavascript;
    }

    public void setEscapeJavascript(boolean escapeJavascript) {
        this.escapeJavascript = escapeJavascript;
    }

    public boolean isEscapeJson() {
        return escapeJson;
    }

    public void setEscapeJson(boolean escapeJson) {
        this.escapeJson = escapeJson;
    }

    public boolean isUnescapeHTML() {
        return unescapeHTML;
    }

    public void setUnescapeHTML(boolean unescapeHTML) {
        this.unescapeHTML = unescapeHTML;
    }

    public boolean isUnescapeJavascript() {
        return unescapeJavascript;
    }

    public void setUnescapeJavascript(boolean unescapeJavascript) {
        this.unescapeJavascript = unescapeJavascript;
    }

    public boolean isUnescapeJson() {
        return unescapeJson;
    }

    public void setUnescapeJson(boolean unescapeJson) {
        this.unescapeJson = unescapeJson;
    }

    @Override
    public void setParent(JspTag parent) {
        this.parent = parent;
    }

    @Override
    public JspTag getParent() {
        return parent;
    }

    @Override
    public void setJspContext(JspContext jspContext) {
        this.jspContext = jspContext;
    }

    @Override
    public void setJspBody(JspFragment jspBody) {
        this.jspBody = jspBody;
    }

    public Class<?> controllerClass() {
        return (Class<?>) jspContext.getAttribute(GeemvcKey.CONTROLLER_CLASS, PageContext.REQUEST_SCOPE);
    }

    public Method handlerMethod() {
        return (Method) jspContext.getAttribute(GeemvcKey.HANDLER_METHOD, PageContext.REQUEST_SCOPE);
    }

    public RequestContext requestContext() {
        return (RequestContext) jspContext.getAttribute(GeemvcKey.REQUEST_CONTEXT, PageContext.REQUEST_SCOPE);
    }

    public Errors validationErrors() {
        return (Errors) jspContext.getAttribute(GeemvcKey.VALIDATION_ERRORS, PageContext.REQUEST_SCOPE);
    }

    public Notices notices() {
        return (Notices) jspContext.getAttribute(GeemvcKey.NOTICES, PageContext.REQUEST_SCOPE);
    }
}
