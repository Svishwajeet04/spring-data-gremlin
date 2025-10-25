/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.spring.data.gremlin.repository;

import com.spring.data.gremlin.common.GremlinEntityType;
import com.spring.data.gremlin.common.TestConstants;
import com.spring.data.gremlin.common.TestRepositoryConfiguration;
import com.spring.data.gremlin.common.domain.Person;
import com.spring.data.gremlin.common.domain.Project;
import com.spring.data.gremlin.common.repository.PersonRepository;
import com.spring.data.gremlin.common.repository.ProjectRepository;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
public class PersonRepositoryIT {

    private final Person person = new Person(null, TestConstants.VERTEX_PERSON_NAME);  // ID will be auto-generated
    private final Person person0 = new Person(null, TestConstants.VERTEX_PERSON_0_NAME);  // ID will be auto-generated
    private final Project project = new Project(null, TestConstants.VERTEX_PROJECT_NAME,
            TestConstants.VERTEX_PROJECT_URI);  // ID will be auto-generated

    @Autowired
    private PersonRepository repository;

    @Autowired
    private ProjectRepository projectRepository;

    @Before
    public void setup() {
        this.repository.deleteAll();
    }

    @After
    public void cleanup() {
        this.repository.deleteAll();
    }

    @Test
    public void testDeleteAll() {
        Person savedPerson = this.repository.save(this.person);

        Assert.assertTrue(this.repository.existsById(savedPerson.getId()));

        this.repository.deleteAll();

        Assert.assertFalse(this.repository.existsById(savedPerson.getId()));
    }

    @Test
    public void testDeleteById() {
        Person savedPerson = this.repository.save(this.person);
        Person savedPerson0 = this.repository.save(this.person0);

        Assert.assertTrue(this.repository.existsById(savedPerson.getId()));
        Assert.assertTrue(this.repository.existsById(savedPerson0.getId()));

        this.repository.deleteById(savedPerson.getId());

        Assert.assertFalse(this.repository.existsById(savedPerson.getId()));
        Assert.assertTrue(this.repository.existsById(savedPerson0.getId()));
    }

    @Test
    public void testDelete() {
        Person savedPerson = this.repository.save(this.person);
        Person savedPerson0 = this.repository.save(this.person0);

        Assert.assertTrue(this.repository.existsById(savedPerson.getId()));
        Assert.assertTrue(this.repository.existsById(savedPerson0.getId()));

        this.repository.delete(savedPerson);

        Assert.assertFalse(this.repository.existsById(savedPerson.getId()));
        Assert.assertTrue(this.repository.existsById(savedPerson0.getId()));
    }

    @Test
    public void testDeleteAllIds() {
        Person savedPerson = this.repository.save(this.person);
        Person savedPerson0 = this.repository.save(this.person0);
        final List<Person> domains = Arrays.asList(savedPerson, savedPerson0);

        this.repository.deleteAll(domains);

        Assert.assertFalse(this.repository.existsById(savedPerson.getId()));
        Assert.assertFalse(this.repository.existsById(savedPerson0.getId()));
    }

    @Test
    public void testSave() {
        Person savedPerson = this.repository.save(this.person);
        Person savedPerson0 = this.repository.save(this.person0);

        Assert.assertTrue(this.repository.existsById(savedPerson.getId()));
        Assert.assertTrue(this.repository.existsById(savedPerson0.getId()));
    }

    @Test
    public void testSaveAll() {
        final List<Person> domains = Arrays.asList(this.person, this.person0);

        List<Person> savedDomains = Lists.newArrayList(this.repository.saveAll(domains));

        Assert.assertTrue(this.repository.existsById(savedDomains.get(0).getId()));
        Assert.assertTrue(this.repository.existsById(savedDomains.get(1).getId()));
    }

