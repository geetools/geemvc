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

package com.cb.geemvc.taglib.layout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LayoutContext implements Serializable {
    private static final long serialVersionUID = -4336764850836158695L;

    public static final String KEY = "gee.layout.context";

    protected Map<String, String> sections = new HashMap<>();
    protected Map<String, Object> attributes = new LinkedHashMap<>();

    public LayoutContext putSection(String name, String content) {
        sections.put(name, content);
        return this;
    }

    public boolean hasSection(String name) {
        return section(name) != null;
    }

    public String section(String name) {
        return sections.get(name);
    }

    public Map<String, String> sections() {
        return sections;
    }

    public LayoutContext setAttribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public LayoutContext setAttributes(Map<String, Object> attributes) {
        this.attributes.putAll(attributes);
        return this;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
