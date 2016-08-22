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

package com.geemvc.rest.jaxrs.delegate;

import javax.ws.rs.core.MediaType;

import com.geemvc.Char;

public class DefaultMediaTypeHeaderDelegate implements MediaTypeHeaderDelegate {

    @Override
    public MediaType fromString(String mediaType) throws IllegalArgumentException {
        if (mediaType == null)
            throw new IllegalArgumentException("MediaType cannot be null");

        int pos = mediaType.indexOf(Char.SLASH);

        String topLevelType = mediaType.substring(0, pos);
        String subType = mediaType.substring(pos + 1);

        int parameterPos = subType.indexOf(Char.SEMI_COLON);

        if (parameterPos != -1) {
            subType = subType.substring(0, parameterPos);
        }

        return new MediaType(topLevelType, subType);
    }

    @Override
    public String toString(MediaType mediaType) {
        return new StringBuilder(mediaType.getType()).append(Char.SLASH).append(mediaType.getSubtype()).toString();
    }

}
