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

package com.geemvc.rest.jaxrs.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.geemvc.Char;
import com.geemvc.Str;
import com.geemvc.ThreadStash;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultHttpHeaders implements HttpHeaders {

    protected final HttpServletRequest servletRequest;

    @Inject
    protected Injector injector;

    public DefaultHttpHeaders() {
        this.servletRequest = (HttpServletRequest) ThreadStash.get(ServletRequest.class);
    }

    @Override
    public List<String> getRequestHeader(String name) {
        return Collections.list(servletRequest.getHeaders(name));
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        MultivaluedMap mm = injector.getInstance(MultivaluedMap.class);
        Enumeration<String> headerNames = servletRequest.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = (String) headerNames.nextElement();
                List<String> values = getRequestHeader(name);

                for (String val : values) {
                    mm.add(name, val);
                }
            }
        }

        return mm;
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        List<MediaType> acceptMediaTypes = new ArrayList<>();
        List<String> acceptTypes = getRequestHeader("Accept");

        for (String accept : acceptTypes) {
            acceptMediaTypes.add(MediaType.valueOf(accept));
        }

        return acceptMediaTypes;
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        String acceptLangHeader = servletRequest.getHeader("Accept-Language");

        if (!Str.isEmpty(acceptLangHeader)) {
            return Collections.list(servletRequest.getLocales());
        }

        return null;
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.valueOf(servletRequest.getContentType());
    }

    @Override
    public Locale getLanguage() {
        String contentLangHeader = servletRequest.getHeader("Content-Language");

        int commaPos = contentLangHeader.indexOf(Char.COMMA);

        String firstLanguage = commaPos == -1 ? contentLangHeader : contentLangHeader.substring(0, commaPos);

        int hyphenPos = contentLangHeader.indexOf(Char.HYPHEN);

        return hyphenPos == -1 ? new Locale(firstLanguage) : new Locale(firstLanguage.substring(0, hyphenPos), firstLanguage.substring(hyphenPos + 1));
    }

    @Override
    public Map<String, Cookie> getCookies() {
        javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();

        Map<String, Cookie> cookieMap = new HashMap<>();

        if (cookies != null && cookies.length > 0) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), new Cookie(cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain(), cookie.getVersion()));
            }
        }

        return cookieMap;
    }
}
