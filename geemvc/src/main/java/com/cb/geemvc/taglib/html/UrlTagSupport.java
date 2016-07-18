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

package com.cb.geemvc.taglib.html;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.cb.geemvc.Char;
import com.cb.geemvc.Str;
import com.cb.geemvc.annotation.Request;
import com.cb.geemvc.handler.CompositeHandlerResolver;
import com.cb.geemvc.handler.RequestHandler;
import com.cb.geemvc.handler.RequestMappingKey;
import com.cb.geemvc.helper.Annotations;
import com.cb.geemvc.helper.Controllers;
import com.cb.geemvc.helper.Requests;
import com.cb.geemvc.reflect.ReflectionProvider;
import com.cb.geemvc.taglib.HtmlTagSupport;
import com.google.common.base.CaseFormat;

public class UrlTagSupport extends HtmlTagSupport {
    protected String name;

    protected Class<?> controllerClass;

    protected String handler;

    protected String value;

    protected String method;

    protected Boolean tag = null;

    protected Boolean https = null;

    protected Pattern pathParameterPattern = Pattern.compile("(\\{[a-zA-Z0-9]+:?[^\\{\\}]+\\})");

    protected final Requests requests;

    public UrlTagSupport() {
        requests = injector.getInstance(Requests.class);
    }

    @Override
    public void doTag() throws JspException {

        // Write tag or just output the URL?
        if (getTag() == true)
            writeTag(jspContext.getOut(), "a", true);

        else
            appendTagAttributes(jspContext.getOut());
    }

