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

package com.cb.geemvc.bind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.core.Context;

import com.cb.geemvc.Bindings;
import com.cb.geemvc.bind.param.ParamAdapterFactory;
import com.cb.geemvc.bind.param.annotation.Model;
import com.cb.geemvc.reflect.ReflectionProvider;
import com.cb.geemvc.validation.Errors;
import com.google.inject.Inject;

public class DefaultMethodParam implements MethodParam {
    protected String name;
    protected Class<?> type;
    protected Type parameterizedType;
    protected Annotation[] annotations;
    protected Annotation paramAnnotation;

    private boolean isInitialized = false;

    protected final ParamAdapterFactory paramAdapterFactory;
    protected final ReflectionProvider reflectionProvider;

    @Inject
    public DefaultMethodParam(ParamAdapterFactory paramAdapterFactory, ReflectionProvider reflectionProvider) {
        this.paramAdapterFactory = paramAdapterFactory;
        this.reflectionProvider = reflectionProvider;
    }

    @Override
    public MethodParam build(String name, Class<?> type, Type parameterizedType, Annotation[] annotations) {
        if (!isInitialized) {
            this.name = name;
            this.type = type;
            this.parameterizedType = parameterizedType;
            this.annotations = annotations;
            this.paramAnnotation = paramAnnotation();

            if (this.paramAnnotation == null) {
                this.annotations = provideAnnotation(name, type, parameterizedType);
                this.paramAnnotation = paramAnnotation();
            }

            this.isInitialized = true;
        } else {
            throw new IllegalStateException("MethodArg.build() can only be called once");
        }

        return this;
    }

    protected Annotation[] provideAnnotation(String name, Class<?> type, Type parameterizedType) {
        Annotation[] modelAnnotation = null;

        if (isContextType(type, parameterizedType)) {
            modelAnnotation = new Annotation[]{contextAnnotation()};
        } else {
            modelAnnotation = new Annotation[]{modelAnnotation()};
        }

        if (annotations != null && annotations.length > 0) {
            Annotation[] mergedAnnotations = new Annotation[annotations.length + 1];

            System.arraycopy(annotations, 0, mergedAnnotations, 0, annotations.length);
            System.arraycopy(modelAnnotation, 0, mergedAnnotations, annotations.length, 1);

            modelAnnotation = mergedAnnotations;
        }

        return modelAnnotation;
    }

    protected Model modelAnnotation() {
        return new Model() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Model.class;
            }

            @Override
            public String value() {
                return name;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String defaultValue() {
                return "__N-O-N-E__";
            }
        };
    }

    protected Context contextAnnotation() {
        return new Context() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return Context.class;
            }
        };
    }

    protected boolean isContextType(Class<?> type, Type parameterizedType) {
        List<Class<?>> genericType = null;

        if (Map.class.isAssignableFrom(type) && parameterizedType != null) {
            genericType = reflectionProvider.getGenericType(parameterizedType);
        }

        if (ServletRequest.class.isAssignableFrom(type)
                || ServletResponse.class.isAssignableFrom(type)
                || ServletContext.class.isAssignableFrom(type)
                || HttpSession.class.isAssignableFrom(type)
                || Cookie[].class.isAssignableFrom(type)
                || Locale.class.isAssignableFrom(type)
                || Errors.class.isAssignableFrom(type)
                || Bindings.class.isAssignableFrom(type)
                || (Map.class.isAssignableFrom(type) && genericType != null && genericType.size() == 2 && String.class == genericType.get(0) && String[].class == genericType.get(1))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<?> type() {
        return type;
    }

    @Override
    public Type parameterizedType() {
        return parameterizedType;
    }

    @Override
    public Annotation[] annotations() {
        return annotations;
    }

    @Override
    public Annotation paramAnnotation() {
        if (annotations == null || annotations.length == 0)
            return null;

        if (!isInitialized) {
            Optional<Annotation> o = Arrays.asList(annotations).stream().filter((a) -> paramAdapterFactory.exists(a.annotationType())).findFirst();
            this.paramAnnotation = o.isPresent() ? o.get() : null;
        }

        return this.paramAnnotation;
    }

    @Override
    public boolean isNullable() {
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == Nullable.class)
                    return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(annotations);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parameterizedType == null) ? 0 : parameterizedType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DefaultMethodParam other = (DefaultMethodParam) obj;
        if (!Arrays.equals(annotations, other.annotations))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (parameterizedType == null) {
            if (other.parameterizedType != null)
                return false;
        } else if (!parameterizedType.equals(other.parameterizedType))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DefaultMethodParam [name=" + name + ", type=" + type + ", parameterizedType=" + parameterizedType + ", annotations=" + Arrays.toString(annotations) + "]";
    }
}
