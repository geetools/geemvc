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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class Configurations {
    protected static ThreadLocal<Configuration> CONFIGURATION_STASH = new ThreadLocal<Configuration>();

    protected Class<?> DEFAULT_CONFIGURATION_TYPE = DefaultConfiguration.class;

    protected Class<?> customConfigurationType = null;

    public static Configurations builder() {
        return new Configurations();
    }

    public Configurations with(Class<?> configurarionType) {
        if (configurarionType.isInterface() || !Configuration.class.isAssignableFrom(configurarionType))
            throw new IllegalStateException("The configuration type must be an implementation class of the tyoe com.geemvc.config.Configuration");

        this.customConfigurationType = configurarionType;
        return this;
    }

    protected Configuration newConfigurationInstance() {
        try {
            return customConfigurationType == null ? (Configuration) DEFAULT_CONFIGURATION_TYPE.newInstance() : (Configuration) customConfigurationType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public Configuration build(ServletConfig servletConfig) {
        Map<String, String> configurationMap = new HashMap<>();
        Enumeration<String> parameterNames = servletConfig.getInitParameterNames();

        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            configurationMap.put(name, servletConfig.getInitParameter(name));
        }

        Configuration c = newConfigurationInstance().build(configurationMap);
        CONFIGURATION_STASH.set(c);

        return c;
    }

    public Configuration build(Map<String, String> configurationProperties) {
        Configuration c = newConfigurationInstance().build(configurationProperties);
        CONFIGURATION_STASH.set(c);

        return c;
    }

    public static Configuration get() {
        return CONFIGURATION_STASH.get();
    }

    public static void set(Configuration configuration) {
        CONFIGURATION_STASH.set(configuration);
    }

    public static void copyFrom(ServletContext servletContext) {
        Configuration configuration = (Configuration) servletContext.getAttribute(Configuration.class.getName());
        CONFIGURATION_STASH.set(configuration);
    }
}
