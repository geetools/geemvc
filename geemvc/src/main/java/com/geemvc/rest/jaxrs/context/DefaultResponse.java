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

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultResponse extends GeeMvcResponse {

    protected int status;
    protected Object entity;
    protected MultivaluedMap<String, Object> metaData;

    @Inject
    protected Injector injector;

    @Override
    public Response build(int status) {
        this.status = status;
        this.metaData = injector.getInstance(MultivaluedMap.class);
        return this;
    }

    @Override
    public Response build(int status, Object entity) {
        this.status = status;
        this.entity = entity;
        this.metaData = injector.getInstance(MultivaluedMap.class);
        return this;
    }

    @Override
    public Response build(int status, Object entity, MultivaluedMap metaData) {
        this.status = status;
        this.entity = entity;
        this.metaData = metaData;
        return this;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public MultivaluedMap<String, Object> getMetadata() {
        return metaData;
    }
}
