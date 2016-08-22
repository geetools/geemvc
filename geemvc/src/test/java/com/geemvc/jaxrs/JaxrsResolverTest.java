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

package com.geemvc.jaxrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;
import javax.ws.rs.ext.RuntimeDelegate;

import org.junit.Test;

import com.geemvc.mock.bean.Address;
import com.geemvc.mock.bean.Person;
import com.geemvc.reflect.ReflectionProvider;
import com.geemvc.rest.jaxrs.ProviderFilter;
import com.geemvc.rest.jaxrs.delegate.DefaultRuntimeDelegate;
import com.geemvc.test.BaseTest;
import com.owlike.genson.Genson;

/**
 * Created by Michael on 16.08.2016.
 */
public class JaxRsResolverTest extends BaseTest {
    @SuppressWarnings("unused")
    private Map<String, Person> genericField1 = null;
    private Map<String, String> genericField2 = null;
    private Person genericField3 = null;
    private Address genericField4 = null;

    @Test
    public void testLocateGensonContextResolver() {
        Providers providers = instance(Providers.class);

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        ContextResolver contextResolver = providers.getContextResolver(Genson.class, MediaType.APPLICATION_JSON_TYPE);

        assertNotNull(contextResolver);
        assertEquals(TestGensonContextResolver3.class, contextResolver.getClass());
    }

    @Test
    public void testLocateObjectContextResolver() {
        Providers providers = instance(Providers.class);

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        ContextResolver contextResolver = providers.getContextResolver(Person.class, MediaType.APPLICATION_JSON_TYPE);

        assertNotNull(contextResolver);
        assertEquals(TestGensonContextResolver6.class, contextResolver.getClass());
    }

    @Test
    public void testLocateApplicationContextResolver() {
        Providers providers = instance(Providers.class);

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        ContextResolver contextResolver = providers.getContextResolver(Application.class, MediaType.APPLICATION_JSON_TYPE);

        assertNotNull(contextResolver);
        assertEquals(TestApplicationContextResolver.class, contextResolver.getClass());
    }

    @Test
    public void testLocateClosestPersonMapMessageBodyReaderJSON() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField1");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.APPLICATION_JSON_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(ModelMapMessageBodyReader.class, messageBodyReader.getClass());
    }

    @Test
    public void testLocateClosestPersonMapMessageBodyReaderXML() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField1");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.APPLICATION_XML_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(PersonMapMessageBodyReader.class, messageBodyReader.getClass());
    }

    @Test
    public void testLocateModelMapMessageBodyReader() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField1");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.APPLICATION_JSON_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(ModelMapMessageBodyReader.class, messageBodyReader.getClass());
    }

    @Test
    public void testLocateStringMapMessageBodyReader() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField2");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.APPLICATION_JSON_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(MapMessageBodyReader.class, messageBodyReader.getClass());
    }

    @Test
    public void testLocateClosestPersonMessageBodyReaderJSON() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField3");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.APPLICATION_JSON_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(AbstractModelMessageBodyReader.class, messageBodyReader.getClass());
    }

    @Test
    public void testLocateClosestPersonMessageBodyReaderXML() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField3");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.APPLICATION_XML_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(PersonMessageBodyReader.class, messageBodyReader.getClass());
    }

    @Test
    public void testLocateClosestPersonMessageBodyReaderTextPlain() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField3");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.TEXT_PLAIN_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(AbstractModelMessageBodyReader.class, messageBodyReader.getClass());
    }

    @Test
    public void testLocateClosestAddressMessageBodyReaderJSON() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField4");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.APPLICATION_JSON_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(ObjectMessageBodyReaderJSON.class, messageBodyReader.getClass());
    }

    @Test
    public void testLocateClosestAddressMessageBodyReaderXML() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField4");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.APPLICATION_XML_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(ObjectMessageBodyReaderXML.class, messageBodyReader.getClass());
    }

    @Test
    public void testLocateClosestAddressMessageBodyReaderTextPlain() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField4");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.TEXT_PLAIN_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(ObjectMessageBodyReaderWildCard.class, messageBodyReader.getClass());
    }

    @Test
    public void testLocateMapMessageBodyWriter() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField1");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyWriter messageBodyWriter = providers.getMessageBodyWriter(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.WILDCARD_TYPE);

        assertNotNull(messageBodyWriter);
        assertEquals(MapMessageBodyWriter.class, messageBodyWriter.getClass());
    }

    @Test
    public void testLocateMostRelevantConstructor() {
        ReflectionProvider rp = instance(ReflectionProvider.class);

        Providers providers = instance(Providers.class);
        ProviderFilter providerFilter = instance(ProviderFilter.class);

        Field f = rp.getField(JaxRsResolverTest.class, "genericField3");

        RuntimeDelegate.setInstance(new DefaultRuntimeDelegate());

        MessageBodyReader messageBodyReader = providers.getMessageBodyReader(f.getType(), f.getGenericType(), f.getAnnotations(), MediaType.APPLICATION_XML_TYPE);

        assertNotNull(messageBodyReader);
        assertEquals(PersonMessageBodyReader.class, messageBodyReader.getClass());

        Constructor<?> c = providerFilter.mostRelevantConstructor(messageBodyReader.getClass());
        assertEquals(4, c.getParameterCount());
        assertEquals("public com.geemvc.jaxrs.PersonMessageBodyReader(javax.ws.rs.core.Application,javax.ws.rs.core.UriInfo,javax.ws.rs.core.HttpHeaders,javax.ws.rs.ext.Providers)", c.toString());
    }
}
