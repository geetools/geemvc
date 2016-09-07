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

package com.geemvc.helper;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.geemvc.Char;
import com.geemvc.Str;
import com.geemvc.annotation.Request;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class DefaultUriBuilder implements UriBuilder {

    protected Pattern pathParameterPattern = Pattern.compile("(\\{[a-zA-Z0-9]+:?[^\\{\\}]+\\})");

    @Inject
    protected Injector injector;

    @Override
    public String build(Class<?> controllerClass, Method handlerMethod) {
        return build(controllerClass, handlerMethod);
    }

    @Override
    public String build(Class<?> controllerClass, Method handlerMethod, Map<String, String[]> parameters) {
        String requestURI = buildRequestURI(controllerClass, handlerMethod);
        return insertParameters(requestURI, parameters);
    }

    protected String buildRequestURI(Class<?> controllerClass, Method handlerMethod) {
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

    protected String insertParameters(String requestURI, Map<String, String[]> parameters) {
        // No parameters exist in URI.
        if (requestURI.indexOf(Char.CURLY_BRACKET_OPEN) == -1)
            return requestURI;

        Matcher m = pathParameterPattern.matcher(requestURI);

        while (m.find()) {
            String origPram = m.group(0).trim();
            String param = origPram;

            if (param.indexOf(Char.COLON) != -1)
                param = param.substring(1, param.indexOf(Char.COLON)).trim();

            else
                param = param.substring(1, param.length() - 1).trim();

            String[] value = parameters.get(param);

            if (value == null || value.length == 0)
                throw new IllegalStateException("An attempt to populate parameters in the URI '" + requestURI + "' failed because the value for the parameter '" + param + "' could not be found.");

            // throw new IllegalStateException("The parameter '" + param + "' was found in the action path but no parameter could be found for it. You
            // can specify a parameter in the form-tag like this: <f:form action=\"/some/path/{" + param + "}\" p_"
            // + param + "=\"${" + param + "}\">.");

            requestURI = requestURI.replace(origPram, value[0]);
        }

        return requestURI;
    }

}
