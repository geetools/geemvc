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

package com.cb.geemvc.validation.adapter;

import com.cb.geemvc.Str;
import com.cb.geemvc.annotation.Adapter;
import com.cb.geemvc.handler.RequestHandler;
import com.cb.geemvc.validation.Errors;
import com.cb.geemvc.validation.ValidationAdapter;
import com.cb.geemvc.validation.ValidationContext;
import com.cb.geemvc.validation.annotation.Required;
import com.google.inject.Singleton;

import javax.validation.constraints.NotNull;

@Adapter
@Singleton
public class RequiredValidationAdapter implements ValidationAdapter<Required> {

    @Override
    public boolean incudeInValidation(Required requiredAnnotation, RequestHandler requestHandler, ValidationContext validationCtx) {
        return true;
    }

    @Override
    public void validate(Required requiredAnnotation, String name, ValidationContext validationCtx, Errors e) {
        Object value = validationCtx.value(name);

        if (value instanceof String && Str.isEmpty((String) value))
            e.add(name, requiredAnnotation.message());
        else if (value == null)
            e.add(name, requiredAnnotation.message());
    }
}
