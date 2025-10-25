/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.common.repository;

import com.spring.data.gremlin.common.domain.Network;
import com.spring.data.gremlin.repository.GremlinRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetworkRepository extends GremlinRepository<Network, String> {

    List<Network> findByEdgeList(List<Object> edgeList);
}
