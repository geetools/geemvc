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

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.geemvc.RequestContext;
import com.geemvc.ThreadStash;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultUriInfo implements UriInfo {
    protected final RequestContext requestCtx;

    @Inject
    protected Injector injector;

    public DefaultUriInfo() {
        this.requestCtx = (RequestContext) ThreadStash.get(RequestContext.class);
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getPath(boolean decode) {
        return null;
    }

    @Override
    public List<PathSegment> getPathSegments() {
        return null;
    }

    @Override
    public List<PathSegment> getPathSegments(boolean decode) {
        return null;
    }

    @Override
    public URI getRequestUri() {
        return null;
    }

    @Override
    public UriBuilder getRequestUriBuilder() {
        return null;
    }

    @Override
    public URI getAbsolutePath() {
        return null;
    }

    @Override
    public UriBuilder getAbsolutePathBuilder() {
        return null;
    }

    @Override
    public URI getBaseUri() {
        return null;
    }

    @Override
    public UriBuilder getBaseUriBuilder() {
        return null;
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters() {
        MultivaluedMap<String, String> mvPathParameterMap = injector.getInstance(MultivaluedMap.class);

        Map<String, String[]> pathParmeters = requestCtx.getPathParameters();

        for (Map.Entry<String, String[]> entry : pathParmeters.entrySet()) {
            String[] values = entry.getValue();

            for (String v : values) {
                mvPathParameterMap.add(entry.getKey(), v);
            }

        }

        return mvPathParameterMap;
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters(boolean decode) {
        return getPathParameters();
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters() {
        MultivaluedMap<String, String> mvParameterMap = injector.getInstance(MultivaluedMap.class);

        Map<String, String[]> parameters = requestCtx.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            String[] values = entry.getValue();

            for (String v : values) {
                mvParameterMap.add(entry.getKey(), v);
            }
        }

        return mvParameterMap;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
        return getQueryParameters();
    }

    @Override
    public List<String> getMatchedURIs() {
        return null;
    }

    @Override
    public List<String> getMatchedURIs(boolean decode) {
        return null;
    }

    @Override
    public List<Object> getMatchedResources() {
        return null;
    }
}
