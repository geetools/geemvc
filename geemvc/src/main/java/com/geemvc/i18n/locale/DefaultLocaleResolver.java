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

package com.geemvc.i18n.locale;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;

import com.geemvc.RequestContext;
import com.geemvc.config.Configuration;
import com.geemvc.config.Configurations;
import com.geemvc.logging.Log;
import com.geemvc.logging.annotation.Logger;
import com.google.inject.Singleton;

@Singleton
public class DefaultLocaleResolver implements LocaleResolver {
    protected Configuration configuration = Configurations.get();

    @Logger
    protected Log log;

    public Locale resolve(RequestContext requestCtx) {
        Enumeration<Locale> requestLocales = requestCtx.getLocales();

        Set<Locale> supportedLocales = configuration.supportedLocales();

        log.debug("Resolving locale with request locales {} and supported locales {}.", () -> requestLocales, () -> supportedLocales);

        if (supportedLocales.isEmpty())
            throw new IllegalStateException("You must provide at least 1 supported locale. Check your supported-locales configuration - an example of the correct syntax would be: 'en, de_DE, fr_FR, es_ES, ru_RU:UTF-8, ja_JP:Shift_JIS, zh:UTF-8'.");

        // If there is no request-locale, return the first supported one.
        if (requestLocales == null) {
            return supportedLocales.iterator().next();
        }

        String foundLanguage = null;
        String foundCountry = null;

        while (requestLocales.hasMoreElements()) {
            Locale requestLocale = (Locale) requestLocales.nextElement();

            for (Locale supportedLocale : supportedLocales) {
                if (foundLanguage == null && requestLocale.getLanguage().equals(supportedLocale.getLanguage()))
                    foundLanguage = supportedLocale.getLanguage();

                if (foundCountry == null && foundLanguage != null && supportedLocale.getLanguage().equals(foundLanguage) && requestLocale.getCountry() != null && supportedLocale.getCountry() != null
                        && requestLocale.getCountry().equals(supportedLocale.getCountry())) {
                    foundCountry = supportedLocale.getCountry();
                    break;
                }
            }

            // Exact match found, so no need to continue.
            if (foundLanguage != null && foundCountry != null)
                break;
        }

        Locale resolvedLocale = null;

        if (foundLanguage != null && foundCountry != null)
            resolvedLocale = new Locale(foundLanguage, foundCountry);

        else if (foundLanguage != null)
            resolvedLocale = new Locale(foundLanguage);

        return resolvedLocale == null ? supportedLocales.iterator().next() : resolvedLocale;
    }
}
