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

package com.geemvc.validation.adapter;

import com.geemvc.annotation.Adapter;
import com.geemvc.handler.RequestHandler;
import com.geemvc.validation.AbstractValidator;
import com.geemvc.validation.Errors;
import com.geemvc.validation.ValidationAdapter;
import com.geemvc.validation.ValidationContext;
import com.google.inject.Singleton;

import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Adapter
@Singleton
public class PastValidationAdapter extends AbstractValidator implements ValidationAdapter<Past> {

    @Override
    public boolean incudeInValidation(Past pastAnnotation, RequestHandler requestHandler, ValidationContext validationCtx) {
        return true;
    }

    @Override
    public void validate(Past pastAnnotation, String name, ValidationContext validationCtx, Errors errors) {
        Object value = validationCtx.value(name);

        if (value == null)
            return;

        if (!(value instanceof Date))
            errors.add(name, pastAnnotation.message(), value);

        LocalDate inputDate = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (yesterday().isAfter(inputDate) || yesterday().isEqual(inputDate))
            errors.add(name, pastAnnotation.message(), value);

    }

    protected LocalDate yesterday() {
        return LocalDate.now().minusDays(1);
    }
}
