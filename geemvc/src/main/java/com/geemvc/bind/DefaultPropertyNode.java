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

package com.geemvc.bind;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.geemvc.Char;
import com.geemvc.Str;
import com.geemvc.Val;
import com.geemvc.reflect.ReflectionProvider;
import com.google.inject.Inject;
import com.google.inject.Injector;

import jodd.bean.BeanUtil;

public class DefaultPropertyNode implements PropertyNode {
    protected String name;

    protected String expression;

    protected String originalExpression;

    protected List<String> expressionParts;

    protected int index = 0;

    protected boolean isLeaf = false;

    protected Class<?> type;

    protected List<Class<?>> genericType;

    protected PropertyNode previous;

    protected PropertyNode next;

    protected PropertyDescriptor propertyDescriptor;

    protected final ReflectionProvider reflectionProvider;

    protected static final Pattern stripBracketsPattern = Pattern.compile("\\[[^\\[\\]]*\\]");

    @Inject
    Injector injector;

    @Inject
    protected DefaultPropertyNode(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    @Override
    public PropertyNode build(String expression, Class<?> type) {
        int pos = expression.indexOf(Char.DOT);

        this.originalExpression = expression;
        expression = stripBrackets(expression);

        if (pos != -1) {
            // Split the expression into individual parts.
            this.expressionParts = Arrays.asList(expression.split("\\."));
            // Get the name of the current part. In this case the first (root) one.
            this.name = expressionParts.get(0);
            this.expression = expressionParts.get(0);

            // Get reflection information for current (root) part.
            PropertyDescriptor pd = reflectionProvider.getPropertyDescriptor(type, name);

            if (pd == null)
                throw new IllegalStateException("Property '" + name + "' could not be found in " + type.getName() + ". Please check your expression '" + originalExpression + "'");

            this.type = pd.getPropertyType();
            // We also need to find out the generic types in case the current property is a collection or map.
            this.genericType = reflectionProvider.getGenericType(type, name);

            // Move onto the next part of the expression.
            this.next = injector.getInstance(PropertyNode.class).build(expression, this, expressionParts);
        } else {
            this.expression = expression;
            this.expressionParts = new ArrayList<>();
            this.expressionParts.add(expression);
            this.name = expression;

            PropertyDescriptor pd = reflectionProvider.getPropertyDescriptor(type, name);

            if (pd == null)
                throw new IllegalStateException("Property '" + name + "' could not be found in " + type.getName() + ". Please check your expression '" + originalExpression + "'");

            this.type = pd.getPropertyType();
            // We also need to find out the generic types in case the current property is a collection or map.
            this.genericType = reflectionProvider.getGenericType(type, name);
            this.isLeaf = true;
        }

        return this;
    }

    @Override
    public PropertyNode build(String expression, PropertyNode previous, List<String> expressionParts) {
        // Position in expression.
        this.index = previous.index() + 1;
        // Property name of the current expression part.
        this.name = expressionParts.get(this.index);
        this.originalExpression = previous.originalExpression();
        // Set the full list of expression parts on the current node for future reference.
        this.expressionParts = expressionParts;
        // Is this the final node (expression part)?
        this.isLeaf = this.index == expressionParts.size() - 1;

        // If the previous expression part was a collection or map, we need to use the generic type to find a
        // matching property for the current node (expression part).
        if (previous.type().isArray()) {
            if (previous.type().getComponentType() != null) {
                PropertyDescriptor pd = reflectionProvider.getPropertyDescriptor(previous.type().getComponentType(), name);

                if (pd == null)
                    throw new IllegalStateException("Property '" + name + "' could not be found in " + type.getName() + ". Please check your expression '" + originalExpression + "'");

                this.type = pd.getPropertyType();
                this.genericType = reflectionProvider.getGenericType(previous.type().getComponentType(), name);
            } else {
                // TODO: throw exception. Must provide generic type for collection and map.
            }
        } else if (Collection.class.isAssignableFrom(previous.type())) {
            if (previous.genericType() != null) {
                PropertyDescriptor pd = reflectionProvider.getPropertyDescriptor(previous.genericType().get(0), name);

                if (pd == null)
                    throw new IllegalStateException("Property '" + name + "' could not be found in " + type.getName() + ". Please check your expression '" + originalExpression + "'");

                this.type = pd.getPropertyType();
                this.genericType = reflectionProvider.getGenericType(previous.genericType().get(0), name);
            } else {
                // TODO: throw exception. Must provide generic type for collection and map.
            }
        } else if (Map.class.isAssignableFrom(previous.type())) {
            PropertyDescriptor pd = reflectionProvider.getPropertyDescriptor(previous.genericType().get(1), name);

            if (pd == null)
                throw new IllegalStateException("Property '" + name + "' could not be found in " + type.getName() + ". Please check your expression '" + originalExpression + "'");

            this.type = pd.getPropertyType();
            this.genericType = reflectionProvider.getGenericType(previous.genericType().get(1), name);
        } else {
            PropertyDescriptor pd = reflectionProvider.getPropertyDescriptor(previous.type(), name);

            if (pd == null)
                throw new IllegalStateException("Property '" + name + "' could not be found in " + type.getName() + ". Please check your expression '" + originalExpression + "'");

            this.type = pd.getPropertyType();
            this.genericType = reflectionProvider.getGenericType(previous.type(), name);
        }

        // Get sub-expression to current expression part (node).
        this.expression = pathToCurrentNode(expressionParts, this.index);

        // If we have not reached the leaf node yet, continue with the next part.
        if (expressionParts.size() - 1 > this.index) {
            this.next = injector.getInstance(PropertyNode.class).build(expression, this, expressionParts);
        }

        return this;
    }

    @Override
    public Object get(Object bean) {
        PropertyNode currentNode = this;
        Object currentBean = bean;

        while (!currentNode.isLeafNode() && currentNode.next() != null) {
            currentBean = BeanUtil.declared.getProperty(currentBean, currentNode.name());
            currentNode = currentNode.next();
        }

        if (currentNode.isLeafNode() && currentBean != null) {
            return BeanUtil.declared.getProperty(currentBean, currentNode.name());
        } else {
            return null;
        }
    }

    @Override
    public void set(Object bean, Object value) {
        if (!isLeaf) {
            Object nodeBean = BeanUtil.declared.getProperty(bean, name);

            if (nodeBean == null) {
                nodeBean = injector.getInstance(type);
                BeanUtil.declared.setProperty(bean, name, nodeBean);
            }

            next.set(nodeBean, value);
        } else {
            BeanUtil.declared.setProperty(bean, name, validValue(value, type));
        }
    }

    @Override
    public void set(Object bean, Object value, String expression) {
        if (!isLeaf) {
            if (type.isArray()) {
                Object list = ensureExists(bean, name, type);

                String extendedName = extendedName(expression);

                if (extendedName.endsWith(Str.SQUARE_BRACKET_OPEN_CLOSE))
                    extendedName = extendedName.replace(Str.SQUARE_BRACKET_OPEN_CLOSE, new StringBuilder(Str.SQUARE_BRACKET_OPEN).append(((Collection<?>) list).size()).append(Char.SQUARE_BRACKET_CLOSE).toString());

                Object nodeBean = BeanUtil.declaredSilent.getProperty(bean, extendedName);

                if (nodeBean == null)
                    nodeBean = injector.getInstance(type.getComponentType());

                BeanUtil.forced.setProperty(bean, extendedName, nodeBean);

                next.set(nodeBean, value, expression);
            } else if (Collection.class.isAssignableFrom(type)) {
                Object list = ensureExists(bean, name, type);

                String extendedName = extendedName(expression);

                if (extendedName.endsWith(Str.SQUARE_BRACKET_OPEN_CLOSE))
                    extendedName = extendedName.replace(Str.SQUARE_BRACKET_OPEN_CLOSE, new StringBuilder(Str.SQUARE_BRACKET_OPEN).append(((Collection<?>) list).size()).append(Char.SQUARE_BRACKET_CLOSE).toString());

                Object nodeBean = BeanUtil.silent.getProperty(bean, extendedName);

                if (nodeBean == null)
                    nodeBean = injector.getInstance(genericType.get(0));

                BeanUtil.forced.setProperty(bean, extendedName, nodeBean);

                next.set(nodeBean, value, expression);
            } else if (Map.class.isAssignableFrom(type)) {
                // Object map = ensureExists(bean, name, type);

                String extendedName = extendedName(expression);
                Object nodeBean = BeanUtil.silent.getProperty(bean, extendedName);

                if (nodeBean == null)
                    nodeBean = injector.getInstance(genericType.get(1));

                BeanUtil.forced.setProperty(bean, extendedName, nodeBean);

                next.set(nodeBean, value, expression);
            } else {
                Object nodeBean = ensureExists(bean, name, type);

                next.set(nodeBean, value, expression);
            }
        } else {
            if (type.isArray()) {
                Object array = ensureExists(bean, name, type);

                String extendedName = extendedName(expression);

                if (extendedName.endsWith(Str.SQUARE_BRACKET_OPEN_CLOSE))
                    extendedName = extendedName.replace(Str.SQUARE_BRACKET_OPEN_CLOSE, new StringBuilder(Str.SQUARE_BRACKET_OPEN).append(Array.getLength(array)).append(Char.SQUARE_BRACKET_CLOSE).toString());

                BeanUtil.forced.setProperty(bean, extendedName, validValue(value, type));
            } else if (Collection.class.isAssignableFrom(type)) {
                Object list = ensureExists(bean, name, type);

                String extendedName = extendedName(expression);

                if (extendedName.endsWith(Str.SQUARE_BRACKET_OPEN_CLOSE))
                    extendedName = extendedName.replace(Str.SQUARE_BRACKET_OPEN_CLOSE, new StringBuilder(Str.SQUARE_BRACKET_OPEN).append(((Collection<?>) list).size()).append(Char.SQUARE_BRACKET_CLOSE).toString());

                BeanUtil.forced.setProperty(bean, extendedName, validValue(value, type));
            } else {
                BeanUtil.declared.setProperty(bean, name, validValue(value, type));
            }
        }
    }

    protected Object validValue(Object value, Class<?> targetType) {
        if (!targetType.isPrimitive() && !targetType.isEnum())
            return value;

        if (value instanceof String && !Str.isEmpty((String) value) && !Str.NULL_STRING.equals(value)) {
            return value;
        } else if (value != null && !(value instanceof String)) {
            return value;
        }

        if (targetType == byte.class) {
            return Val.DEFAULT_BYTE;
        } else if (targetType == short.class) {
            return Val.DEFAULT_SHORT;
        } else if (targetType == int.class) {
            return Val.DEFAULT_INT;
        } else if (targetType == long.class) {
            return Val.DEFAULT_LONG;
        } else if (targetType == float.class) {
            return Val.DEFAULT_FLOAT;
        } else if (targetType == double.class) {
            return Val.DEFAULT_DOUBLE;
        } else if (targetType == boolean.class) {
            return Val.DEFAULT_BOOEAN;
        } else if (targetType == char.class) {
            return Val.DEFAULT_CHAR;
        } else if (targetType.isEnum()) {
            return asEnum((Class<? extends Enum>) targetType, value);
        }

        return null;
    }

    protected <E extends Enum<E>> E asEnum(Class<E> enumType, Object value) {
        if (value.getClass().isEnum()) {
            return (E) value;
        } else if (value instanceof String) {
            String enumVal = (String) value;

            if (Str.isEmpty(enumVal))
                return null;

            try {
                Method m = enumType.getDeclaredMethod("fromString", String.class);
                return (E) m.invoke(null, enumVal);
            } catch (NoSuchMethodException e) {
                return Enum.valueOf(enumType, enumVal);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        } else if (value instanceof Integer) {
            Integer enumVal = (Integer) value;

            try {
                Method m = enumType.getDeclaredMethod("fromId", int.class);
                return (E) m.invoke(null, enumVal);
            } catch (NoSuchMethodException e) {
                // Try finding by ordinal() if fromId() does not exist.
                E[] enums = enumType.getEnumConstants();

                for (E constant : enums) {
                    if (constant.ordinal() == enumVal) {
                        return constant;
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

            return null;
        } else {
            throw new IllegalStateException("Unable to convert object of type " + value.getClass().getName() + " to " + enumType.getName());
        }
    }

    protected String stripBrackets(String expression) {
        if (expression.indexOf(Char.SQUARE_BRACKET_OPEN) == -1)
            return expression;

        Matcher m = stripBracketsPattern.matcher(expression);
        return m.replaceAll(Str.EMPTY);
    }

    protected Object ensureExists(Object bean, String propertyName, Class<?> targetType) {
        Object propertyValue = BeanUtil.declared.getProperty(bean, propertyName);

        if (propertyValue != null)
            return propertyValue;

        if (targetType.isArray()) {
            Class<?> arrayType = targetType.getComponentType();
            propertyValue = Array.newInstance(arrayType, 0);
        } else if (List.class.isAssignableFrom(targetType)) {
            try {
                propertyValue = targetType.isInterface() ? new ArrayList<>() : targetType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (SortedSet.class.isAssignableFrom(targetType)) {
            try {
                propertyValue = targetType.isInterface() ? new TreeSet<>() : targetType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (SortedMap.class.isAssignableFrom(targetType)) {
            try {
                propertyValue = targetType.isInterface() ? new TreeMap<>() : targetType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (Set.class.isAssignableFrom(targetType)) {
            try {
                propertyValue = targetType.isInterface() ? new LinkedHashSet<>() : targetType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (Map.class.isAssignableFrom(targetType)) {
            try {
                propertyValue = targetType.isInterface() ? new LinkedHashMap<>() : targetType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            propertyValue = injector.getInstance(type);
        }

        BeanUtil.declared.setProperty(bean, propertyName, propertyValue);

        return propertyValue;
    }

    protected String pathToCurrentNode(List<String> expressionParts, int index) {
        StringBuilder expr = new StringBuilder();

        int idx = 0;
        for (String expressionPart : expressionParts) {
            if (idx > 0)
                expr.append(Char.DOT);

            expr.append(expressionPart);

            if (index == idx)
                break;

            idx++;
        }

        return expr.toString();
    }

    protected String extendedName(String expression) {
        String[] names = expression.split("\\.");

        String extendedName = names[index];

        if (!extendedName.startsWith(new StringBuilder(name).append(Char.SQUARE_BRACKET_OPEN).toString())) {
            // TODO: throw exception
        }

        return extendedName;
    }

    @Override
    public boolean isLeafNode() {
        return isLeaf;
    }

    protected Object newBean() {
        if (!isSimpleJavaType(type)) {
            return injector.getInstance(type);
        }

        return null;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String expression() {
        return expression;
    }

    @Override
    public String originalExpression() {
        return originalExpression;
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public Class<?> type() {
        return type;
    }

    @Override
    public List<Class<?>> genericType() {
        return genericType;
    }

    @Override
    public PropertyNode previous() {
        return previous;
    }

    @Override
    public PropertyNode next() {
        return next;
    }

    protected boolean isSimpleJavaType(Class<?> type) {
        return (type.isPrimitive() && type != void.class) || type == Double.class || type == Float.class || type == Long.class || type == Integer.class || type == Short.class || type == Character.class || type == Byte.class || type == Boolean.class
                || type == String.class;
    }

    @Override
    public String toString() {
        return "DefaultPropertyNode [expression=" + expression + ", index=" + index + ", isLeaf=" + isLeaf + ", type=" + type + ", genericType=" + genericType + ", next=" + next + "]";
    }
}
