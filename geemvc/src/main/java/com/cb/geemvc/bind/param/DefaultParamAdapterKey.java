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

package com.cb.geemvc.bind.param;

import java.lang.annotation.Annotation;

public class DefaultParamAdapterKey implements ParamAdapterKey {
    protected Class<? extends Annotation> paramAnnotation = null;
    protected boolean isInitialized = false;

    public ParamAdapterKey build(Class<? extends Annotation> paramAnnotation) {
        if (!isInitialized) {
            this.paramAnnotation = paramAnnotation;
            this.isInitialized = true;
        } else {
            throw new IllegalStateException("ParamAdapterKey.build() can only be called once");
        }

        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((paramAnnotation == null) ? 0 : paramAnnotation.hashCode());
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

        DefaultParamAdapterKey other = (DefaultParamAdapterKey) obj;

        if (paramAnnotation == null) {
            if (other.paramAnnotation != null)
                return false;
        } else if (!paramAnnotation.equals(other.paramAnnotation))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "DefaultParamAdapterKey [paramAnnotation=" + paramAnnotation + "]";
    }
}
