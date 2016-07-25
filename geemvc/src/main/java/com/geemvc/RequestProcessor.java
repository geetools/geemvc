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

package com.geemvc;

import javax.servlet.AsyncContext;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class RequestProcessor implements Runnable {
    protected AsyncContext asyncContext;

    protected RequestContext requestCtx;

    // protected UnitOfWork unitOfWork;

    @Inject
    protected Injector injector;

    @Override
    public void run() {
        try {
            String id = asyncContext.getRequest().getParameter("id");

            asyncContext.getResponse().setContentType("text/html");

            RequestRunner requestRunner = injector.getInstance(RequestRunner.class);
            requestRunner.process(requestCtx);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            asyncContext.complete();
        }
    }

    public RequestProcessor build(AsyncContext asyncContext, RequestContext requestCtx) {
        this.asyncContext = asyncContext;
        this.requestCtx = requestCtx;
        return this;
    }

}
