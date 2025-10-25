/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.common.repository;

import com.spring.data.gremlin.common.domain.SimpleDependency;
import com.spring.data.gremlin.repository.GremlinRepository;

public interface SimpleDependencyRepository extends GremlinRepository<SimpleDependency, String> {
}
