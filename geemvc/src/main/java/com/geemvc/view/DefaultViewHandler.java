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

import com.geemvc.Char;
import com.geemvc.RequestContext;
import com.geemvc.ThreadStash;
import com.geemvc.config.Configuration;
import com.geemvc.helper.Requests;
import com.geemvc.intercept.Interceptors;
import com.geemvc.intercept.LifecycleContext;
import com.geemvc.intercept.annotation.PostView;
import com.geemvc.intercept.annotation.PreView;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.geemvc.view.bean.View;
import com.google.inject.Inject;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class DefaultViewHandler implements ViewHandler {
    protected final ViewAdapterFactory viewAdapterFactory;
    protected final StreamViewHandler streamViewHandler;
    protected final Requests requests;
    protected final Interceptors interceptors;

    @Logger
    protected Log log;

    @Inject
    protected Configuration configuration;

    @Inject
    protected DefaultViewHandler(ViewAdapterFactory viewAdapterFactory, StreamViewHandler streamViewHandler, Requests requests, Interceptors interceptors) {
        this.viewAdapterFactory = viewAdapterFactory;
        this.streamViewHandler = streamViewHandler;
        this.requests = requests;
        this.interceptors = interceptors;
    }

    @Override
    public void handle(View view, RequestContext requestCtx) throws ServletException, IOException {
        if (view.forward() != null) {
            String viewPath = viewPath(view.forward());

            ViewAdapter viewAdapter = viewAdapterFactory.create(viewPath);

            if (viewAdapter == null)
                throw new IllegalStateException("No ViewAdapter found for the view-path '" + viewPath + "'. Please check your view-path or your configuration settings 'view-prefix' and 'view-suffix'.");

            log.debug("Processing forward view '{}' with adapter '{}'.", () -> viewPath, () -> viewAdapter.getClass().getName());

            processIncomingFlashVars(requestCtx);

            // ---------- Intercept lifecycle: PreView.
            interceptors.interceptLifecycle(PreView.class, (LifecycleContext) ThreadStash.get(LifecycleContext.class));

            log.trace("Preparing data before forwarding request to view servlet.");
            viewAdapter.prepare(view, requestCtx);

            log.trace("Forwarding request to view servlet.");
            viewAdapter.forward(viewPath, requestCtx);

            LifecycleContext lifecycleCtx = (LifecycleContext) ThreadStash.get(LifecycleContext.class);

            // ---------- Intercept lifecycle: PostView.
            interceptors.interceptLifecycle(PostView.class, lifecycleCtx);

        } else if (view.redirect() != null) {
            HttpServletRequest request = (HttpServletRequest) requestCtx.getRequest();
            final String redirectPath = requests.toRequestURL(view.redirect(), request.isSecure(), request);

            processOutgoingFlashVars(view, requestCtx);

            log.debug("Sending user to redirect path '{}'.", () -> redirectPath);
            ((HttpServletResponse) requestCtx.getResponse()).sendRedirect(requests.toRequestURL(view.redirect(), request.isSecure(), request));
        } else if (view.status() != null) {
            if (view.message() != null)
                ((HttpServletResponse) requestCtx.getResponse()).sendError(view.status());
            else
                ((HttpServletResponse) requestCtx.getResponse()).sendError(view.status(), view.message());
        }
        // Assuming stream.
        else {
            log.debug("Streaming data to user for path '{}'.", () -> requestCtx.getPath());
            streamViewHandler.handle(view, requestCtx);
        }
    }

    protected void processIncomingFlashVars(RequestContext requestCtx) {
        HttpSession session = requestCtx.getSession(false);
        ServletRequest request = requestCtx.getRequest();

        if (session == null)
            return;

        Map<String, Object> flashVars = (Map<String, Object>) session.getAttribute(GeemvcKey.FLASH_VARS);
        session.removeAttribute(GeemvcKey.FLASH_VARS);

        if (flashVars != null && !flashVars.isEmpty()) {
            for (Map.Entry<String, Object> flashVar : flashVars.entrySet()) {
                log.debug("Recovering flash variable '{}' for path '{}'.", () -> flashVar.getKey(), () -> requestCtx.getPath());
                request.setAttribute(flashVar.getKey(), flashVar.getValue());
            }
        }
    }

    protected void processOutgoingFlashVars(View view, RequestContext requestCtx) {
        Map<String, Object> flashVars = view.flashMap();

        if (flashVars == null || flashVars.size() == 0)
            return;

        log.debug("Adding flash variables {} to http session for redirect '{}'.", () -> flashVars.keySet(), () -> requestCtx.getPath() + " -> " + view.redirect());

        HttpSession session = requestCtx.getSession(true);
        session.setAttribute(GeemvcKey.FLASH_VARS, flashVars);
    }

    protected String viewPath(String path) {
        return path.startsWith("/WEB-INF/") ? path : new StringBuilder("/WEB-INF").append(configuration.viewPrefix()).append(Char.SLASH).append(path).append(configuration.viewSuffix()).toString();
    }
}
