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

import com.geemvc.Str;
import com.geemvc.annotation.Adapter;
import com.geemvc.handler.RequestHandler;
import com.geemvc.validation.Errors;
import com.geemvc.validation.ValidationAdapter;
import com.geemvc.validation.ValidationContext;
import com.google.inject.Singleton;

import javax.validation.constraints.Pattern;
import java.util.regex.Matcher;

@Adapter
@Singleton
public class PatternValidationAdapter implements ValidationAdapter<Pattern> {

    @Override
    public boolean incudeInValidation(Pattern patternAnnotation, RequestHandler requestHandler, ValidationContext validationCtx) {
        return true;
    }

    @Override
    public void validate(Pattern patternAnnotation, String name, ValidationContext validationCtx, Errors e) {
        Object value = validationCtx.value(name);

        if (value == null || Str.isEmpty(patternAnnotation.regexp()))
            return;

        String pattern = patternAnnotation.regexp();
        Pattern.Flag[] flags = patternAnnotation.flags();

        int flagBits = 0;
        for (Pattern.Flag flag : flags) {
            flagBits |= flag.getValue();
        }

        java.util.regex.Pattern regexPattern = java.util.regex.Pattern.compile(pattern, flagBits);
        Matcher m = regexPattern.matcher(String.valueOf(value));

        if (!m.matches())
            e.add(name, patternAnnotation.message());
    }
}
