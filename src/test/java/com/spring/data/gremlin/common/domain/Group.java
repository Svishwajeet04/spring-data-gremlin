/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.common.domain;

import com.spring.data.gremlin.annotation.Edge;
import com.spring.data.gremlin.annotation.EdgeFrom;
import com.spring.data.gremlin.annotation.EdgeTo;
import com.spring.data.gremlin.annotation.GeneratedValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Edge
@Data
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue
    private Long id;

    @EdgeFrom
    private Student student;

    @EdgeTo
    private GroupOwner groupOwner;

    public Group(Student student, GroupOwner groupOwner) {
        this.student = student;
        this.groupOwner = groupOwner;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public GroupOwner getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(GroupOwner groupOwner) {
        this.groupOwner = groupOwner;
    }
}
