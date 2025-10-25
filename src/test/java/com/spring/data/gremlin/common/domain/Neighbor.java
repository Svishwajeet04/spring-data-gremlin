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
import lombok.NoArgsConstructor;

@Edge
@Data
@NoArgsConstructor
public class Neighbor {

    private Long id;

    private Long distance;

    @EdgeFrom
    private Student studentFrom;

    @EdgeTo
    private Student studentTo;

    public Neighbor(Long id, Long distance, Student studentFrom, Student studentTo) {
        this.id = id;
        this.distance = distance;
        this.studentFrom = studentFrom;
        this.studentTo = studentTo;
    }

    public Student getStudentFrom() {
        return studentFrom;
    }

    public void setStudentFrom(Student studentFrom) {
        this.studentFrom = studentFrom;
    }

    public Student getStudentTo() {
        return studentTo;
    }

    public void setStudentTo(Student studentTo) {
        this.studentTo = studentTo;
    }
}