    protected void appendTagAttributes(JspWriter writer) throws JspException {
        try {
            String name = getName();
            String id = getId();

            if (value == null && handler == null && name == null)
                throw new IllegalArgumentException(
                        "You must either specify the value attribute (<h:url value=\"/my/path\">), a target handler method (<h:url handler=\"myHandler\">) or a unique handler-name (<h:url name=\"my-unique-handler-name\">) when using the url tag.");

            // Only write attributes when generating a tag.
            if (getTag() == true) {

                if (name == null)
                    name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, handlerMethod().getName());

                if (id == null)
                    id = toElementId(handlerMethod().getName());

                writer.write(Char.SPACE);
                writer.write("id");
                writer.write(Char.EQUALS);
                writer.write(Char.DOUBLE_QUOTE);
                writer.write(id);
                writer.write(Char.DOUBLE_QUOTE);
            }

            if (value == null) {
                if (handler != null)
                    value = toURI(getControllerClass(), handler);

                else if (name != null)
                    value = toURI(name);
            } else {
                ensureMatchingHandlerExists(value);
            }

            if (value == null)
                throw new JspException("No url could be found or automatically composed. Check your tag definition.");

            // URI may contain parameters.
            if (value.indexOf(Char.CURLY_BRACKET_OPEN) != -1) {
                Map<String, String> pathParameters = pathParameters();
                value = insertParameters(value, pathParameters);
            }

            ServletRequest request = ((PageContext) jspContext).getRequest();
            String url = injector.getInstance(Requests.class).toRequestURL(value, https == null ? request.isSecure() : https, request);

            if (!Str.isEmpty(var)) {
                jspContext.setAttribute(var, url, scope());
            } else {
                // Only output the href attribute when a tag is to be generated.
                if (getTag() == true) {
                    writer.write(Char.SPACE);
                    writer.write("href");
                    writer.write(Char.EQUALS);
                    writer.write(Char.DOUBLE_QUOTE);
                    writer.write(url);
                    writer.write(Char.DOUBLE_QUOTE);
                } else {
                    writer.write(url);
                }
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

    protected String insertParameters(String uri, Map<String, String> pathParameters) throws JspException {
        // No parameters exist in URI.
        if (uri.indexOf(Char.CURLY_BRACKET_OPEN) == -1)
            return uri;

        Matcher m = pathParameterPattern.matcher(uri);

        while (m.find()) {
            String origPram = m.group(0).trim();
            String param = origPram;

            if (param.indexOf(Char.COLON) != -1)
                param = param.substring(1, param.indexOf(Char.COLON)).trim();

            else
                param = param.substring(1, param.length() - 1).trim();

            String value = pathParameters.get(param);

            if (value == null)
                throw new JspException("The parameter '" + param + "' was found in the uri but no parameter could be found for it. You can specify a parameter in the url-tag like this: <h:url value=\"/some/path/{" + param + "}\" p_" + param
                        + "=\"${" + param + "}\">.");

            uri = uri.replace(origPram, value);
        }

        return uri;
    }

    protected void ensureMatchingHandlerExists(String uri) throws JspException {
        CompositeHandlerResolver handlerResolver = injector.getInstance(CompositeHandlerResolver.class);
        List<RequestHandler> requestHandlers = handlerResolver.resolve(uri, getMethod());

        if (requestHandlers.size() == 0)
            throw new JspException("Request-handler method '" + handler + "' not found for uri '" + uri + "' and method '" + getMethod() + "'");

        if (requestHandlers.size() > 1)
            throw new JspException("More than 1 method found for uri '" + uri + "' and method '" + getMethod() + "'. If it is not possible to provide a handler with a unique path and http-method you can use a unique name instead.");
    }

    protected String toURI(String handlerName) throws JspException {
        ReflectionProvider reflectionProvider = injector.getInstance(ReflectionProvider.class);

        Set<Method> handlerMethods = reflectionProvider.locateHandlerMethods(m -> handlerName.equals(m.getAnnotation(Request.class).name()));

        if (handlerMethods.size() == 0)
            throw new JspException("No request-handler method found having the name '" + name + "'. Make sure that a request handler exists that has been annotated with the unique name - e.g. @Request(name=\"" + name + "\").");

        if (handlerMethods.size() > 1)
            throw new JspException("More than 1 request-handler method found for the name '" + name + "'. You must either provide a 'unique' name [ @Request(name=\"" + name + "\") ] or manually specify the uri using the 'value' attribute.");

        Method handlerMethod = handlerMethods.iterator().next();

        return buildURI(handlerMethod.getDeclaringClass(), handlerMethod);
    }

    protected String toURI(Class<?> controllerClass, String handlerMethodName) throws JspException {
        if (this.controllerClass != null && controllerClass == null)
            throw new JspException("Unable to locate controller class '" + controllerClass + "'.");

        ReflectionProvider reflectionProvider = injector.getInstance(ReflectionProvider.class);

        Map<RequestMappingKey, Method> requestHandlerMap = reflectionProvider.getRequestHandlerMethods(controllerClass, e -> handler.equals(e.getValue().getName()));

        if (requestHandlerMap.size() == 0)
            throw new JspException("Request-handler method '" + handler + "' not found in controller '" + controllerClass.getName() + "'.");

        if (requestHandlerMap.size() > 1)
            throw new JspException("More than 1 request-handler found for method name '" + handler + "' in controller '" + controllerClass.getName()
                    + "'. You must either provide a unique handler method name or manually specify the uri using the 'value' attribute.");

        return buildURI(controllerClass, requestHandlerMap.values().iterator().next());
    }

    protected String buildURI(Class<?> controllerClass, Method handlerMethod) throws JspException {
        Controllers controllers = injector.getInstance(Controllers.class);
        Annotations annotations = injector.getInstance(Annotations.class);

        String basePath = controllers.getBasePath(controllerClass);
        String[] paths = annotations.paths(handlerMethod.getAnnotation(Request.class));
        String path = paths == null || paths.length == 0 ? null : paths[0];

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMethod() {
        return method == null ? null : method.toUpperCase();
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getTag() {
        return tag == null ? true : tag.booleanValue();
    }

    public void setTag(Boolean tag) {
        this.tag = tag;
    }

    public Boolean getHttps() {
        return https;
    }

    public void setHttps(Boolean https) {
        this.https = https;
    }
}
