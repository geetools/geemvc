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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.cb.geemvc.Char;
import com.cb.geemvc.RequestContext;
import com.cb.geemvc.Str;

public class DefaultParamAdapters implements ParamAdapters {
    public List<String> getRequestValues(String paramName, RequestContext requestCtx) {
        Map<String, String[]> paramMap = requestCtx.getParameterMap();

        List<String> matchingValues = new ArrayList<>();

        Set<String> keys = paramMap.keySet();

        for (String key : keys) {
            if (paramName.equals(key)) {
                String[] reqValues = paramMap.get(key);
                matchingValues.addAll(Arrays.asList(reqValues));
            } else if (key.startsWith(new StringBuilder(paramName).append(Char.DOT).toString())) {
                String[] reqValues = paramMap.get(key);

                for (String mapVal : reqValues) {
                    matchingValues.add(new StringBuilder(key).append(Char.EQUALS).append(mapVal).toString());
                }

            } else if (key.startsWith(new StringBuilder(paramName).append(Char.SQUARE_BRACKET_OPEN).toString())) {
                int openSquareBracketPos = key.indexOf(Char.SQUARE_BRACKET_OPEN);
                int closeSquareBracketPos = key.indexOf(Char.SQUARE_BRACKET_CLOSE);
                String key1 = key.substring(openSquareBracketPos + 1, closeSquareBracketPos);

                int openSquareBracketPos2 = key.indexOf(Char.SQUARE_BRACKET_OPEN, closeSquareBracketPos + 1);
                int closeSquareBracketPos2 = key.indexOf(Char.SQUARE_BRACKET_CLOSE, closeSquareBracketPos + 1);

                String key2 = null;

                if (openSquareBracketPos2 == closeSquareBracketPos + 1)
                    key2 = key.substring(openSquareBracketPos2 + 1, closeSquareBracketPos2);

                String[] reqValues = paramMap.get(key);

                if (key2 == null) {
                    for (String mapVal : reqValues) {
                        matchingValues.add(new StringBuilder(key1).append(Char.EQUALS).append(mapVal).toString());
                    }
                } else {
                    for (String mapVal : reqValues) {
                        matchingValues.add(new StringBuilder(key.replace(paramName, Str.EMPTY)).append(Char.EQUALS).append(mapVal).toString());
                    }
                }
            }
        }

        return matchingValues;
    }
}
