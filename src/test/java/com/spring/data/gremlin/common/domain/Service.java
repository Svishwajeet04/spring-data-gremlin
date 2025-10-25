/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.common.domain;

import com.spring.data.gremlin.annotation.Vertex;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
@Vertex
public class Service {

    @Id
    private String id;

    private int instanceCount;

    private boolean active;

    private String name;

    private ServiceType type;

    private Date createAt;

    private Map<String, Object> properties;

    public Service(String id, int instanceCount, boolean active, String name, ServiceType type, Date createAt, Map<String, Object> properties) {
        this.id = id;
        this.instanceCount = instanceCount;
        this.active = active;
        this.name = name;
        this.type = type;
        this.createAt = createAt != null ? new Date(createAt.getTime()) : null;
        this.properties = properties != null ? new HashMap<>(properties) : new HashMap<>();
    }

    public Date getCreateAt() {
        return createAt != null ? new Date(createAt.getTime()) : null;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt != null ? new Date(createAt.getTime()) : null;
    }

    public Map<String, Object> getProperties() {
        return properties != null ? new HashMap<>(properties) : new HashMap<>();
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties != null ? new HashMap<>(properties) : new HashMap<>();
    }
}
