/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.common.domain;

import com.spring.data.gremlin.annotation.EdgeSet;
import com.spring.data.gremlin.annotation.Graph;
import com.spring.data.gremlin.annotation.VertexSet;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Graph
public class Network {

    @Setter
    private String id;

    @VertexSet
    private List<Object> vertexList;

    @EdgeSet
    private List<Object> edgeList;

    public Network() {
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
    }

    public void vertexAdd(Object object) {
        this.vertexList.add(object);
    }

    public void edgeAdd(Object object) {
        this.edgeList.add(object);
    }

    public String getId() {
        return id;
    }

    public List<Object> getVertexList() {
        return vertexList != null ? new ArrayList<>(vertexList) : new ArrayList<>();
    }

    public void setVertexList(List<Object> vertexList) {
        this.vertexList = vertexList != null ? new ArrayList<>(vertexList) : new ArrayList<>();
    }

    public List<Object> getEdgeList() {
        return edgeList != null ? new ArrayList<>(edgeList) : new ArrayList<>();
    }

    public void setEdgeList(List<Object> edgeList) {
        this.edgeList = edgeList != null ? new ArrayList<>(edgeList) : new ArrayList<>();
    }
}
