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

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Adapter
@Singleton
public class DecimalMinValidationAdapter extends AbstractValidator implements ValidationAdapter<DecimalMin> {

    @Override
    public boolean incudeInValidation(DecimalMin decimalMinAnnotation, RequestHandler requestHandler, ValidationContext validationCtx) {
        return true;
    }

    @Override
    public void validate(DecimalMin decimalMinAnnotation, String name, ValidationContext validationCtx, Errors errors) {
        Object value = validationCtx.value(name);

        if (value == null)
            return;

        if (!(value instanceof BigDecimal))
            errors.add(name, decimalMinAnnotation.message(), value);

        if (!validateMin(BigDecimal.valueOf(Double.valueOf(decimalMinAnnotation.value())), value)) {
            errors.add(name, decimalMinAnnotation.message(), value, decimalMinAnnotation.value());
        }
    }
}
