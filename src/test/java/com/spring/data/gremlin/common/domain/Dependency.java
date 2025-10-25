/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.common.domain;

import com.spring.data.gremlin.annotation.Edge;
import com.spring.data.gremlin.annotation.EdgeFrom;
import com.spring.data.gremlin.annotation.EdgeTo;
import lombok.Data;

@Edge
@Data
public class Dependency {

    private String id;

    private String type;

    @EdgeFrom
    private Library source;

    @EdgeTo
    private Library target;

    public Dependency() {
    }

    public Dependency(String id, String type, Library source, Library target) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.target = target;
    }

    public Library getSource() {
        return source;
    }

    public void setSource(Library source) {
        this.source = source;
    }

    public Library getTarget() {
        return target;
    }

    public void setTarget(Library target) {
        this.target = target;
    }
}
