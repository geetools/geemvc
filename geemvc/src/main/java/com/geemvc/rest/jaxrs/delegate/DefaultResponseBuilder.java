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

package com.geemvc.rest.jaxrs.delegate;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Variant;

import com.geemvc.rest.jaxrs.context.GeeMvcResponse;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultResponseBuilder extends ResponseBuilder implements Cloneable {
    protected int status = -1;
    protected Object entity;
    protected MultivaluedMap<String, Object> metaData;

    @Inject
    protected Injector injector;

    @Override
    public Response build() {
        if (entity == null && status == -1)
            status = HttpServletResponse.SC_NO_CONTENT;

        return injector.getInstance(GeeMvcResponse.class).build(status, entity, metaData);
    }

    @Override
    public ResponseBuilder clone() {
        DefaultResponseBuilder rb = new DefaultResponseBuilder();
        rb.status(status);
        rb.entity(entity);

        MultivaluedMap mm = injector.getInstance(MultivaluedMap.class);
        mm.putAll(this.metaData);
        rb.metaData(mm);

        return this;
    }

    @Override
    public ResponseBuilder status(int status) {
        this.status = status;
        return this;
    }

    @Override
    public ResponseBuilder entity(Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public ResponseBuilder type(MediaType type) {
        return setHeader(HttpHeaders.CONTENT_TYPE, type);
    }

    @Override
    public ResponseBuilder type(String type) {
        return setHeader(HttpHeaders.CONTENT_TYPE, type);
    }

    @Override
    public ResponseBuilder variant(Variant variant) {
        // TODO
        return this;
    }

    @Override
    public ResponseBuilder variants(List<Variant> variants) {
        // TODO
        return this;
    }

    @Override
    public ResponseBuilder language(String language) {
        return setHeader(HttpHeaders.CONTENT_LANGUAGE, language);
    }

    @Override
    public ResponseBuilder language(Locale language) {
        return setHeader(HttpHeaders.CONTENT_LANGUAGE, language);
    }

    @Override
    public ResponseBuilder location(URI location) {
        return setHeader(HttpHeaders.LOCATION, location);
    }

    @Override
    public ResponseBuilder contentLocation(URI location) {
        return setHeader(HttpHeaders.CONTENT_LOCATION, location);
    }

    @Override
    public ResponseBuilder tag(EntityTag tag) {
        return setHeader(HttpHeaders.ETAG, tag);
    }

    @Override
    public ResponseBuilder tag(String tag) {
        // TODO
        return this;
    }

    @Override
    public ResponseBuilder lastModified(Date lastModified) {
        return setHeader(HttpHeaders.LAST_MODIFIED, lastModified);
    }

    @Override
    public ResponseBuilder cacheControl(CacheControl cacheControl) {
        return setHeader(HttpHeaders.CACHE_CONTROL, cacheControl);
    }

    @Override
    public ResponseBuilder expires(Date expires) {
        return setHeader(HttpHeaders.EXPIRES, expires);
    }

    @Override
    public ResponseBuilder header(String name, Object value) {
        return setHeader(name, value);
    }

    @Override
    public ResponseBuilder cookie(NewCookie... cookies) {
        return addHeader(HttpHeaders.SET_COOKIE, (Object[]) cookies);
    }

    protected ResponseBuilder setHeader(String name, Object value) {
        if (value == null) {
            metaData().remove(name);
        } else {
            metaData().putSingle(name, value);
        }

        return this;
    }

    protected ResponseBuilder addHeader(String name, Object... values) {
        if (values != null && values.length > 0 && values[0] != null) {
            for (Object value : values) {
                metaData().add(name, value);
            }
        } else {
            metaData().remove(name);
        }

        return this;
    }

    protected MultivaluedMap<String, Object> metaData() {
        if (this.metaData == null)
            this.metaData = injector.getInstance(MultivaluedMap.class);

        return this.metaData;
    }

    protected ResponseBuilder metaData(MultivaluedMap<String, Object> metaData) {
        this.metaData = metaData;
        return this;
    }
}
