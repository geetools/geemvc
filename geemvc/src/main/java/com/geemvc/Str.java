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

package com.geemvc;

public class Str {
    public static final String SPACE = String.valueOf(Char.SPACE);
    public static final String DOUBLE_SPACE = "  ";
    public static final String SLASH = String.valueOf(Char.SLASH);
    public static final String SLASH_ESCAPED = "\\" + Char.SLASH;
    public static final String DOT = String.valueOf(Char.DOT);
    public static final String DOT_ESCAPED = "\\" + Char.DOT;
    public static final String AT = String.valueOf(Char.AT);
    public static final String CARET = String.valueOf(Char.CARET);
    public static final String COMMA = String.valueOf(Char.COMMA);
    public static final String COLON = String.valueOf(Char.COLON);
    public static final String SEMI_COLON = String.valueOf(Char.SEMI_COLON);
    public static final String ASTERIX = String.valueOf(Char.ASTERIX);
    public static final String EQUALS = String.valueOf(Char.EQUALS);
    public static final String EQUALS_2X = "==";
    public static final String EQUALS_3X = "===";
    public static final String PLUS = String.valueOf(Char.PLUS);
    public static final String HYPHEN = String.valueOf(Char.HYPHEN);
    public static final String HYPHEN_2x = "--";
    public static final String HYPHEN_3x = "---";
    public static final String HYPHEN_ESCAPED = "\\" + Char.HYPHEN;
    public static final String UNDERSCORE = String.valueOf(Char.UNDERSCORE);
    public static final String UNDERSCORE_2X = "__";
    public static final String UNDERSCORE_3X = "___";
    public static final String AMPERSAND = String.valueOf(Char.AMPERSAND);
    public static final String AMPERSAND_2X = "&&";
    public static final String BACKSLASH = String.valueOf(Char.BACKSLASH);
    public static final String BACKSLASH_ESCAPED = "\\" + Char.BACKSLASH;
    public static final String LESS_THAN = String.valueOf(Char.LESS_THAN);
    public static final String GREATER_THAN = String.valueOf(Char.GREATER_THAN);
    public static final String EXCLAMATION_MARK = String.valueOf(Char.EXCLAMATION_MARK);
    public static final String QUESTION_MARK = String.valueOf(Char.QUESTION_MARK);
    public static final String HASH = String.valueOf(Char.HASH);
    public static final String PERCENT = String.valueOf(Char.PERCENT);
    public static final String EUR = String.valueOf(Char.EUR);
    public static final String GBP = String.valueOf(Char.GBP);
    public static final String DOLLAR = String.valueOf(Char.DOLLAR);
    public static final String DOLLAR_ESCAPED = "\\" + Char.DOLLAR;
    public static final String SINGLE_QUOTE = String.valueOf(Char.SINGLE_QUOTE);
    public static final String SINGLE_QUOTE_ESCAPED = "\\" + Char.SINGLE_QUOTE;
    public static final String DOUBLE_QUOTE = String.valueOf(Char.DOUBLE_QUOTE);
    public static final String DOUBLE_QUOTE_ESCAPED = "\\" + Char.DOUBLE_QUOTE;
    public static final String BRACKET_OPEN = String.valueOf(Char.BRACKET_OPEN);
    public static final String BRACKET_OPEN_ESCAPED = "\\" + Char.BRACKET_OPEN;
    public static final String BRACKET_CLOSE = String.valueOf(Char.BRACKET_CLOSE);
    public static final String BRACKET_CLOSE_ESCAPED = "\\" + Char.BRACKET_CLOSE;
    public static final String BRACKET_OPEN_CLOSE = "()";
    public static final String SQUARE_BRACKET_OPEN = String.valueOf(Char.SQUARE_BRACKET_OPEN);
    public static final String SQUARE_BRACKET_CLOSE = String.valueOf(Char.SQUARE_BRACKET_CLOSE);
    public static final String SQUARE_BRACKET_OPEN_CLOSE = "[]";
    public static final String CURLY_BRACKET_OPEN = String.valueOf(Char.CURLY_BRACKET_OPEN);
    public static final String CURLY_BRACKET_CLOSE = String.valueOf(Char.CURLY_BRACKET_CLOSE);
    public static final String CURLY_BRACKET_OPEN_CLOSE = "{}";
    public static final String PIPE = String.valueOf(Char.PIPE);
    public static final String PIPE_2X = "||";
    public static final String NEWLINE = String.valueOf(Char.NEWLINE);
    public static final String PROTOCOL_SUFFIX = "://";
    public static final String EMPTY = "";
    public static final String[] EMPTY_ARRAY = {};
    public static final String NUL = String.valueOf(Char.NUL);
    public static final String NULL_STRING = "null";
    public static final String TILDE = String.valueOf(Char.TILDE);
    public static final String EQUALS_TILDE = "=~";

    public static boolean isEmpty(String s) {
        if (s == null)
            return true;

        return EMPTY.equals(s.trim());
    }

    public static boolean trimEquals(String s1, String s2) {
        if (s1 == s2)
            return true;

        if (s1 == null && s2 == null)
            return true;

        if ((s1 != null && s2 == null) || (s1 == null && s2 != null))
            return false;

        return s1.trim().equals(s2.trim());
    }

    public static boolean trimEqualsIgnoreCase(String s1, String s2) {
        if (s1 == s2)
            return true;

        if (s1 == null && s2 == null)
            return true;

        if ((s1 != null && s2 == null) || (s1 == null && s2 != null))
            return false;

        return s1.trim().equalsIgnoreCase(s2.trim());
    }
}
