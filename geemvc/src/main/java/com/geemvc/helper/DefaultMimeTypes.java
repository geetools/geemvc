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

package com.geemvc.helper;

import com.geemvc.Char;
import com.google.inject.Singleton;

@Singleton
public class DefaultMimeTypes implements MimeTypes {
    @Override
    public String toWildCard(String mimeType) {
        if (mimeType == null)
            return null;

        int pos = mimeType.indexOf(Char.SLASH);
        String generalPart = mimeType.substring(0, pos).trim();

        return new StringBuilder(generalPart).append(Char.SLASH).append(Char.ASTERIX).toString();
    }

    @Override
    public boolean match(String mimeType1, String mimeType2) {
        if (mimeType1 == mimeType2 || (mimeType1 != null && mimeType1.equalsIgnoreCase(mimeType2)))
            return true;

        int pos1 = mimeType1.indexOf(Char.SLASH);
        String generalPart1 = mimeType1.substring(0, pos1).trim();

        int pos2 = mimeType1.indexOf(Char.SLASH);
        String generalPart2 = mimeType1.substring(0, pos2).trim();

        return generalPart1 != null && generalPart1.equalsIgnoreCase(generalPart2);
    }

}
