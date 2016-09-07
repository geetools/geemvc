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

package com.geemvc.view.bean;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class DefaultResult implements Result {
    protected Map<String, Object> bindings = null;

    protected Map<String, Object> flashBindings = null;

    protected Map<String, Object> handlerParmeters = null;

    protected String forward = null;

    protected String redirect = null;

    protected String handlerPath = null;

    protected String httpMethod = null;

    protected String uniqueHandler = null;

    protected Class<?> controllerClass = null;

    protected String handlerMethod = null;

    protected String contentType = null;

    protected InputStream inputStream = null;

    protected Reader reader = null;

    protected String output = null;

    protected String filename = null;

    protected Object result = null;

    protected String characterEncoding = null;

    protected long lastModified;

    protected long length;

    protected boolean attachment;

    protected boolean rangeSupport;

    protected Integer status = null;

    protected String message = null;

    @Override
    public Result view(String path) {
        this.forward = path;
        return this;
    }

    @Override
    public String view() {
        return forward;
    }

    @Override
    public Result redirect(String to) {
        this.redirect = to;
        return this;
    }

    @Override
    public String redirect() {
        return redirect;
    }

    @Override
    public Result handler(String path) {
        this.handlerPath = path;
        return this;
    }

    @Override
    public Result handler(String path, String httpMethod) {
        this.handlerPath = path;
        this.httpMethod = httpMethod;
        return this;
    }

    @Override
    public String handlerPath() {
        return handlerPath;
    }

    @Override
    public String httpMethod() {
        return httpMethod;
    }

    @Override
    public Result handler(Class<?> controllerClass, String handlerMethod) {
        this.controllerClass = controllerClass;
        this.handlerMethod = handlerMethod;
        return this;
    }

    @Override
    public Class<?> controllerClass() {
        return controllerClass;
    }

    @Override
    public String handlerMethod() {
        return handlerMethod;
    }

    @Override
    public Result uniqueHandler(String name) {
        this.uniqueHandler = name;
        return this;
    }

    @Override
    public String uniqueHandler() {
        return uniqueHandler;
    }

    @Override
    public Result bind(String name, Object value) {
        if (bindings == null)
            bindings = new HashMap<>();

        bindings.put(name, value);

        return this;
    }

    @Override
    public Result bind(Map<String, Object> bindings) {
        if (bindings == null)
            return this;

        if (this.bindings == null)
            this.bindings = new HashMap<>();

        this.bindings.putAll(bindings);

        return this;
    }

    @Override
    public Object binding(String name) {
        return bindings == null ? null : bindings.get(name);
    }

    @Override
    public boolean containsBinding(String name) {
        return bindings == null ? false : bindings.containsKey(name);
    }

    @Override
    public boolean hasBindings() {
        return bindings != null && !bindings.isEmpty();
    }

    @Override
    public Map<String, Object> bindings() {
        return bindings;
    }

    @Override
    public Result flash(String name, Object value) {
        if (flashBindings == null)
            flashBindings = new HashMap<>();

        flashBindings.put(name, value);

        return this;
    }

    @Override
    public Result flash(Map<String, Object> flashBindings) {
        if (flashBindings == null)
            return this;

        if (this.flashBindings == null)
            this.flashBindings = new HashMap<>();

        this.flashBindings.putAll(flashBindings);

        return this;
    }

    @Override
    public Map<String, Object> flashMap() {
        return flashBindings;
    }

    @Override
    public Result param(String name, Object value) {
        if (handlerParmeters == null)
            handlerParmeters = new HashMap<>();

        handlerParmeters.put(name, value);

        return this;
    }

    @Override
    public Result param(Map<String, Object> parameters) {
        if (handlerParmeters == null)
            return this;

        if (this.handlerParmeters == null)
            this.handlerParmeters = new HashMap<>();

        this.handlerParmeters.putAll(parameters);

        return this;
    }

    @Override
    public Object param(String name) {
        return handlerParmeters == null ? null : handlerParmeters.get(name);
    }

    @Override
    public boolean containsParam(String name) {
        return handlerParmeters == null ? false : handlerParmeters.containsKey(name);
    }

    @Override
    public boolean hasParameters() {
        return handlerParmeters != null && !handlerParmeters.isEmpty();
    }

    @Override
    public Map<String, Object> parameters() {
        return handlerParmeters;
    }

    @Override
    public Result stream(String contentType, InputStream inputStream) {
        this.contentType = contentType;
        this.inputStream = inputStream;

        return this;
    }

    @Override
    public Result stream(String contentType, Reader reader) {
        this.contentType = contentType;
        this.reader = reader;

        return this;
    }

    @Override
    public Result stream(String contentType, String output) {
        this.contentType = contentType;
        this.output = output;

        return this;
    }

    @Override
    public Result stream(String contentType, Object result) {
        this.contentType = contentType;
        this.result = result;

        return this;
    }

    @Override
    public String contentType() {
        return contentType;
    }

    @Override
    public InputStream stream() {
        return inputStream;
    }

    @Override
    public Reader reader() {
        return reader;
    }

    @Override
    public String output() {
        return output;
    }

    @Override
    public Result filename(String filename) {
        this.filename = filename;
        return this;
    }

    @Override
    public String filename() {
        return filename;
    }

    @Override
    public Object result() {
        return result;
    }

    @Override
    public void result(Object result) {
        this.result = result;
    }

    @Override
    public Result characterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
        return this;
    }

    @Override
    public String characterEncoding() {
        return characterEncoding;
    }

    @Override
    public Result lastModified(long lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    @Override
    public long lastModified() {
        return lastModified;
    }

    @Override
    public Result length(long length) {
        this.length = length;
        return this;
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public Result attachment(boolean attachment) {
        this.attachment = attachment;
        return this;
    }

    @Override
    public boolean attachment() {
        return attachment;
    }

    @Override
    public Result rangeSupport(boolean rangeSupport) {
        this.rangeSupport = rangeSupport;
        return this;
    }

    @Override
    public boolean rangeSupport() {
        return rangeSupport;
    }

    @Override
    public Integer status() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public Result status(Integer status) {
        this.status = status;
        return this;
    }

    @Override
    public Result status(Integer status, String message) {
        this.status = status;
        this.message = message;
        return this;
    }
}
