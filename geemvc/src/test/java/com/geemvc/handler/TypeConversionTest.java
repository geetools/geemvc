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

package com.geemvc.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.geemvc.converter.ConverterAdapter;
import com.geemvc.converter.SimpleConverter;
import org.junit.Test;

import com.geemvc.converter.ConverterAdapterFactory;
import com.geemvc.converter.adapter.ByteArrayConverterAdapter;
import com.geemvc.converter.adapter.CharArrayConverterAdapter;
import com.geemvc.converter.adapter.CollectionConverterAdapter;
import com.geemvc.converter.adapter.DoubleArrayConverterAdapter;
import com.geemvc.converter.adapter.FloatArrayConverterAdapter;
import com.geemvc.converter.adapter.IntArrayConverterAdapter;
import com.geemvc.converter.adapter.LongArrayConverterAdapter;
import com.geemvc.converter.adapter.MapConverterAdapter;
import com.geemvc.converter.adapter.ShortArrayConverterAdapter;
import com.geemvc.mock.Id;
import com.geemvc.mock.bean.RootBean;
import com.geemvc.test.BaseTest;

public class TypeConversionTest extends BaseTest {
    @Test
    public void testSimpleConverter() {
        SimpleConverter converter = instance(SimpleConverter.class);

        // Integer
        assertNotNull(converter.fromString("1", Integer.class));
        assertEquals(Integer.class, converter.fromString("1", Integer.class).getClass());
        assertEquals(Integer.valueOf("1"), converter.fromString("1", Integer.class));

        // Long
        assertNotNull(converter.fromString("1", Long.class));
        assertEquals(Long.class, converter.fromString("1", Long.class).getClass());
        assertEquals(Long.valueOf("1"), converter.fromString("1", Long.class));

        // Float
        assertNotNull(converter.fromString("1.1", Float.class));
        assertEquals(Float.class, converter.fromString("1.1", Float.class).getClass());
        assertEquals(Float.valueOf("1.1"), converter.fromString("1.1", Float.class));

        // Double
        assertNotNull(converter.fromString("1.1", Double.class));
        assertEquals(Double.class, converter.fromString("1.1", Double.class).getClass());
        assertEquals(Double.valueOf("1.1"), converter.fromString("1.1", Double.class));

        // Boolean
        assertNotNull(converter.fromString("true", Boolean.class));
        assertEquals(Boolean.class, converter.fromString("true", Boolean.class).getClass());
        assertEquals(Boolean.valueOf("true"), converter.fromString("true", Boolean.class));

        // BigInteger
        assertNotNull(converter.fromString("1", BigInteger.class));
        assertEquals(BigInteger.class, converter.fromString("1", BigInteger.class).getClass());
        assertEquals(BigInteger.valueOf(1L), converter.fromString("1", BigInteger.class));

        // BigDecimal
        assertNotNull(converter.fromString("1", BigDecimal.class));
        assertEquals(BigDecimal.class, converter.fromString("1", BigDecimal.class).getClass());
        assertEquals(BigDecimal.valueOf(1.1D), converter.fromString("1.1", BigDecimal.class));

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Date #1
        assertNotNull(converter.fromString("2016-01-01", Date.class, "yyyy-MM-dd"));
        assertEquals(Date.class, converter.fromString("2016-01-01", Date.class, "yyyy-MM-dd").getClass());
        assertEquals("2016-01-01", sdf.format(converter.fromString("2016-01-01", Date.class, "yyyy-MM-dd")));

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Date #2 - TODO: !!
        // assertNotNull(converter.fromString("2016-01-01T00:00:00Z", Date.class, "yyyy-MM-dd'T'HH:mm:ss'Z'"));
        // assertEquals(Date.class, converter.fromString("2016-01-01T00:00:00Z", Date.class,
        // "yyyy-MM-dd'T'HH:mm:ss'Z'").getClass());
        // assertEquals("2016-01-01T00:00:00Z", sdf.format(converter.fromString("2016-01-01T00:00:00Z", Date.class,
        // "yyyy-MM-dd'T'HH:mm:ss'Z'")));

        sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);

        // Date #3 - TODO: !!
        // assertNotNull(converter.fromString("01.01.2016", Date.class, "dd.MM.yyyy", Locale.GERMANY));
        // assertEquals(Date.class, converter.fromString("01.01.2016", Date.class, "dd.MM.yyyy",
        // Locale.GERMANY).getClass());
        // assertEquals("01.01.2016", sdf.format(converter.fromString("01.01.2016", Date.class, "dd.MM.yyyy",
        // Locale.GERMANY)));
    }

    protected List<String> stringList;
    protected List<Object> objectList;
    protected List<int[]> intArrayList;
    protected List<Boolean> booleanList;
    protected List<RootBean> rootBeanList;
    protected List<Map<String, RootBean>> listOfBeanMaps;

    protected Map<String, String> stringMap;
    protected Map<Object, Object> objectMap;
    protected Map<Id, int[]> idIntArrayMap;
    protected Map<String, Boolean> stringBooleanMap;
    protected Map<Long, RootBean> rootBeanMap;
    protected Map<Id, List<RootBean>> rootBeanMapList;

    protected int[] intArray;
    protected long[] longArray;
    protected float[] floatArray;
    protected double[] doubleArray;
    protected short[] shortArray;
    protected byte[] byteArray;
    protected char[] charArray;

    @SuppressWarnings("rawtypes")
    @Test
    public void testConverterFactoryAdapter() {
        ConverterAdapterFactory converterAdapterFactory = instance(ConverterAdapterFactory.class);

        // ---------------------------------------------------------------
        // List<String>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("stringList").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(CollectionConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // List<Object>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("objectList").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(CollectionConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // List<int[]>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("intArrayList").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(CollectionConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // List<Boolean>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("booleanList").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(CollectionConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // List<RootBean>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("rootBeanList").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(CollectionConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // List<Map<String, RootBean>>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("listOfBeanMaps").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(CollectionConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // Map<String, String>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("stringMap").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(MapConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // Map<Object, Object>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("objectMap").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(MapConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // Map<Id, int[]>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("idIntArrayMap").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(MapConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // Map<String, Boolean>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("stringBooleanMap").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(MapConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // Map<Long, RootBean>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("rootBeanMap").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(MapConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // Map<Id, List<RootBean>>
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("rootBeanMapList").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(MapConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // int[]
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("intArray").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(IntArrayConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // long[]
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("longArray").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(LongArrayConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // float[]
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("floatArray").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(FloatArrayConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // double[]
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("doubleArray").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(DoubleArrayConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // short[]
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("shortArray").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(ShortArrayConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // byte[]
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("byteArray").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(ByteArrayConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }

        // ---------------------------------------------------------------
        // char[]
        // ---------------------------------------------------------------
        try {
            ConverterAdapter converterAdapter = converterAdapterFactory.create(List.class, TypeConversionTest.class.getField("charArray").getGenericType());
            assertNotNull(converterAdapter);
            assertEquals(CharArrayConverterAdapter.class, converterAdapter.getClass());
        } catch (NoSuchFieldException | SecurityException e) {
        }
    }
}
