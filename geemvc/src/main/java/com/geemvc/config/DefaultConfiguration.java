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

package com.geemvc.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.geemvc.Char;
import com.geemvc.Str;
import com.geemvc.inject.DefaultInjectorProvider;
import com.geemvc.inject.InjectorProvider;
import com.google.inject.Singleton;

@Singleton
public class DefaultConfiguration implements Configuration {
    protected Map<String, String> configurationMap;

    protected String defaultCharacterEncoding = "UTF-8";

    protected String defaultContentType = "text/html";

    protected Map<Locale, String> supportedLocaleEncodingMap = new LinkedHashMap<>();

    protected String defaultSupportedUriSuffixes = ".htm, .html, .json, .txt, .xml, .jsp";

    protected List<String> supportedUriSuffixesList = new ArrayList<>();

    protected List<String> includeLibsInReflections = new ArrayList<>();

    protected List<String> excludeLibsInReflections = new ArrayList<>();

    protected InjectorProvider ínjectorProvider = null;

    @Override
    public Configuration build(Map<String, String> configurationMap) {
        this.configurationMap = configurationMap;
        return this;
    }

    @Override
    public String viewPrefix() {
        return configurationMap.get(VIEW_PREFIX_KEY);
    }

    @Override
    public String viewSuffix() {
        return configurationMap.get(VIEW_SUFFIX_KEY);
    }

    @Override
    public String defaultCharacterEncoding() {
        String configuredDefaultCharacterEncoding = configurationMap.get(DEFAULT_CHARACTER_ENCODING_KEY);
        return Str.isEmpty(configuredDefaultCharacterEncoding) ? defaultCharacterEncoding : configuredDefaultCharacterEncoding.trim();
    }

    @Override
    public String defaultContentType() {
        String configuredDefaultContentType = configurationMap.get(DEFAULT_CONTENT_TYPE_KEY);
        return Str.isEmpty(configuredDefaultContentType) ? defaultContentType : configuredDefaultContentType.trim();
    }

    @Override
    public Set<Locale> supportedLocales() {
        if (supportedLocaleEncodingMap.isEmpty()) {
            String supportedLocales = configurationMap.get(SUPPORTED_LOCALES_KEY);

            StringTokenizer st = new StringTokenizer(supportedLocales, Str.COMMA);

            while (st.hasMoreTokens()) {
                String localeStr = st.nextToken();
                String charset = null;

                int colonPos = localeStr.indexOf(Char.COLON);

                if (colonPos != -1) {
                    charset = localeStr.substring(colonPos + 1);
                    localeStr = localeStr.substring(0, colonPos);
                }

                if (Str.isEmpty(charset))
                    charset = defaultCharacterEncoding();

                StringTokenizer localeStrTokenizer = new StringTokenizer(localeStr, Str.UNDERSCORE);

                String lang = null;
                String country = null;
                String variant = null;

                if (localeStrTokenizer.hasMoreTokens())
                    lang = localeStrTokenizer.nextToken();

                if (localeStrTokenizer.hasMoreTokens())
                    country = localeStrTokenizer.nextToken();

                if (localeStrTokenizer.hasMoreTokens())
                    variant = localeStrTokenizer.nextToken();

                if (lang != null && country != null && variant != null)
                    supportedLocaleEncodingMap.put(new Locale(lang.trim(), country.trim(), variant.trim()), charset);

                else if (lang != null && country != null)
                    supportedLocaleEncodingMap.put(new Locale(lang.trim(), country.trim()), charset);

                else if (lang != null)
                    supportedLocaleEncodingMap.put(new Locale(lang.trim()), charset);
            }
        }

        return supportedLocaleEncodingMap.keySet();
    }

    @Override
    public String characterEncodingFor(Locale locale) {
        if (locale == null)
            return null;

        return supportedLocaleEncodingMap.get(locale);
    }

