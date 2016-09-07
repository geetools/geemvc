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

package com.geemvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.geemvc.validation.Validator;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Request {
    String value() default "";

    String path() default "";

    String name() default "";

    String[] ignore() default {};

    String[] method() default {};

    String[] params() default {};

    String[] headers() default {};

    String[] cookies() default {};

    String handles() default "";

    String[] consumes() default {};

    String[] produces() default {};

    int priority() default 111;

    Class<? extends Validator>[] validator() default {};

    String onError() default "";
}
