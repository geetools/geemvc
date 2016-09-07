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
import com.geemvc.annotation.Request;
import com.geemvc.handler.CompositeHandlerResolver;
import com.geemvc.handler.RequestHandler;
import com.geemvc.handler.RequestMappingKey;
import com.geemvc.helper.Annotations;
import com.geemvc.helper.Controllers;
import com.geemvc.helper.Requests;
import com.geemvc.reflect.ReflectionProvider;
import com.geemvc.taglib.HtmlTagSupport;
import com.google.common.base.CaseFormat;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormTagSupport extends HtmlTagSupport {
    protected String name;

    protected Class<?> controllerClass;

    protected String handler;

    protected String action;

    protected String method;

    protected Boolean https = null;

    protected String fieldGroupClass;

    protected String fieldLabelClass;

    protected String fieldWrapperClass;

    protected String fieldClass;

    protected String fieldHintClass;

    protected String fieldErrorClass;

    protected String fieldNoticeClass;

    protected boolean fieldErrors = true;

    protected boolean fieldNotices = true;

    protected Pattern pathParameterPattern = Pattern.compile("(\\{[a-zA-Z0-9]+:?[^\\{\\}]+\\})");

    @Override
    public void doTag() throws JspException {
        writeTag(jspContext.getOut(), "form", true);
    }

    protected void appendTagAttributes(JspWriter writer) throws JspException {
        try {
            String name = getName();
            String id = getId();

            if (action == null && handler == null && name == null)
                throw new IllegalArgumentException(
                        "You must either specify the action attribute (<f:form action=\"/my/path\">), a target handler method (<f:form handler=\"myHandler\">) or a unique handler-name (<f:form name=\"my-unique-handler-name\">) when using the form tag.");

            if (id == null)
                id = toElementId(handlerMethod().getName());

            writer.write(Char.SPACE);
            writer.write("id");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write(id);
            writer.write(Char.DOUBLE_QUOTE);

            writer.write(Char.SPACE);
            writer.write("name");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write(name);
            writer.write(Char.DOUBLE_QUOTE);

            if (action == null) {
                if (handler != null)
                    action = toActionURI(getControllerClass(), handler);

                else if (name != null)
                    action = toActionURI(name);
            } else {
                ensureMatchingHandlerExists(action);
            }

            if (action == null)
                throw new JspException("No action path could be found or automatically composed. Check your form attributes.");

            // Action URI may contain parameters.
            if (action.indexOf(Char.CURLY_BRACKET_OPEN) != -1) {
                Map<String, String> pathParameters = pathParameters();
                action = insertParameters(action, pathParameters);
            }

            ServletRequest request = ((PageContext) jspContext).getRequest();

            writer.write(Char.SPACE);
            writer.write("action");
            writer.write(Char.EQUALS);
            writer.write(Char.DOUBLE_QUOTE);
            writer.write(injector.getInstance(Requests.class).toRequestURL(action, https == null ? request.isSecure() : https, request));
            writer.write(Char.DOUBLE_QUOTE);

            if (!Str.isEmpty(method)) {
                writer.write(Char.SPACE);
                writer.write("method");
                writer.write(Char.EQUALS);
                writer.write(Char.DOUBLE_QUOTE);
                writer.write(method);
                writer.write(Char.DOUBLE_QUOTE);
            }
        } catch (Throwable t) {
            throw new JspException(t);
        }
    }

    protected Map<String, String> pathParameters() {
        Map<String, String> pathParameters = null;

        Set<String> keysToRemove = null;

        if (!dynamicAttributes.isEmpty()) {
            pathParameters = new LinkedHashMap<>();
            keysToRemove = new HashSet<>();

            Set<Entry<String, Object>> entries = dynamicAttributes.entrySet();

            for (Entry<String, Object> entry : entries) {
                if (entry.getKey().startsWith("p_")) {
                    pathParameters.put(entry.getKey().substring(2), String.valueOf(entry.getValue()));
                    keysToRemove.add(entry.getKey());
                }
            }

            // Remove dynamic parameters so that they do not end up in the form tag.
            for (String key : keysToRemove) {
                dynamicAttributes.remove(key);
            }
        }

        return pathParameters;
    }

    protected String insertParameters(String actionURI, Map<String, String> pathParameters) throws JspException {
        // No parameters exist in URI.
        if (actionURI.indexOf(Char.CURLY_BRACKET_OPEN) == -1)
            return actionURI;

        Matcher m = pathParameterPattern.matcher(actionURI);

        while (m.find()) {
            String origPram = m.group(0).trim();
            String param = origPram;

            if (param.indexOf(Char.COLON) != -1)
                param = param.substring(1, param.indexOf(Char.COLON)).trim();

            else
                param = param.substring(1, param.length() - 1).trim();

            String value = pathParameters.get(param);

            if (value == null)
                throw new JspException("The parameter '" + param + "' was found in the action path but no parameter could be found for it. You can specify a parameter in the form-tag like this: <f:form action=\"/some/path/{" + param + "}\" p_"
                        + param + "=\"${" + param + "}\">.");

            actionURI = actionURI.replace(origPram, value);
        }

        return actionURI;
    }

    protected void ensureMatchingHandlerExists(String actionPath) throws JspException {
        CompositeHandlerResolver handlerResolver = injector.getInstance(CompositeHandlerResolver.class);
        List<RequestHandler> requestHandlers = handlerResolver.resolve(actionPath, getMethod());

        if (requestHandlers.size() == 0)
            throw new JspException("Request-handler method '" + handler + "' not found for action '" + actionPath + "' and method '" + getMethod() + "'");

        if (requestHandlers.size() > 1)
            throw new JspException("More than 1 method found for action '" + actionPath + "' and method '" + getMethod() + "'. If it is not possible to provide a handler with a unique path and http-method you can use a unique name instead.");
    }

    protected String toActionURI(String handlerName) throws JspException {
        ReflectionProvider reflectionProvider = injector.getInstance(ReflectionProvider.class);

        Set<Method> handlerMethods = reflectionProvider.locateHandlerMethods(m -> handlerName.equals(m.getAnnotation(Request.class).name()));

        if (handlerMethods.size() == 0)
            throw new JspException("No request-handler method found having the name '" + name + "'. Make sure that a request handler exists that has been annotated with the unique name - e.g. @Request(name=\"" + name + "\").");

        if (handlerMethods.size() > 1)
            throw new JspException("More than 1 request-handler method found for the name '" + name + "'. You must either provide a 'unique' name [ @Request(name=\"" + name + "\") ] or manually specify the path using the 'action' attribute.");

        Method handlerMethod = handlerMethods.iterator().next();

        return buildActionURI(handlerMethod.getDeclaringClass(), handlerMethod);
    }

    protected String toActionURI(Class<?> controllerClass, String handlerMethodName) throws JspException {
        if (this.controllerClass != null && controllerClass == null)
            throw new JspException("Unable to locate controller class '" + controllerClass + "'.");

        ReflectionProvider reflectionProvider = injector.getInstance(ReflectionProvider.class);

        Map<RequestMappingKey, Method> requestHandlerMap = reflectionProvider.getRequestHandlerMethods(controllerClass, e -> handler.equals(e.getValue().getName()));

        if (requestHandlerMap.size() == 0)
            throw new JspException("Request-handler method '" + handler + "' not found in controller '" + controllerClass.getName() + "'.");

        if (requestHandlerMap.size() > 1)
            throw new JspException("More than 1 request-handler found for method name '" + handler + "' in controller '" + controllerClass.getName()
                    + "'. You must either provide a unique handler method name or manually specify the path using the 'action' attribute.");

        return buildActionURI(controllerClass, requestHandlerMap.values().iterator().next());
    }

    protected String buildActionURI(Class<?> controllerClass, Method handlerMethod) throws JspException {
        Controllers controllers = injector.getInstance(Controllers.class);
        Annotations annotations = injector.getInstance(Annotations.class);

        String basePath = controllers.getBasePath(controllerClass);
        String path = annotations.path(handlerMethod.getAnnotation(Request.class));

        if (basePath == null)
            basePath = Str.EMPTY;

        if (path == null)
            path = Str.EMPTY;

        StringBuilder combinedPath = new StringBuilder(basePath);

        if (!basePath.endsWith(Str.SLASH) && !path.startsWith(Str.SLASH))
            combinedPath.append(Char.SLASH);

        combinedPath.append(path);

        if (combinedPath.charAt(0) != Char.SLASH)
            combinedPath.insert(0, Char.SLASH);

        if (combinedPath.charAt(combinedPath.length() - 1) == Char.SLASH) {
            // Only delete last slash if it is not the only character in the request-URI (i.e. in index page).
            if (combinedPath.length() > 1)
                combinedPath.deleteCharAt(combinedPath.length() - 1);
        }

        return combinedPath.toString();
    }

    public String getName() {
        if (name == null)
            name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, handlerMethod().getName());

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getControllerClass() throws ClassNotFoundException {
        return controllerClass == null ? controllerClass() : controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public String getController() {
        return this.controllerClass == null ? null : this.controllerClass.getName();
    }

    public void setController(String controllerFQN) throws ClassNotFoundException {
        this.controllerClass = Class.forName(controllerFQN);
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMethod() {
        return method == null ? null : method.toUpperCase();
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getHttps() {
        return https;
    }

    public void setHttps(Boolean https) {
        this.https = https;
    }

    public String getFieldGroupClass() {
        return fieldGroupClass;
    }

    public void setFieldGroupClass(String fieldGroupClass) {
        this.fieldGroupClass = fieldGroupClass;
    }

    public String getFieldLabelClass() {
        return fieldLabelClass;
    }

    public void setFieldLabelClass(String fieldLabelClass) {
        this.fieldLabelClass = fieldLabelClass;
    }

    public String getFieldWrapperClass() {
        return fieldWrapperClass;
    }

    public void setFieldWrapperClass(String fieldWrapperClass) {
        this.fieldWrapperClass = fieldWrapperClass;
    }

    public String getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(String fieldClass) {
        this.fieldClass = fieldClass;
    }

    public String getFieldHintClass() {
        return fieldHintClass;
    }

    public void setFieldHintClass(String fieldHintClass) {
        this.fieldHintClass = fieldHintClass;
    }

    public String getFieldErrorClass() {
        return fieldErrorClass;
    }

    public void setFieldErrorClass(String fieldErrorClass) {
        this.fieldErrorClass = fieldErrorClass;
    }

    public String getFieldNoticeClass() {
        return fieldNoticeClass;
    }

    public void setFieldNoticeClass(String fieldNoticeClass) {
        this.fieldNoticeClass = fieldNoticeClass;
    }

    public boolean isFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(boolean fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public boolean isFieldNotices() {
        return fieldNotices;
    }

    public void setFieldNotices(boolean fieldNotices) {
        this.fieldNotices = fieldNotices;
    }
}
