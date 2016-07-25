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

package com.geemvc.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.function.Supplier;

/**
 * Created by Michael on 15.07.2016.
 */
public class DefaultLog implements Log {
    protected Logger logger = null;

    protected String GEEMVC_PACKAGE_PREFIX = "com.cb.geemvc";
    protected String GEEMVC_LOG_PREFIX = "[Geemvc] ";

    protected boolean isGeemvc() {
        return logger != null && getName().startsWith(GEEMVC_PACKAGE_PREFIX);
    }

    @Override
    public Log get(Class<?> declaringClass) {
        logger = LoggerFactory.getLogger(declaringClass);
        return this;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void trace(String message, Supplier<?>... paramSuppliers) {
        if (isTraceEnabled())
            logger.trace(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, parameters(paramSuppliers));
    }

    @Override
    public void debug(String message, Supplier<?>... paramSuppliers) {
        if (isDebugEnabled())
            logger.debug(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, parameters(paramSuppliers));
    }

    @Override
    public void info(String message, Supplier<?>... paramSuppliers) {
        if (isInfoEnabled())
            logger.info(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, parameters(paramSuppliers));
    }

    @Override
    public void warn(String message, Supplier<?>... paramSuppliers) {
        if (isWarnEnabled())
            logger.warn(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, parameters(paramSuppliers));
    }

    @Override
    public void error(String message, Supplier<?>... paramSuppliers) {
        if (isErrorEnabled())
            logger.error(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, parameters(paramSuppliers));
    }

    protected Object[] parameters(Supplier<?>... suppliers) {
        if (suppliers == null)
            return null;

        Object[] parameters = new Object[suppliers.length];

        for (int i = 0; i < suppliers.length; i++) {
            parameters[i] = suppliers[i] == null ? null : suppliers[i].get();
        }

        return parameters;
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String message) {
        if (isTraceEnabled())
            logger.trace(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message);
    }

    @Override
    public void trace(String message, Object o) {
        if (isTraceEnabled())
            logger.trace(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o);
    }

    @Override
    public void trace(String message, Object o, Object o1) {
        if (isTraceEnabled())
            logger.trace(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o, o1);
    }

    @Override
    public void trace(String message, Object... objects) {
        if (isTraceEnabled())
            logger.trace(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, objects);
    }

    @Override
    public void trace(String message, Throwable throwable) {
        if (isTraceEnabled())
            logger.trace(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String message) {
        if (isTraceEnabled(marker))
            logger.trace(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message);
    }

    @Override
    public void trace(Marker marker, String message, Object o) {
        if (isTraceEnabled(marker))
            logger.trace(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o);
    }

    @Override
    public void trace(Marker marker, String message, Object o, Object o1) {
        if (isTraceEnabled(marker))
            logger.trace(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o, o1);
    }

    @Override
    public void trace(Marker marker, String message, Object... objects) {
        if (isTraceEnabled(marker))
            logger.trace(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, objects);
    }

    @Override
    public void trace(Marker marker, String message, Throwable throwable) {
        if (isTraceEnabled(marker))
            logger.trace(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String message) {
        if (isDebugEnabled())
            logger.debug(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message);
    }

    @Override
    public void debug(String message, Object o) {
        if (isDebugEnabled())
            logger.debug(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o);
    }

    @Override
    public void debug(String message, Object o, Object o1) {
        if (isDebugEnabled())
            logger.debug(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o, o1);
    }

    @Override
    public void debug(String message, Object... objects) {
        if (isDebugEnabled())
            logger.debug(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, objects);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        if (isDebugEnabled())
            logger.debug(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String message) {
        if (isDebugEnabled(marker))
            logger.debug(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message);
    }

    @Override
    public void debug(Marker marker, String message, Object o) {
        if (isDebugEnabled(marker))
            logger.debug(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o);
    }

    @Override
    public void debug(Marker marker, String message, Object o, Object o1) {
        if (isDebugEnabled(marker))
            logger.debug(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o, o1);
    }

    @Override
    public void debug(Marker marker, String message, Object... objects) {
        if (isDebugEnabled(marker))
            logger.debug(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, objects);
    }

    @Override
    public void debug(Marker marker, String message, Throwable throwable) {
        if (isDebugEnabled(marker))
            logger.debug(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String message) {
        if (isInfoEnabled())
            logger.info(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message);
    }

    @Override
    public void info(String message, Object o) {
        if (isInfoEnabled())
            logger.info(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o);
    }

    @Override
    public void info(String message, Object o, Object o1) {
        if (isInfoEnabled())
            logger.info(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o, o1);
    }

    @Override
    public void info(String message, Object... objects) {
        if (isInfoEnabled())
            logger.info(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, objects);
    }

    @Override
    public void info(String message, Throwable throwable) {
        if (isInfoEnabled())
            logger.info(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String message) {
        if (isInfoEnabled(marker))
            logger.info(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message);
    }

    @Override
    public void info(Marker marker, String message, Object o) {
        if (isInfoEnabled(marker))
            logger.info(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o);
    }

    @Override
    public void info(Marker marker, String message, Object o, Object o1) {
        if (isInfoEnabled(marker))
            logger.info(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o, o1);
    }

    @Override
    public void info(Marker marker, String message, Object... objects) {
        if (isInfoEnabled(marker))
            logger.info(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, objects);
    }

    @Override
    public void info(Marker marker, String message, Throwable throwable) {
        if (isInfoEnabled(marker))
            logger.info(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String message) {
        if (isWarnEnabled())
            logger.warn(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message);
    }

    @Override
    public void warn(String message, Object o) {
        if (isWarnEnabled())
            logger.warn(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o);
    }

    @Override
    public void warn(String message, Object... objects) {
        if (isWarnEnabled())
            logger.warn(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, objects);
    }

    @Override
    public void warn(String message, Object o, Object o1) {
        if (isWarnEnabled())
            logger.warn(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o, o1);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        if (isWarnEnabled())
            logger.warn(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String message) {
        if (isWarnEnabled(marker))
            logger.warn(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message);
    }

    @Override
    public void warn(Marker marker, String message, Object o) {
        if (isWarnEnabled(marker))
            logger.warn(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o);
    }

    @Override
    public void warn(Marker marker, String message, Object o, Object o1) {
        if (isWarnEnabled(marker))
            logger.warn(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o, o1);
    }

    @Override
    public void warn(Marker marker, String message, Object... objects) {
        if (isWarnEnabled(marker))
            logger.warn(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, objects);
    }

    @Override
    public void warn(Marker marker, String message, Throwable throwable) {
        if (isWarnEnabled(marker))
            logger.warn(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String message) {
        if (isErrorEnabled())
            logger.error(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message);
    }

    @Override
    public void error(String message, Object o) {
        if (isErrorEnabled())
            logger.error(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o);
    }

    @Override
    public void error(String message, Object o, Object o1) {
        if (isErrorEnabled())
            logger.error(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o, o1);
    }

    @Override
    public void error(String message, Object... objects) {
        if (isErrorEnabled())
            logger.error(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, objects);
    }

    @Override
    public void error(String message, Throwable throwable) {
        if (isErrorEnabled())
            logger.error(isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String message) {
        if (isErrorEnabled(marker))
            logger.error(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message);
    }

    @Override
    public void error(Marker marker, String message, Object o) {
        if (isErrorEnabled(marker))
            logger.error(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o);
    }

    @Override
    public void error(Marker marker, String message, Object o, Object o1) {
        if (isErrorEnabled(marker))
            logger.error(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, o, o1);
    }

    @Override
    public void error(Marker marker, String message, Object... objects) {
        if (isErrorEnabled(marker))
            logger.error(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, objects);
    }

    @Override
    public void error(Marker marker, String message, Throwable throwable) {
        if (isErrorEnabled(marker))
            logger.error(marker, isGeemvc() ? GEEMVC_LOG_PREFIX + message : message, throwable);
    }
}
