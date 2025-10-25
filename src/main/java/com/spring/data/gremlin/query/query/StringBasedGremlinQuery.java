/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.query.query;

import com.spring.data.gremlin.query.GremlinOperations;
import com.spring.data.gremlin.query.paramerter.GremlinParameterAccessor;
import org.springframework.data.repository.query.Parameters;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RepositoryQuery to execute a raw Gremlin query defined via @GremlinQuery on repository methods.
 */
public class StringBasedGremlinQuery extends AbstractGremlinQuery {

    private final String rawQueryTemplate;

    public StringBasedGremlinQuery(@NonNull GremlinQueryMethod method, @NonNull GremlinOperations operations,
                                   @NonNull String rawQueryTemplate) {
        super(method, operations);
        this.rawQueryTemplate = rawQueryTemplate;
    }

    @Override
    protected GremlinQuery createQuery(@NonNull GremlinParameterAccessor accessor) {
        String bound = bindParameters(rawQueryTemplate, getQueryMethod(), accessor);
        return GremlinQuery.raw(bound);
    }

    static String bindParameters(String template, GremlinQueryMethod method, GremlinParameterAccessor accessor) {
        Map<String, Object> valuesByName = new HashMap<>();
        Parameters<?, ?> params = method.getParameters();
        for (int i = 0; i < params.getNumberOfParameters(); i++) {
            String inferredName = params.getParameter(i).getName().orElse("p" + i);
            Object value = accessor.getBindableValue(i);
            valuesByName.put(inferredName, value);
            valuesByName.put("p" + i, value); // positional fallback
        }

        // Replace occurrences of :name with serialized values. Word boundary for names (letters, digits, underscore)
        Pattern p = Pattern.compile(":([a-zA-Z0-9_]+)");
        Matcher m = p.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String name = m.group(1);
            Object v = valuesByName.get(name);
            String replacement = serialize(v);
            // escape backslashes and dollar signs for Matcher.appendReplacement
            replacement = replacement.replace("\\", "\\\\").replace("$", "\\$");
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String serialize(Object v) {
        if (v == null) {
            return "null";
        }
        if (v instanceof Number || v instanceof Boolean) {
            return String.valueOf(v);
        }
        // default to single-quoted string with escaping
        String s = String.valueOf(v);
        s = s.replace("\\", "\\\\").replace("'", "\\'");
        return "'" + s + "'";
    }
}
