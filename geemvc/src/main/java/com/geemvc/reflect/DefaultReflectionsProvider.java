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

package com.geemvc.reflect;

import com.geemvc.Str;
import com.geemvc.ThreadStash;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultReflectionsProvider implements ReflectionsProvider {
    protected String WEBAPP_CLASSES_DIR = "/WEB-INF/classes";
    protected String WEBAPP_LIB_DIR = "/WEB-INF/lib/";
    protected String MVN_TARGET_DIR = "/target/classes"; // For jetty-embedded.
    protected String GEEMVC_PROJECT_NAME = "geemvc";

    protected Pattern webGeemvcJarPattern = Pattern.compile("^\\/WEB\\-INF\\/lib\\/geemvc\\-\\d\\.\\d\\.\\d.*\\.jar$");
    protected Pattern appGeemvcJarPattern = Pattern.compile(".*geemvc\\-\\d\\.\\d\\.\\d.*\\.jar$");

    @Override
    public Reflections provide() {
        Reflections reflections = null;

        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();

            URL webappClassesPath = webappClassesPath();
            URL geemvcLibPath = geemvcLibPath();

            cb = cb.addUrls(webappClassesPath, geemvcLibPath).addClassLoader(mainClassLoader());

            // Give the developer a chance to add his own URLs.
            Set<URL> urls = appendURLs();
            if (urls != null && !urls.isEmpty()) {
                for (URL url : urls) {
                    if (url != null) {
                        cb.addUrls(url);
                    }
                }
            }

            // Give the developer a chance to add his own class-loaders.
            Set<ClassLoader> classLoaders = appendClassLoaders();
            if (classLoaders != null && !classLoaders.isEmpty()) {
                cb.addClassLoaders(classLoaders);
            }

            // Set all scanners. Can be changed in the extend() method if necessary.
            cb = cb.setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new MethodAnnotationsScanner(), new SubTypesScanner());

            // Give the developer a chance to extend the ConfigurationBuilder without having to re-implement the whole class.
            extend(cb);

            // Finally build the reflections.
            reflections = cb.build();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return reflections;
    }

    protected URL webappClassesPath() throws IOException {
        URL webappClassesPath = null;

        List<URL> urls = Collections.list(mainClassLoader().getResources(Str.EMPTY));

        if (urls != null && !urls.isEmpty()) {
            for (URL url : urls) {
                if (url.getPath().contains(WEBAPP_CLASSES_DIR)) {
                    webappClassesPath = url;
                    break;
                }

                if (url.getPath().contains(MVN_TARGET_DIR)) {
                    webappClassesPath = url;
                    break;
                }
            }
        }

        return webappClassesPath;
    }

    protected URL geemvcLibPath() throws Exception {
        URL geemvcLibPath = null;

        ServletContext servletContext = servletContext();

        // ----------------------------------------------------------------------------------------
        // First we'll try to find the geeMVC jar in the WEB-INF/lib directory.
        // ----------------------------------------------------------------------------------------
        if (servletContext != null) {
            Set<String> libJars = servletContext.getResourcePaths(WEBAPP_LIB_DIR);

            if (libJars != null && !libJars.isEmpty()) {
                if (libJars != null && !libJars.isEmpty()) {
                    for (String jar : libJars) {

                        Matcher m = webGeemvcJarPattern.matcher(jar);

                        if (m.matches()) {
                            File f = new File(servletContext.getRealPath(jar));

                            if (f.exists()) {
                                geemvcLibPath = f.toURI().toURL();
                                break;
                            }
                        }
                    }
                }
            }

            // ----------------------------------------------------------------------------------------
            // If we could not find it in web-scope, we will try the classpath.
            // ----------------------------------------------------------------------------------------
        } else {
            String classpath = System.getProperty("java.class.path");
            String[] classpathEntries = classpath.split(File.pathSeparator);

            for (String classpathEntry : classpathEntries) {
                Matcher m = appGeemvcJarPattern.matcher(classpathEntry);

                if (m.matches()) {
                    File f = new File(classpathEntry);

                    if (f.exists()) {
                        geemvcLibPath = f.toURI().toURL();
                        break;
                    }
                }
            }
        }

        // ----------------------------------------------------------------------------------------
        // If the geemMVC-lib path is still null, see if there is a geeMVC target/classes entry.
        // ----------------------------------------------------------------------------------------
        if (geemvcLibPath == null) {
            List<URL> urls = Collections.list(mainClassLoader().getResources(Str.EMPTY));

            if (urls != null && !urls.isEmpty()) {
                for (URL url : urls) {
                    if (url.getPath().contains(MVN_TARGET_DIR) && url.getPath().contains(GEEMVC_PROJECT_NAME)) {
                        geemvcLibPath = url;
                        break;
                    }
                }
            }
        }

        if (geemvcLibPath == null) {
            throw new IllegalStateException("geeMVC library could not be found in the classpath");
        }

        return geemvcLibPath;
    }

    protected void extend(ConfigurationBuilder cb) {
        // Override this method if necessary.
    }

    protected ClassLoader mainClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    protected Set<ClassLoader> appendClassLoaders() {
        // Override this method if necessary.
        return null;
    }

    protected Set<URL> appendURLs() {
        // Override this method if necessary.
        return null;
    }

    protected ServletContext servletContext() {
        ServletConfig servletConfig = (ServletConfig) ThreadStash.get(ServletConfig.class);
        return servletConfig.getServletContext();
    }
}
