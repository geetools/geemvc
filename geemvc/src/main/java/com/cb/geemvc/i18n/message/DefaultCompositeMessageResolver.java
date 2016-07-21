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

package com.cb.geemvc.i18n.message;

import com.cb.geemvc.RequestContext;
import com.cb.geemvc.Str;
import com.cb.geemvc.cache.Cache;
import com.cb.geemvc.logging.Log;
import com.cb.geemvc.logging.annotation.Logger;
import com.cb.geemvc.reflect.ReflectionProvider;
import com.google.common.base.CaseFormat;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

@Singleton
public class DefaultCompositeMessageResolver implements CompositeMessageResolver {
    protected final Set<MessageResolver> messageResolvers;

    protected String controllerSuffix = "Controller";

    protected String actionSuffix = "Action";

    protected String noLocale = "NONE";

    protected final ReflectionProvider reflectionProvider;

    @Inject
    protected Injector injector;

    @Inject
    protected Cache cache;

    @Logger
    protected Log log;

    protected static final String RESOLVED_MESSAGE_CACHE_KEY = "geemvc/resolvedMessage/%s@%s";

    @Inject
    public DefaultCompositeMessageResolver(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
        this.messageResolvers = reflectionProvider.locateMessageResolvers();
    }

    @Override
    public String resolve(String messageKey, RequestContext requestCtx) {
        return resolve(messageKey, null, requestCtx);
    }

    @Override
    public String resolve(String messageKey, Locale locale, RequestContext requestCtx) {
        return resolve(messageKey, locale, requestCtx, false);
    }

    @Override
    public String resolve(String messageKey, Locale locale, RequestContext requestCtx, boolean failQuietly) {
        String localeStr = locale == null ? noLocale : locale.toString();

        log.trace("Attempting to resolve message '{}' using locale '{}'.", () -> messageKey, () -> localeStr);

        String cacheKey = String.format(RESOLVED_MESSAGE_CACHE_KEY, messageKey, localeStr);

        return (String) cache.get(DefaultCompositeMessageResolver.class, cacheKey, () -> {
            Set<String> resolveAttempts = new LinkedHashSet<>();

            Locale currentLocale = requestCtx.currentLocale();
            Locale langAndCountryLocale = new Locale(currentLocale.getLanguage(), currentLocale.getCountry());
            Locale langLocale = new Locale(currentLocale.getLanguage());

            Class<?> controllerClass = requestCtx.requestHandler().controllerClass();

            Set<String> attemptBundleBaseNames = new LinkedHashSet<>();
            attemptBundleBaseNames.add(controllerBundle(requestCtx));
            attemptBundleBaseNames.add(defaultBundle());

            Set<Locale> attemptLocales = new LinkedHashSet<>();

            if (locale == null) {
                attemptLocales.add(langAndCountryLocale);
                attemptLocales.add(langLocale);
                attemptLocales.add(Locale.ROOT);
            } else {
                attemptLocales.add(locale);
            }

            Set<String> attemptContextPrefixes = new LinkedHashSet<>();
            attemptContextPrefixes.add(controllerClass.getName() + Str.DOT);
            attemptContextPrefixes.add(controllerClass.getSimpleName() + Str.DOT);
            attemptContextPrefixes.add(Str.EMPTY);

            for (MessageResolver messageResolver : messageResolvers) {
                for (String baseName : attemptBundleBaseNames) {
                    for (Locale attemptLocale : attemptLocales) {
                        for (String ctxPrefix : attemptContextPrefixes) {
                            resolveAttempts.add(toString(baseName, attemptLocale, ctxPrefix, messageKey));

                            log.trace("Attempting to resolve message '{}' using baseName '{}', locale '{}' and context prefix '{}'.", () -> messageKey, () -> baseName, () -> localeStr, () -> ctxPrefix);

                            String message = messageResolver.resolve(String.format("%s%s", ctxPrefix, messageKey), baseName, attemptLocale);

                            if (message != null) {
                                log.debug("Resolved message '{}' using key '{}', baseName '{}', locale '{}' and context prefix '{}'.", () -> message, () -> messageKey, () -> baseName, () -> localeStr, () -> ctxPrefix);

                                return message;
                            }
                        }
                    }
                }
            }

            if (!failQuietly)
                throw new IllegalStateException("Unable to find message key '" + messageKey + "' in any of the following attempts: " + resolveAttempts);

            else {
                log.debug("Unable to resolve message using key '{}' and locale '{}'.", () -> messageKey, () -> localeStr);
                return null;
            }
        });
    }

    protected String toString(String baseName, Locale locale, String ctxPrefix, String messageKey) {
        return String.format("%s%s@%s%s", baseName, Str.isEmpty(locale.toString()) ? Str.EMPTY : Str.UNDERSCORE + locale.toString(), ctxPrefix, messageKey);
    }

    protected String defaultBundle() {
        return "messages";
    }

    protected String controllerBundle(RequestContext requestCtx) {
        Locale currentLocale = requestCtx.currentLocale();
        return String.format("%s-messages", controllerName(requestCtx), currentLocale.getLanguage());
    }

    protected String controllerName(RequestContext requestCtx) {
        Class<?> controllerClass = requestCtx.requestHandler().controllerClass();

        String name = controllerClass.getSimpleName();

        if (name.endsWith(controllerSuffix)) {
            name = name.replace(controllerSuffix, Str.EMPTY);
        } else if (name.endsWith(actionSuffix)) {
            name = name.replace(actionSuffix, Str.EMPTY);
        }

        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }
}
