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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.geemvc.Char;
import com.google.inject.Singleton;

@Singleton
public class DefaultRequests implements Requests {
    protected String SCHEME_HTTP = "http://";
    protected String SCHEME_HTTPS = "https://";

    @Override
    public String toRequestURL(String path, boolean isHttps, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        StringBuilder requestURL = new StringBuilder();

        boolean isRelative = isHttps && httpRequest.isSecure() || !isHttps && !httpRequest.isSecure();

        if (!isRelative) {
            requestURL.append(isHttps ? SCHEME_HTTPS : SCHEME_HTTP).append(httpRequest.getServerName());

            if (httpRequest.getServerPort() != 80)
                requestURL.append(Char.COLON).append(httpRequest.getServerPort());
        }

        if (httpRequest.getContextPath() != null)
            requestURL.append(httpRequest.getContextPath());

        if (httpRequest.getServletPath() != null && !httpRequest.getRequestURI().equals(httpRequest.getServletPath()))
            requestURL.append(httpRequest.getServletPath());

        return requestURL.append(path).toString();
    }
}
