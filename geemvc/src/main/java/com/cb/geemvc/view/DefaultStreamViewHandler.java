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

package com.cb.geemvc.view;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.view.bean.View;

public class DefaultStreamViewHandler implements StreamViewHandler {

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