    @Test
    public void testFindById() {
        Person savedPerson = this.repository.save(this.person);

        final Person foundPerson = this.repository.findById(savedPerson.getId()).get();

        Assert.assertNotNull(foundPerson);
        Assert.assertEquals(foundPerson.getId(), savedPerson.getId());
        Assert.assertEquals(foundPerson.getName(), savedPerson.getName());

        // Test finding non-existent person
        Assert.assertFalse(this.repository.findById("non-existent-id").isPresent());
    }

    @Test
    public void testExistById() {
        Assert.assertFalse(this.repository.existsById("non-existent-id"));

        Person savedPerson = this.repository.save(this.person);

        Assert.assertTrue(this.repository.existsById(savedPerson.getId()));
    }

    @Test
    public void testFindAllById() {
        Person savedPerson = this.repository.save(this.person);
        Person savedPerson0 = this.repository.save(this.person0);
        final List<String> ids = Arrays.asList(savedPerson.getId(), savedPerson0.getId());

        final List<Person> foundDomains = (List<Person>) this.repository.findAllById(ids);

        Assert.assertEquals(2, foundDomains.size());
        Assert.assertTrue(foundDomains.stream().anyMatch(p -> p.getId().equals(savedPerson.getId())));
        Assert.assertTrue(foundDomains.stream().anyMatch(p -> p.getId().equals(savedPerson0.getId())));
    }

    @Test
    public void testDomainClassFindAll() {
        final List<Person> domains = Arrays.asList(this.person, this.person0);
        List<Person> foundDomains = (List<Person>) this.repository.findAll(Person.class);

        Assert.assertTrue(foundDomains.isEmpty());

        List<Person> savedDomains = Lists.newArrayList(this.repository.saveAll(domains));

        foundDomains = (List<Person>) this.repository.findAll(Person.class);

        Assert.assertEquals(savedDomains.size(), foundDomains.size());

        savedDomains.sort((a, b) -> (a.getId().compareTo(b.getId())));
        foundDomains.sort((a, b) -> (a.getId().compareTo(b.getId())));

        Assert.assertArrayEquals(savedDomains.toArray(), foundDomains.toArray());
    }

    @Test
    public void testVertexCount() {
        Assert.assertEquals(this.repository.count(), 0);
        Assert.assertEquals(this.repository.edgeCount(), 0);
        Assert.assertEquals(this.repository.vertexCount(), 0);

        this.repository.save(this.person);
        this.repository.save(this.person0);

        Assert.assertEquals(this.repository.count(), 2);
        Assert.assertEquals(this.repository.edgeCount(), 0);
        Assert.assertEquals(this.repository.vertexCount(), this.repository.count());
    }

    @Test
    public void testDeleteAllByType() {
        Person savedPerson = this.repository.save(this.person);
        Person savedPerson0 = this.repository.save(this.person0);

        this.repository.deleteAll(GremlinEntityType.VERTEX);

        Assert.assertFalse(this.repository.findById(savedPerson.getId()).isPresent());
        Assert.assertFalse(this.repository.findById(savedPerson0.getId()).isPresent());
    }

    @Test
    public void testDeleteAllByClass() {
        Person savedPerson = this.repository.save(this.person);
        Person savedPerson0 = this.repository.save(this.person0);
        Project savedProject = this.projectRepository.save(this.project);

        this.repository.deleteAll(Person.class);

        Assert.assertFalse(this.repository.findById(savedPerson.getId()).isPresent());
        Assert.assertFalse(this.repository.findById(savedPerson0.getId()).isPresent());
        Assert.assertTrue(this.projectRepository.findById(savedProject.getId()).isPresent());
    }

    @Test
    public void testFindAll() {
        final List<Person> persons = Arrays.asList(this.person, this.person0);
        List<Person> savedPersons = Lists.newArrayList(this.repository.saveAll(persons));

        final List<Person> foundPersons = Lists.newArrayList(this.repository.findAll());

        foundPersons.sort(Comparator.comparing(Person::getId));
        savedPersons.sort(Comparator.comparing(Person::getId));

        Assert.assertEquals(savedPersons, foundPersons);

        this.repository.deleteAll();
        Assert.assertFalse(this.repository.findAll().iterator().hasNext());
    }
}

