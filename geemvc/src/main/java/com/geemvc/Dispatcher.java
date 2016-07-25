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

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class Dispatcher implements ReadListener, WriteListener {
    protected final byte[] buffer = new byte[1024];
    protected final AsyncContext asyncContext;
    protected final ServletInputStream input;
    protected final ServletOutputStream output;
    protected boolean complete;

    public Dispatcher(AsyncContext asyncContext) throws IOException {
        this.asyncContext = asyncContext;
        this.input = asyncContext.getRequest().getInputStream();
        this.output = asyncContext.getResponse().getOutputStream();
    }

    @Override
    public void onWritePossible() throws IOException {
        if (!input.isFinished()) {
            if (!complete)
                asyncContext.complete();
        } else {
            onDataAvailable();
        }
    }

    @Override
    public void onDataAvailable() throws IOException {
        while (input.isReady()) {
            int read = input.read(buffer);

            output.write(buffer, 0, read);

            if (!output.isReady())
                return;
        }

        if (input.isFinished()) {
            complete = true;
            asyncContext.complete();
        }
    }

    @Override
    public void onAllDataRead() throws IOException {

    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

}
