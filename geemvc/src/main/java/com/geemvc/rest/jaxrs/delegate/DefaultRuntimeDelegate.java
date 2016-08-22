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

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import com.geemvc.inject.Injectors;
import com.google.inject.Injector;

public class DefaultRuntimeDelegate extends RuntimeDelegate {

    protected final Injector injector;

    public DefaultRuntimeDelegate() {
        injector = Injectors.provide();
    }

    @Override
    public UriBuilder createUriBuilder() {
        // TODO
        return null;
    }

    @Override
    public ResponseBuilder createResponseBuilder() {
        return injector.getInstance(ResponseBuilder.class);
    }

    @Override
    public VariantListBuilder createVariantListBuilder() {
        // TODO
        return null;
    }

    @Override
    public <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException, UnsupportedOperationException {
        // TODO
        return null;
    }

    @Override
    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) {
        return (HeaderDelegate<T>) injector.getInstance(MediaTypeHeaderDelegate.class);
    }
}
