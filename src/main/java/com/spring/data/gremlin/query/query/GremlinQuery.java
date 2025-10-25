/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.query.query;

import com.spring.data.gremlin.query.criteria.Criteria;
import lombok.Getter;
import lombok.NonNull;

public class GremlinQuery {

    @Getter
    private final Criteria criteria;

    @Getter
    private final String rawQuery;

    private GremlinQuery(Criteria criteria, String rawQuery) {
        this.criteria = criteria;
        this.rawQuery = rawQuery;
    }

    public GremlinQuery(@NonNull Criteria criteria) {
        this(criteria, null);
    }

    public static GremlinQuery raw(@NonNull String rawQuery) {
        return new GremlinQuery(null, rawQuery);
    }

    public boolean isRaw() {
        return this.rawQuery != null;
    }
}
