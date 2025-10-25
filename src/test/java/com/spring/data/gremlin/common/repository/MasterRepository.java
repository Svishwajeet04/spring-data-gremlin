/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.common.repository;

import com.spring.data.gremlin.common.domain.Master;
import com.spring.data.gremlin.repository.GremlinRepository;

public interface MasterRepository extends GremlinRepository<Master, Long> {
}
