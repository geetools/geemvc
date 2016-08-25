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

package com.geemvc.i18n.message;

import java.text.MessageFormat;
import java.util.Locale;

import com.geemvc.RequestContext;
import com.geemvc.ThreadStash;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DefaultMessages implements Messages {
    protected final CompositeMessageResolver compositeMessageResolver;

    @Inject
    public DefaultMessages(CompositeMessageResolver compositeMessageResolver) {
        this.compositeMessageResolver = compositeMessageResolver;
    }

    @Override
    public String getString(String messageKey) {
        return getString(messageKey, null, false, (Object[]) null);
    }

    @Override
    public String getString(String messageKey, Object... args) {
        return getString(messageKey, null, false, args);

    }

    @Override
    public String getString(String messageKey, Locale locale) {
        return getString(messageKey, locale, false, (Object[]) null);
    }

    @Override
    public String getString(String messageKey, Locale locale, Object... args) {
        return getString(messageKey, locale, false, args);
    }

    @Override
    public String getString(String messageKey, Locale locale, boolean failQuietly, Object... args) {
        String message = compositeMessageResolver.resolve(messageKey, locale, requestContext(), failQuietly);

        if (message != null && args != null && args.length > 0 && args[0] != null) {
            message = MessageFormat.format(message, args);
        }

        return message;
    }

    protected RequestContext requestContext() {
        return (RequestContext) ThreadStash.get(RequestContext.class);
    }
}
