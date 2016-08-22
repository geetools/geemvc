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

package com.geemvc.rest.jaxrs.response;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;

import com.geemvc.RequestContext;
import com.google.inject.Inject;

public class DefaultJaxRsResponseHandler implements JaxRsResponseHandler {
    protected final Providers providers;
    protected final Application application;

    @Inject
    public DefaultJaxRsResponseHandler(Application application, Providers providers) {
        this.application = application;
        this.providers = providers;
    }

    public void handle(Object result, RequestContext requestCtx) {
        MessageBodyWriter writer = providers.getMessageBodyWriter(result.getClass(), result.getClass(), result.getClass().getAnnotations(), MediaType.valueOf(requestCtx.contentType()));

        try {
            writer.writeTo(result, result.getClass(), null, result.getClass().getAnnotations(), MediaType.valueOf(requestCtx.contentType()), null, requestCtx.getResponse().getOutputStream());
        } catch (WebApplicationException | IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

}
