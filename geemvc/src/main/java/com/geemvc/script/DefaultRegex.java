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

package com.geemvc.script;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultRegex implements Regex {
    protected String expression = null;
    protected Pattern pattern = null;

    @Override
    public Regex build(String expression) {
        this.expression = expression;
        this.pattern = Pattern.compile(expression);

        return this;
    }

    @Override
    public boolean matches(CharSequence text) {
        Matcher m = this.pattern.matcher(text);

        return m.matches();
    }

    @Override
    public Matcher matcher(CharSequence text) {
        return this.pattern.matcher(text);
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expression == null) ? 0 : expression.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        DefaultRegex other = (DefaultRegex) obj;

        if (expression == null) {
            if (other.expression != null)
                return false;
        } else if (!expression.equals(other.expression))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "DefaultRegex [expression=" + expression + "]";
    }
}
