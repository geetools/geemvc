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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultView implements View {
    protected Map<String, Object> bindings = null;

    protected Map<String, Object> flashBindings = null;

    protected String forward = null;

    protected String redirect = null;

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
    public View forward(String to) {
        this.forward = to;
        return this;
    }

    @Override
    public String forward() {
        return forward;
    }

    @Override
    public View redirect(String to) {
        this.redirect = to;
        return this;
    }

    @Override
    public String redirect() {
        return redirect;
    }

    @Override
    public View bind(String name, Object value) {
        if (bindings == null)
            bindings = new HashMap<>();

        bindings.put(name, value);

        return this;
    }

    @Override
    public View bind(Map<String, Object> bindings) {
        if (bindings == null)
            return this;

        if (this.bindings == null)
            this.bindings = new HashMap<>();

        this.bindings.putAll(bindings);

        return this;
    }

    @Override
    public View flash(String name, Object value) {
        if (flashBindings == null)
            flashBindings = new HashMap<>();

        flashBindings.put(name, value);

        return this;
    }

    @Override
    public View flash(Map<String, Object> flashBindings) {
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
    public int size() {
        return bindings == null ? 0 : bindings.size();
    }

    @Override
    public boolean isEmpty() {
        return bindings == null ? true : bindings.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return bindings == null ? false : bindings.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return bindings == null ? false : bindings.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return bindings == null ? null : bindings.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        if (bindings == null)
            bindings = new HashMap<>();

        return bindings.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return bindings == null ? null : bindings.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        if (bindings == null)
            bindings = new HashMap<>();

        bindings.putAll(m);
    }

    @Override
    public void clear() {
        if (bindings != null)
            bindings.clear();
    }

    @Override
    public Set<String> keySet() {
        return bindings == null ? null : bindings.keySet();
    }

    @Override
    public Collection<Object> values() {
        return bindings == null ? null : bindings.values();
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return bindings == null ? null : bindings.entrySet();
    }

    @Override
    public View stream(String contentType, InputStream inputStream) {
        this.contentType = contentType;
        this.inputStream = inputStream;

        return this;
    }

    @Override
    public View stream(String contentType, Reader reader) {
        this.contentType = contentType;
        this.reader = reader;

        return this;
    }

    @Override
    public View stream(String contentType, String output) {
        this.contentType = contentType;
        this.output = output;

        return this;
    }

    @Override
    public View stream(String contentType, Object result) {
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
    public View filename(String filename) {
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
    public View characterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
        return this;
    }

    @Override
    public String characterEncoding() {
        return characterEncoding;
    }

    @Override
    public View lastModified(long lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    @Override
    public long lastModified() {
        return lastModified;
    }

    @Override
    public View length(long length) {
        this.length = length;
        return this;
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public View attachment(boolean attachment) {
        this.attachment = attachment;
        return this;
    }

    @Override
    public boolean attachment() {
        return attachment;
    }

    @Override
    public View rangeSupport(boolean rangeSupport) {
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
    public View status(Integer status) {
        this.status = status;
        return this;
    }

    @Override
    public View status(Integer status, String message) {
        this.status = status;
        this.message = message;
        return this;
    }

}
