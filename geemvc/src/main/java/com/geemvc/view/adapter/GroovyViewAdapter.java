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

package com.geemvc.view.adapter;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.geemvc.RequestContext;
import com.geemvc.annotation.Adapter;
import com.geemvc.view.ViewAdapter;
import com.geemvc.view.bean.View;
import com.google.inject.Singleton;

@Singleton
@Adapter
public class GroovyViewAdapter implements ViewAdapter {
    protected final static String NAME = "GroovyViewAdapter";
    protected final static String GROOVY_EXTENSION = ".groovy";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean canHandle(String path) {
        return path.endsWith(GROOVY_EXTENSION);
    }

    @Override
    public ViewAdapter prepare(View view, RequestContext requestCtx) {
        if (!view.isEmpty()) {
            Set<Entry<String, Object>> entries = view.entrySet();

            if (entries != null && !entries.isEmpty()) {
                HttpServletRequest request = (HttpServletRequest) requestCtx.getRequest();

                for (Entry<String, Object> entry : entries) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
            }
        }

        return this;
    }

    @Override
    public void forward(String path, RequestContext requestCtx) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = requestCtx.getServletContext().getRequestDispatcher(path);
        requestDispatcher.forward(requestCtx.getRequest(), requestCtx.getResponse());
    }
}
