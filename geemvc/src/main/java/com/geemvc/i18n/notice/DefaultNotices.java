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

package com.geemvc.i18n.notice;

import com.geemvc.Str;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class DefaultNotices implements Notices {

    protected Map<String, Set<Notice>> notices = null;

    protected static final String GLOBAL_NOTICE_KEY = "__globalNotices";

    @Inject
    protected Injector injector;

    @Override
    public void add(String field, String message, Object... args) {
        if (notices == null)
            notices = new LinkedHashMap<>();

        Set<Notice> fieldNotices = notices.get(field);

        if (fieldNotices == null) {
            fieldNotices = new LinkedHashSet<>();
            notices.put(field, fieldNotices);
        }

        if (message.startsWith(Str.CURLY_BRACKET_OPEN) && message.endsWith(Str.CURLY_BRACKET_CLOSE))
            message = message.substring(1, message.length() - 1);

        fieldNotices.add(injector.getInstance(Notice.class).build(field, message, args));
    }

    @Override
    public void add(String message, Object... args) {
        if (notices == null)
            notices = new LinkedHashMap<>();

        Set<Notice> fieldNotices = notices.get(GLOBAL_NOTICE_KEY);

        if (fieldNotices == null) {
            fieldNotices = new LinkedHashSet<>();
            notices.put(GLOBAL_NOTICE_KEY, fieldNotices);
        }

        if (message.startsWith(Str.CURLY_BRACKET_OPEN) && message.endsWith(Str.CURLY_BRACKET_CLOSE))
            message = message.substring(1, message.length() - 1);

        fieldNotices.add(injector.getInstance(Notice.class).build(GLOBAL_NOTICE_KEY, message, args));
    }

    @Override
    public boolean exist(String field) {
        return notices != null && notices.get(field) != null;
    }

    @Override
    public Notice get(String field) {
        if (notices == null)
            return null;

        Set<Notice> fieldNotices = notices.get(field);

        if (fieldNotices == null || fieldNotices.isEmpty())
            return null;

        return notices.get(field).iterator().next();
    }

    @Override
    public Set<Notice> globalNotices() {
        if (notices == null)
            return null;

        return notices.get(GLOBAL_NOTICE_KEY);
    }

    @Override
    public Set<Notice> allNotices() {
        if (notices == null)
            return null;

        Set<Notice> allNotices = new LinkedHashSet<>();

        for (Map.Entry<String, Set<Notice>> noticeEntry : notices.entrySet()) {
            allNotices.addAll(noticeEntry.getValue());
        }

        return allNotices;
    }

    @Override
    public Set<Notice> fieldNotices() {
        if (notices == null)
            return null;

        Set<Notice> fieldNotices = new LinkedHashSet<>();

        for (Map.Entry<String, Set<Notice>> noticeEntry : notices.entrySet()) {
            if (!GLOBAL_NOTICE_KEY.equals(noticeEntry.getKey())) {
                fieldNotices.addAll(noticeEntry.getValue());
            }
        }

        return fieldNotices;
    }

    @Override
    public boolean isEmpty() {
        return notices == null || notices.isEmpty();
    }

    @Override
    public String toString() {
        return "DefaultNotices [notices=" + notices + "]";
    }
}
