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

package com.geemvc.matcher;

import com.geemvc.Char;
import com.geemvc.RequestContext;
import com.geemvc.Str;
import com.geemvc.config.Configuration;
import com.geemvc.script.Regex;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultPathMatcher implements PathMatcher {
    private static final long serialVersionUID = 8682403475843946688L;

    // Path specified in RequestMapping annotation.
    protected String mappedPath = null;

    // Mapped path converted to regular-expression.
    protected Regex pathRegex = null;

    // Retrieved parameter names from regex groups.
    protected List<String> parameterNames = null;

    // Does the mapped-path include both the controller and the handler mapped path?
    protected boolean isCompletePath = false;

    protected String[] ignorePaths = null;

    protected List<String> ignoreRegexPaths = null;

    protected List<Pattern> ignorePatterns = null;

    @Inject
    protected Injector injector;

    @Inject
    protected Configuration configuration;

    @Override
    public PathMatcher build(String path) {
        boolean isForceRegexPath = false;

        if (path.startsWith(Str.CARET)) {
            isForceRegexPath = true;
            path = path.substring(1);
        }

        if (isForceRegexPath && path.endsWith(Str.DOLLAR)) {
            path = path.substring(0, path.length() - 1);
        }

        // Remove trailing slash to ensure that pattern does not fail simply because of a trailing slash.
        if (path.endsWith(Str.SLASH))
            path = path.substring(0, path.length() - 1);

        // Remove trailing slash to ensure that pattern does not fail simply because of a trailing slash.
        if (!path.startsWith(Str.SLASH))
            path = Str.SLASH + path;

        this.mappedPath = path;

        // Convert mapped annotation path to regular expression for matching request-paths later.
        this.pathRegex = toRegexPath(path, isForceRegexPath);

        return this;
    }

    @Override
    public PathMatcher build(String parentPath, String path) {
        isCompletePath = true;

        if (parentPath == null)
            parentPath = Str.EMPTY;

        if (path == null)
            path = Str.EMPTY;

        // Simple case #1: If the parent (controller) path and the handler path is empty, turn the path into a
        // catch-all-path.
        if (parentPath.isEmpty() && path.isEmpty()) {
            this.mappedPath = "/.*";
            this.pathRegex = toRegexPath(this.mappedPath, true);
            return this;
        }

        // Simple case #2: Do not bother with regular-expression if we are merely looking for a plain index-mapping (/).
        if (Str.SLASH.equals(new StringBuilder(parentPath).append(path))) {
            this.mappedPath = Str.SLASH;
            return this;
        }

        StringBuilder combinedPath = new StringBuilder(parentPath);

        boolean isForceRegexPath = false;

        if (path.startsWith(Str.CARET)) {
            isForceRegexPath = true;
            path = path.substring(1);
        }

        if (isForceRegexPath && path.endsWith(Str.DOLLAR)) {
            path = path.substring(0, path.length() - 1);
        }

        if (!path.startsWith(Str.SLASH))
            combinedPath.append(Char.SLASH);

        combinedPath.append(path);

        if (combinedPath.charAt(0) != Char.SLASH)
            combinedPath.insert(0, Char.SLASH);

        if (combinedPath.charAt(combinedPath.length() - 1) == Char.SLASH) {
            // Only delete last slash if it is not the only character in the request-URI (i.e. in index page).
            if (combinedPath.length() > 1)
                combinedPath.deleteCharAt(combinedPath.length() - 1);
        }

        this.mappedPath = combinedPath.toString();

        // Convert mapped annotation path to regular expression for matching request-paths later.
        this.pathRegex = toRegexPath(this.mappedPath, isForceRegexPath);

        return this;
    }

    /**
     * Matches the request-path against the regular-expression that we created from the original path.
     */
    @Override
    public boolean matches(RequestContext requestCtx, MatcherContext matcherCtx) {
        String path = requestCtx.getPath();

        if (this.pathRegex != null) {
            return pathRegex.matches(path);
        } else if (this.mappedPath != null) {
            if (isCompletePath) {
                boolean matches = path.equals(this.mappedPath);

                if (!matches && path.indexOf(Char.DOT) != -1) {
                    matches = matchesWithSuffix(path);
                }

                return matches;
            } else {
                return path.startsWith(this.mappedPath);
            }
        }

        return false;
    }

    protected boolean matchesWithSuffix(String path) {
        List<String> supportedUriSuffixes = configuration.supportedUriSuffixes();

        if (supportedUriSuffixes != null && !supportedUriSuffixes.isEmpty()) {
            for (String suffix : supportedUriSuffixes) {
                if (path.equals(new StringBuilder(this.mappedPath).append(suffix).toString())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns a map of path-parameters that were retrieved using the regular-expression.
     */
    @Override
    public Map<String, String[]> parameters(RequestContext requestCtx) {
        if (this.pathRegex != null) {
            String path = requestCtx.getPath();

            Matcher m = this.pathRegex.matcher(path);

            Map<String, String[]> parameters = new LinkedHashMap<>();

            if (m.matches()) {
                int count = m.groupCount();

                for (int i = 0; i < count; i++) {
                    parameters.put(this.parameterNames.get(i), new String[]{m.group(i + 1)});
                }
            }

            return parameters;
        }

        return null;
    }

    protected Regex toRegexPath(String path) {
        return toRegexPath(path, false);
    }

    protected Regex toRegexPath(String path, boolean forceRegex) {
        // This path does not contain parameters or wild-cards.
        if (path.indexOf(Char.CURLY_BRACKET_OPEN) == -1 && path.indexOf(Char.ASTERIX) == -1) {
            // Path has already been marked as a regular-expression though
            // (most likely because the original mapped path started with a caret).
            if (forceRegex) {
                return injector.getInstance(Regex.class).build(new StringBuilder(Str.CARET).append(path).append(Char.DOLLAR).toString());
            }

            return null;
        }

        // Begin of regular-expression.
        StringBuilder regexPath = new StringBuilder(Str.CARET);

        // Find first character of variables with the StringTokenizer.
        StringTokenizer st = new StringTokenizer(path, Str.CURLY_BRACKET_OPEN);

        while (st.hasMoreTokens()) {
            String s = st.nextToken();

            // Find the closing bracket of the variable.
            int pos1 = s.indexOf(Char.CURLY_BRACKET_CLOSE);
            int pos2 = s.lastIndexOf(Char.CURLY_BRACKET_CLOSE);

            // If no curly bracket exists in this part then there is no need to continue processing it.
            if (pos1 == -1) {
                regexPath.append(s);
                continue;
            }

            // Simple check to make sure that we only have one closing curly bracket - otherwise there is an error in
            // the mapped path.
            if (pos1 != pos2)
                throw new IllegalStateException("Syntax Error!");

            // Remove the closing curly bracket from variable-name.
            String var = s.substring(0, pos1);

            // Variable names must start with a letter. Could potentially be part of a regex, i.e. [0-9]{1}.
            if (!var.matches("^[a-zA-Z]{1}.*")) {
                regexPath.append(Char.CURLY_BRACKET_OPEN).append(s);
                continue;
            }

            // If we have variables and the list has not been created yet, create it now on demand.
            if (this.parameterNames == null)
                this.parameterNames = new ArrayList<>();

            // If the variable name contains a colon, then the second half is a regular-expression defined by the user.
            int pos = var.indexOf(Char.COLON);

            // No need to create our own regular expression if the user has already created one.
            if (pos != -1) {
                String regexPart = s.substring(pos + 1, pos1);

                // Make sure that the regex-group does not start with a caret.
                if (regexPart.trim().charAt(0) == Char.CARET)
                    regexPart = regexPart.substring(1);

                regexPath.append(Char.BRACKET_OPEN).append(regexPart).append(Char.BRACKET_CLOSE).append(s.substring(pos1 + 1));
                this.parameterNames.add(var.substring(0, pos));
            }
            // Otherwise convert the variable names to regex-groups.
            else {
                regexPath.append("([^\\/]+)").append(s.substring(pos1 + 1).replace("**", ".+").replace(Str.ASTERIX, "[^\\/]+"));
                this.parameterNames.add(var);
            }
        }

        // End the regular-expression.
        regexPath.append(Char.DOLLAR);

        String r1 = regexPath.toString();
        String r2 = r1.replace("**", ".*");
        String r3 = r2.replaceAll("([^\\.\\*]{1})\\*", "$1[^\\\\/]*");

        return injector.getInstance(Regex.class).build(r3);
    }

    @Override
    public int parameterCount() {
        return parameterNames == null ? 0 : parameterNames.size();
    }

    @Override
    public boolean parameterExists(String name) {
        return parameterNames != null && parameterNames.contains(name);
    }

    @Override
    public String getMappedPath() {
        return mappedPath;
    }

    @Override
    public String getRegexPath() {
        return pathRegex == null ? null : pathRegex.getExpression();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mappedPath == null) ? 0 : mappedPath.hashCode());
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
        DefaultPathMatcher other = (DefaultPathMatcher) obj;
        if (mappedPath == null) {
            if (other.mappedPath != null)
                return false;
        } else if (!mappedPath.equals(other.mappedPath))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DefaultPathMatcher [mappedPath=" + mappedPath + ", pathRegex=" + pathRegex + ", parameterNames=" + parameterNames + ", ignorePaths=" + Arrays.toString(ignorePaths) + ", ignoreRegexPaths=" + ignoreRegexPaths + ", ignorePatterns="
                + ignorePatterns + "]";
    }
}
