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

import com.geemvc.Char;
import com.geemvc.Str;
import com.geemvc.inject.DefaultInjectorProvider;
import com.geemvc.inject.InjectorProvider;
import com.google.inject.Singleton;

import javax.servlet.ServletConfig;
import java.util.*;

@Singleton
public class DefaultConfiguration implements Configuration {
    protected ServletConfig servletConfig;

    protected String defaultCharacterEncoding = "UTF-8";

    protected String defaultContentType = "text/html";

    protected Map<Locale, String> supportedLocaleEncodingMap = new LinkedHashMap<>();

    protected String defaultSupportedUriSuffixes = ".htm, .html, .json, .txt, .xml, .jsp";

    protected List<String> supportedUriSuffixesList = new ArrayList<>();

    protected InjectorProvider ínjectorProvider = new DefaultInjectorProvider();

    @Override
    public Configuration build(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;

        return this;
    }

    @Override
    public String viewPrefix() {
        return servletConfig.getInitParameter(VIEW_PREFIX_KEY);
    }

    @Override
    public String viewSuffix() {
        return servletConfig.getInitParameter(VIEW_SUFFIX_KEY);
    }

    @Override
    public String defaultCharacterEncoding() {
        String configuredDefaultCharacterEncoding = servletConfig.getInitParameter(DEFAULT_CHARACTER_ENCODING_KEY);
        return Str.isEmpty(configuredDefaultCharacterEncoding) ? defaultCharacterEncoding : configuredDefaultCharacterEncoding.trim();
    }

    @Override
    public String defaultContentType() {
        String configuredDefaultContentType = servletConfig.getInitParameter(DEFAULT_CONTENT_TYPE_KEY);
        return Str.isEmpty(configuredDefaultContentType) ? defaultContentType : configuredDefaultContentType.trim();
    }

    @Override
    public Set<Locale> supportedLocales() {
        if (supportedLocaleEncodingMap.isEmpty()) {
            String supportedLocales = servletConfig.getInitParameter(SUPPORTED_LOCALES_KEY);

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
        String configuredInjectorProvider = servletConfig.getInitParameter(INJECTOR_PROVIDER_KEY);
        try {
            return Str.isEmpty(configuredInjectorProvider) ? ínjectorProvider : (InjectorProvider) Class.forName(configuredInjectorProvider).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Set<String> excludePathMappinig() {
        String configuredExcludeMappings = servletConfig.getInitParameter(EXCLUDE_PATH_MAPPING_KEY);

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

            if (servletConfig != null)
                supportedUriSuffixes = servletConfig.getInitParameter(SUPPORTED_URI_SUFFIXES_KEY);

            if (Str.isEmpty(supportedUriSuffixes)) {
                supportedUriSuffixes = defaultSupportedUriSuffixes;
            }

            StringTokenizer st = new StringTokenizer(supportedUriSuffixes, Str.COMMA);

            while (st.hasMoreTokens()) {
                String suffix = st.nextToken();

                if (!Str.isEmpty(suffix)) {
                    supportedUriSuffixesList.add(suffix);
                }
            }
        }

        return supportedUriSuffixesList;
    }

    @Override
    public String toString() {
        return "DefaultConfiguration [viewPrefix()=" + viewPrefix() + ", viewSuffix()=" + viewSuffix() + ", defaultCharacterEncoding()=" + defaultCharacterEncoding() + ", defaultContentType()=" + defaultContentType() + ", supportedLocales()="
                + supportedLocales() + ", injectorProvider()=" + injectorProvider() + "]";
    }
}