    @Override
    public InjectorProvider injectorProvider() {
        if (ínjectorProvider != null)
            return ínjectorProvider;

        String configuredInjectorProvider = configurationMap.get(INJECTOR_PROVIDER_KEY);
        try {
            return Str.isEmpty(configuredInjectorProvider) ? new DefaultInjectorProvider() : (InjectorProvider) Class.forName(configuredInjectorProvider).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Set<String> excludePathMappinig() {
        String configuredExcludeMappings = configurationMap.get(EXCLUDE_PATH_MAPPING_KEY);

        Set<String> excludeMappings = null;

        if (!Str.isEmpty(configuredExcludeMappings)) {
            excludeMappings = new HashSet<>();

            StringTokenizer pathTokenizer = new StringTokenizer(configuredExcludeMappings, Str.COMMA);

            while (pathTokenizer.hasMoreTokens()) {
                String mappedPath = pathTokenizer.nextToken();
                excludeMappings.add(mappedPath.trim());
            }
        }

        return excludeMappings;
    }

    @Override
    public List<String> supportedUriSuffixes() {
        if (supportedUriSuffixesList.isEmpty()) {
            String supportedUriSuffixes = null;

            if (configurationMap != null)
                supportedUriSuffixes = configurationMap.get(SUPPORTED_URI_SUFFIXES_KEY);

            if (Str.isEmpty(supportedUriSuffixes)) {
                supportedUriSuffixes = defaultSupportedUriSuffixes;
            }

            StringTokenizer st = new StringTokenizer(supportedUriSuffixes, Str.COMMA);

            while (st.hasMoreTokens()) {
                String suffix = st.nextToken();

                if (!Str.isEmpty(suffix)) {
                    supportedUriSuffixesList.add(suffix.trim());
                }
            }
        }

        return supportedUriSuffixesList;
    }

    @Override
    public List<String> reflectionsLibIncludes() {
        if (includeLibsInReflections.isEmpty()) {
            String reflectionsLibIncludes = null;

            if (configurationMap != null)
                reflectionsLibIncludes = configurationMap.get(REFLECTIONS_INCLUDE_LIBS_KEY);

            if (!Str.isEmpty(reflectionsLibIncludes)) {
                StringTokenizer st = new StringTokenizer(reflectionsLibIncludes, Str.COMMA);

                while (st.hasMoreTokens()) {
                    String lib = st.nextToken();

                    if (!Str.isEmpty(lib)) {
                        includeLibsInReflections.add(lib.trim());
                    }
                }
            }
        }

        return includeLibsInReflections;
    }

    @Override
    public List<String> reflectionsLibExcludes() {
        if (excludeLibsInReflections.isEmpty()) {
            String reflectionsLibExcludes = null;

            if (configurationMap != null)
                reflectionsLibExcludes = configurationMap.get(REFLECTIONS_EXCLUDE_LIBS_KEY);

            if (!Str.isEmpty(reflectionsLibExcludes)) {
                StringTokenizer st = new StringTokenizer(reflectionsLibExcludes, Str.COMMA);

                while (st.hasMoreTokens()) {
                    String lib = st.nextToken();

                    if (!Str.isEmpty(lib)) {
                        excludeLibsInReflections.add(lib.trim());
                    }
                }
            }
        }

        return excludeLibsInReflections;
    }

    @Override
    public boolean isJaxRsEnabled() {
        String configuredJaxRsEnabled = configurationMap.get(JAX_RS_ENABLED_KEY);
        return Str.isEmpty(configuredJaxRsEnabled) ? true : Boolean.valueOf(configuredJaxRsEnabled);
    }

    @Override
    public String toString() {
        return "DefaultConfiguration [viewPrefix()=" + viewPrefix() + ", viewSuffix()=" + viewSuffix() + ", defaultCharacterEncoding()=" + defaultCharacterEncoding() + ", defaultContentType()=" + defaultContentType() + ", supportedLocales()="
                + supportedLocales() + ", injectorProvider()=" + injectorProvider() + ", excludePathMappinig()=" + excludePathMappinig() + ", supportedUriSuffixes()=" + supportedUriSuffixes() + ", reflectionsLibIncludes()=" + reflectionsLibIncludes()
                + ", reflectionsLibExcludes()=" + reflectionsLibExcludes() + ", isJaxRsEnabled()=" + isJaxRsEnabled() + "]";
    }
}
