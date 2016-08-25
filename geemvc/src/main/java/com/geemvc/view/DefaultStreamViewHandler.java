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

package com.geemvc.view;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;

import org.apache.commons.io.IOUtils;

import com.geemvc.RequestContext;
import com.geemvc.config.Configuration;
import com.geemvc.config.Configurations;
import com.geemvc.handler.RequestHandler;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.view.bean.View;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class DefaultStreamViewHandler implements StreamViewHandler {

    protected Configuration configuration = Configurations.get();

    @Inject
    protected Injector injector;

    @Logger
    protected Log log;

    @Inject
    public DefaultStreamViewHandler() {
    }

    @Override
    public void handle(View view, RequestContext requestCtx) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) requestCtx.getResponse();

        if (view.length() > 0) {
            response.setContentLength((int) view.length());
        }

        if (view.filename() != null) {
            if (view.attachment()) {
                response.setHeader("Content-disposition", "attachment; filename=" + view.filename());
            } else {
                response.setHeader("Content-disposition", "filename=" + view.filename());
            }
        }

        if (view.contentType() != null) {
            response.setContentType(view.contentType());
        }

        if (view.rangeSupport()) {
            // TODO: range-support
        }

        if (view.result() != null) {
            RequestHandler requestHandler = requestCtx.requestHandler();
            Method handlerMethod = requestHandler.handlerMethod();

            if (configuration.isJaxRsEnabled()) {
                MessageBodyWriter mbw = injector.getInstance(Providers.class).getMessageBodyWriter(handlerMethod.getReturnType(), handlerMethod.getGenericReturnType(), handlerMethod.getAnnotations(), MediaType.valueOf(response.getContentType()));

                if (mbw != null && mbw.isWriteable(handlerMethod.getReturnType(), handlerMethod.getGenericReturnType(), handlerMethod.getAnnotations(), MediaType.valueOf(response.getContentType()))) {
                    MultivaluedMap<String, Object> httpResponseHeaders = injector.getInstance(MultivaluedMap.class);

                    mbw.writeTo(view.result(), handlerMethod.getReturnType(), handlerMethod.getGenericReturnType(), handlerMethod.getAnnotations(),
                            MediaType.valueOf(response.getContentType()), httpResponseHeaders, response.getOutputStream());
                } else {
                    response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                }
            } else {
                log.info("Unable to convert the result object of type '{}' to the media type '{}' as the JAX-RS runtime has been disabled.", () -> view.result().getClass().getName(), () -> response.getContentType());
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            }

            return;
        }

        if (view.stream() != null) {
            IOUtils.copy(view.stream(), response.getOutputStream());
        } else if (view.reader() != null) {
            IOUtils.copy(view.reader(), response.getOutputStream(), view.characterEncoding());
        } else if (view.output() != null) {
            response.getOutputStream().write(view.output().getBytes());
        } else {
            throw new IllegalStateException("You must provide either a stream, a reader or a string output when using Views.stream(). ");
        }
    }
}
