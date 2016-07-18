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

package com.cb.geemvc.reflect;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import com.cb.geemvc.cache.Cache;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TestReflectionsWrapper extends DefaultReflectionsWrapper {
    @Inject
    public TestReflectionsWrapper(Cache cache) {
        super(cache);
    }

    @Override
    public ReflectionsWrapper configure() {
        reflections = (Reflections) cache.get(Reflections.class.getName());

        if (reflections == null) {
            ConfigurationBuilder cb = new ConfigurationBuilder();

            Set<URL> classLocations = new LinkedHashSet<>();

            try {
                List<URL> urls = Collections.list(Thread.currentThread().getContextClassLoader().getResources(""));

                for (URL url : urls) {
                    if (url.getPath().contains("/geemvc/target/")) {
                        classLocations.add(url);
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }

            cb = cb.addUrls(classLocations).addClassLoader(Thread.currentThread().getContextClassLoader());
            cb = cb.setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new MethodAnnotationsScanner(), new SubTypesScanner());
            reflections = cb.build();

//            Reflections cachedReflections = (Reflections)
              cache.putIfAbsent(Reflections.class.getName(), reflections);

//            if (cachedReflections != null)
//                reflections = cachedReflections;
        }

        return this;
    }

}
