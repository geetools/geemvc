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

package com.geemvc.validation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class DefaultValidation implements Validation {
    protected String name;
    protected Annotation validationAnnotation;
    protected ValidationAdapter<? extends Annotation> validationAdapter;
    protected String[] on = null;

    @Override
    public Validation build(Annotation validationAnnotation, ValidationAdapter<? extends Annotation> validationAdapter) {
        this.validationAnnotation = validationAnnotation;
        this.validationAdapter = validationAdapter;

        return this;
    }

    @Override
    public Validation build(String name, Annotation validationAnnotation, ValidationAdapter<? extends Annotation> validationAdapter) {
        this.name = name;
        this.validationAnnotation = validationAnnotation;
        this.validationAdapter = validationAdapter;

        return this;
    }

    @Override
    public Validation build(String name, Annotation validationAnnotation, ValidationAdapter<? extends Annotation> validationAdapter, String[] on) {
        this.name = name;
        this.validationAnnotation = validationAnnotation;
        this.validationAdapter = validationAdapter;
        this.on = on;

        return this;
    }

    @Override
    public String name() {
        return name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Annotation> T annotation() {
        return (T) validationAnnotation;
    }

    @Override
    public ValidationAdapter<? extends Annotation> adapter() {
        return validationAdapter;
    }

    @Override
    public String[] on() {
        return on;
    }

    @Override
    public String toString() {
        return "DefaultValidation{" +
                "name='" + name + '\'' +
                ", validationAnnotation=" + validationAnnotation +
                ", validationAdapter=" + validationAdapter +
                ", on=" + Arrays.toString(on) +
                '}';
    }
}
