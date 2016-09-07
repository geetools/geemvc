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

package com.geemvc.test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.naming.java.javaURLContextFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.geemvc.RequestContext;
import com.geemvc.ThreadStash;
import com.geemvc.annotation.Request;
import com.geemvc.config.Configuration;
import com.geemvc.config.Configurations;
import com.geemvc.helper.Annotations;
import com.geemvc.helper.TestHelper;
import com.geemvc.inject.InjectorProvider;
import com.geemvc.inject.Injectors;
import com.geemvc.inject.TestModule;
import com.geemvc.matcher.PathMatcher;
import com.geemvc.matcher.PathMatcherKey;
import com.geemvc.mock.bean.Person;
import com.geemvc.reflect.ReflectionsWrapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class BaseTest {
    protected static Injector injector = null;

    protected <T> T instance(Class<T> type) {
        return injector.getInstance(type);
    }

    protected RequestContext newRequestContext(String contextPath, String servletPath, String requestURI) {
        return injector.getInstance(TestHelper.class).newRequestContext(contextPath, servletPath, requestURI);
    }

    protected RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, List<Cookie> cookies) {
        return injector.getInstance(TestHelper.class).newRequestContext(contextPath, servletPath, requestURI, cookies.toArray(new Cookie[cookies.size()]));
    }

    protected RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, Map<String, String[]> params) {
        return injector.getInstance(TestHelper.class).newRequestContext(contextPath, servletPath, requestURI, params);
    }

    protected RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, Map<String, String[]> params, Map<String, String[]> headers) {
        return injector.getInstance(TestHelper.class).newRequestContext(contextPath, servletPath, requestURI, params, headers);
    }

    protected RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, String method) {
        return injector.getInstance(TestHelper.class).newRequestContext(contextPath, servletPath, requestURI, method);
    }

    protected RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, String method, List<Cookie> cookies) {
        return injector.getInstance(TestHelper.class).newRequestContext(contextPath, servletPath, requestURI, method, cookies.toArray(new Cookie[cookies.size()]));
    }

    protected RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, String method, Map<String, String[]> params) {
        return injector.getInstance(TestHelper.class).newRequestContext(contextPath, servletPath, requestURI, method, params);
    }

    protected RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, String method, Map<String, String[]> params, Map<String, String[]> headers, List<Cookie> cookies) {
        return injector.getInstance(TestHelper.class).newRequestContext(contextPath, servletPath, requestURI, method, params, headers, cookies.toArray(new Cookie[cookies.size()]));
    }

    protected RequestContext newRequestContext(String contextPath, String servletPath, String requestURI, String method, Map<String, String[]> params, Map<String, String[]> headers) {
        return injector.getInstance(TestHelper.class).newRequestContext(contextPath, servletPath, requestURI, method, params, headers);
    }

    protected String firstMappedPath(Map<PathMatcherKey, Class<?>> controllers) {
        if (controllers == null || controllers.isEmpty())
            return null;

        Set<PathMatcherKey> keys = controllers.keySet();
        PathMatcherKey pathMatcherKey = keys.iterator().next();

        return pathMatcherKey.matcher().getMappedPath();
    }

    protected String firstMappedPath(Request requestMapping) {
        return injector.getInstance(Annotations.class).path(requestMapping);
    }

    protected String firstRegexPath(Map<PathMatcherKey, Class<?>> controllers) {
        if (controllers == null || controllers.isEmpty())
            return null;

        Set<PathMatcherKey> keys = controllers.keySet();
        PathMatcherKey pathMatcherKey = keys.iterator().next();

        return pathMatcherKey.matcher().getRegexPath();
    }

    protected String firstControllerPath(Collection<Class<?>> controllerClasses) {
        if (controllerClasses == null || controllerClasses.isEmpty())
            return null;

        return controllerPath(controllerClasses.iterator().next());
    }

    protected String controllerPath(Class<?> controllerClass) {
        if (controllerClass == null)
            return null;

        return injector.getInstance(Annotations.class).path(requestMapping(controllerClass));
    }

    protected boolean controllerPathExists(String mappedPath, Collection<Class<?>> controllerClasses) {
        return findController(mappedPath, controllerClasses) != null;
    }

    protected boolean mappedPathExists(String mappedPath, Map<PathMatcherKey, Class<?>> controllers) {
        return findPathMatcher(mappedPath, controllers) != null;
    }

    protected boolean mappedPathExists(String mappedPath, PathMatcher pathMatcher) {
        if (mappedPath == null || pathMatcher == null)
            return false;

        return mappedPath.equals(pathMatcher.getMappedPath());
    }

    protected boolean mappedPathExists(String mappedPath, Request requestMapping) {
        if (mappedPath == null || requestMapping == null)
            return false;

        return injector.getInstance(Annotations.class).path(requestMapping).equals(mappedPath);
    }

    protected boolean mappedParameterExists(String mappedParam, Request requestMapping) {
        if (mappedParam == null || requestMapping == null)
            return false;

        String[] params = requestMapping.params();

        return params == null || params.length == 0 ? false : Arrays.asList(params).contains(mappedParam);
    }

    protected boolean mappedParametersExists(String[] mappedParams, Request requestMapping) {
        if (mappedParams == null || requestMapping == null || requestMapping.params() == null)
            return false;

        if (mappedParams.length != requestMapping.params().length)
            return false;

        List<String> params = Arrays.asList(requestMapping.params());

        for (String mappedParam : mappedParams) {
            if (!params.contains(mappedParam))
                return false;
        }

        return true;
    }

    protected boolean mappedHeaderExists(String mappedHeader, Request requestMapping) {
        if (mappedHeader == null || requestMapping == null)
            return false;

        String[] headers = requestMapping.headers();

        return headers == null || headers.length == 0 ? false : Arrays.asList(headers).contains(mappedHeader);
    }

    protected boolean mappedHeadersExists(String[] mappedHeaders, Request requestMapping) {
        if (mappedHeaders == null || requestMapping == null || requestMapping.headers() == null)
            return false;

        if (mappedHeaders.length != requestMapping.headers().length)
            return false;

        List<String> headers = Arrays.asList(requestMapping.headers());

        for (String mappedHeader : mappedHeaders) {
            if (!headers.contains(mappedHeader))
                return false;
        }

        return true;
    }

    protected boolean mappedCookieExists(String mappedCookie, Request requestMapping) {
        if (mappedCookie == null || requestMapping == null)
            return false;

        String[] cookies = requestMapping.cookies();

        return cookies == null || cookies.length == 0 ? false : Arrays.asList(cookies).contains(mappedCookie);
    }

    protected boolean mappedCookiesExists(String[] mappedCookies, Request requestMapping) {
        if (mappedCookies == null || requestMapping == null || requestMapping.cookies() == null)
            return false;

        if (mappedCookies.length != requestMapping.cookies().length)
            return false;

        List<String> cookies = Arrays.asList(requestMapping.cookies());

        for (String mappedCookie : mappedCookies) {
            if (!cookies.contains(mappedCookie))
                return false;
        }

        return true;
    }

    protected boolean mappedHandlesExists(String mappedHandles, Request requestMapping) {
        if (mappedHandles == null || requestMapping == null)
            return false;

        String handles = requestMapping.handles();

        return handles == null || handles.length() == 0 ? false : handles.equals(mappedHandles);
    }

    protected boolean regexPathIsNull(String mappedPath, Map<PathMatcherKey, Class<?>> controllers) {
        PathMatcher pathMatcher = findPathMatcher(mappedPath, controllers);

        return pathMatcher != null && pathMatcher.getRegexPath() == null;
    }

    protected Class<?> findController(String mappedPath, Collection<Class<?>> controllerClasses) {
        if (mappedPath == null || controllerClasses == null || controllerClasses.isEmpty())
            return null;

        for (Class<?> controllerClass : controllerClasses) {
            String path = injector.getInstance(Annotations.class).path(requestMapping(controllerClass));

            if (path.equals(mappedPath))
                return controllerClass;
        }

        return null;
    }

    protected PathMatcher findPathMatcher(String mappedPath, Map<PathMatcherKey, Class<?>> controllers) {
        if (mappedPath == null || controllers == null || controllers.isEmpty())
            return null;

        Set<PathMatcherKey> keys = controllers.keySet();

        for (PathMatcherKey pathMatcherKey : keys) {
            if (mappedPath.equals(pathMatcherKey.matcher().getMappedPath()))
                return pathMatcherKey.matcher();
        }

        return null;
    }

    protected Request requestMapping(Class<?> controllerClass) {
        if (controllerClass.isAnnotationPresent(Request.class)) {
            return controllerClass.getAnnotation(Request.class);
        } else if (controllerClass.isAnnotationPresent(Path.class) || controllerClass.isAnnotationPresent(GET.class) || controllerClass.isAnnotationPresent(POST.class) || controllerClass.isAnnotationPresent(PUT.class)
                || controllerClass.isAnnotationPresent(DELETE.class) || controllerClass.isAnnotationPresent(HEAD.class) || controllerClass.isAnnotationPresent(OPTIONS.class)) {
            return injector.getInstance(Annotations.class).requestMapping(controllerClass);
        }

        return null;
    }

    protected Request requestMapping(Method method) {
        if (method.isAnnotationPresent(Request.class)) {
            return method.getAnnotation(Request.class);
        } else if (method.isAnnotationPresent(Path.class) || method.isAnnotationPresent(GET.class) || method.isAnnotationPresent(POST.class) || method.isAnnotationPresent(PUT.class) || method.isAnnotationPresent(DELETE.class)
                || method.isAnnotationPresent(HEAD.class) || method.isAnnotationPresent(OPTIONS.class)) {
            return injector.getInstance(Annotations.class).requestMapping(method);
        }

        return null;
    }

    protected static void initializeInjector() {
        if (injector == null) {
            injector = Guice.createInjector(new TestModule(), new JpaPersistModule("testPU"));

            Injectors.set(new InjectorProvider() {

                @Override
                public Injector provide() {
                    return injector;
                }
            });

            injector.getInstance(PersistService.class).start();
        }
    }

    protected static void initializeReflections() {
        injector.getInstance(ReflectionsWrapper.class).configure();
    }

    @Before
    public void setUp() throws Exception {
        ThreadStash.prepare((HttpServletRequest) newRequestContext("/webapp", "/servlet", "/webapp/servlet/test").getRequest());
    }

    @After
    public void tearDown() throws Exception {
        ThreadStash.clear();
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        Configurations.set(new Configuration() {

            @Override
            public String viewSuffix() {
                return ".jsp";
            }

            @Override
            public String viewPrefix() {
                return "/jsp/pages";
            }

            @Override
            public List<String> supportedUriSuffixes() {
                return Arrays.asList(".html");
            }

            @Override
            public Set<Locale> supportedLocales() {
                return new HashSet<>(Arrays.asList(Locale.ENGLISH, Locale.GERMAN));
            }

            @Override
            public List<String> reflectionsLibIncludes() {
                return null;
            }

            @Override
            public List<String> reflectionsLibExcludes() {
                return null;
            }

            @Override
            public boolean isJaxRsEnabled() {
                return true;
            }

            @Override
            public InjectorProvider injectorProvider() {
                return null;
            }

            @Override
            public Set<String> excludePathMappinig() {
                return null;
            }

            @Override
            public String defaultContentType() {
                return "text/html";
            }

            @Override
            public String defaultCharacterEncoding() {
                return "UTF-8";
            }

            @Override
            public String characterEncodingFor(Locale locale) {
                return "UTF-8";
            }

            @Override
            public Configuration build(Map<String, String> configurationMap) {
                return null;
            }
        });

        setupInitialContext();
        initializeInjector();
        initializeReflections();
    }

    @AfterClass
    public static void afterClass() throws Exception {
    }

    protected void setupDatasource() throws Exception {
        destroyDatasource();

        InitialContext ic = new InitialContext();

        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:testDB");
        ds.setCreateDatabase("create");

        ic.bind("java:comp/env/jdbc/testDS", ds);

    }

    protected static void setupInitialContext() throws Exception {
        if (System.getProperty(Context.INITIAL_CONTEXT_FACTORY) == null) {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, javaURLContextFactory.class.getName());
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
            InitialContext ic = new InitialContext();

            ic.createSubcontext("java:");
            ic.createSubcontext("java:comp");
            ic.createSubcontext("java:comp/env");
            ic.createSubcontext("java:comp/env/jdbc");

            EmbeddedDataSource ds = new EmbeddedDataSource();
            ds.setDatabaseName("memory:testDB");
            ds.setCreateDatabase("create");

            ic.bind("java:comp/env/jdbc/testDS", ds);

        }
    }

    protected void initTestData() {
        EntityManager em = injector.getInstance(EntityManager.class);

        EntityTransaction transaction = em.getTransaction();

        // ---------------------------------------------------------------
        // Truncate table
        // ---------------------------------------------------------------

        transaction.begin();

        em.createNativeQuery("TRUNCATE TABLE person").executeUpdate();

        transaction.commit();

        // ---------------------------------------------------------------
        // Insert data
        // ---------------------------------------------------------------

        transaction.begin();

        // Populate some test data.
        Person p1 = new Person();
        p1.setId(1l);
        p1.setForename("Michael");
        p1.setSurname("Delamere");
        p1.setAge(10);
        em.persist(p1);

        Person p2 = new Person();
        p2.setId(2l);
        p2.setForename("Tom");
        p2.setSurname("Checker");
        p2.setAge(1);
        em.persist(p2);

        Person p3 = new Person();
        p3.setId(3l);
        p3.setForename("Marc");
        p3.setSurname("Dude");
        p3.setAge(4);
        em.persist(p3);

        Person p4 = new Person();
        p4.setId(4l);
        p4.setForename("Lea");
        p4.setSurname("Cool");
        p4.setAge(8);
        em.persist(p4);

        transaction.commit();
    }

    protected void destroyDatasource() throws Exception {
        EntityManager em = injector.getInstance(EntityManager.class);
        em.clear();
        // em.close();

        InitialContext ic = new InitialContext();
        ic.unbind("java:comp/env/jdbc/testDS");
    }
}
