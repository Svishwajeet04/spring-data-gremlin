[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/Microsoft/spring-data-gremlin/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/Microsoft/spring-data-gremlin.svg?branch=master)](https://travis-ci.org/Microsoft/spring-data-gremlin)
[![codecov](https://codecov.io/gh/Microsoft/spring-data-gremlin/branch/master/graph/badge.svg)](https://codecov.io/gh/Microsoft/spring-data-gremlin) 

:exclamation::exclamation::exclamation:

We have **deprecated** Spring Data Gremlin. We recommend that you use the TinkerPop driver to [query Cosmos DB with Gremlin API](https://docs.microsoft.com/en-us/azure/cosmos-db/create-graph-gremlin-console) or use [azure-spring-boot-starter-cosmos](https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/spring/azure-spring-boot-starter-cosmos) to query Cosmos DB with Spring Data SQL API. If you would like us to continue supporting Spring Data Gremlin, please tell us how you are using Spring Data Gremlin for Cosmos DB and how important it is to you, by voting on this [issue](https://github.com/Azure/azure-sdk-for-java/issues/24773) with the thumbs up emoji.  



# Spring Data Gremlin 

**Spring Data Gremlin** provides initial Spring Data support for those databases using Gremlin query language. With annotation oriented programming model, it simplified the mapping to the database entity. It also provides supports for basic and custom query. 

This project works with *any Gremlin-compatible* data store, and also with [Azure Cosmos DB](https://docs.microsoft.com/en-us/azure/cosmos-db/introduction). Cosmos is a globally-distributed database service that allows developers to work with data using a variety of standard APIs, such as Graph, MongoDB, and SQL. Spring Data Gremlin provides a delightful experience to interact with Azure Cosmos DB Graph API. 

## What's New

- **Major Upgrade**: Updated to Spring Framework 6.1.2 and Spring Data 3.2.1 (Spring Boot 3.x compatible)
- **Java 17 Support**: Upgraded to Java 17 as the minimum required version
- **Enhanced Query Support**: Added comprehensive raw Gremlin query execution capabilities:
  - `queryForObject()` - Execute raw queries returning single results
  - `queryForList()` - Execute raw queries returning lists with type conversion
  - `queryForPage()` - Execute paginated raw queries with automatic count queries
  - Support for offset/limit pagination in raw queries
- **Build System Migration**: Migrated from Maven to Gradle build system
- **Improved Performance**: Enhanced parallel query execution and better error handling
- **Type Safety**: Added generic type support for query results with automatic conversion
- **Backward Compatibility**: Maintained support for derived query methods (e.g., `findByAFieldAndBField`) and `@GremlinQuery` annotation

## Spring Data Version Support
Version mapping between Spring Boot and spring-data-gremlin: 

| Spring Boot version                                         | Spring Data version | spring-data-gremlin version                                                                                                                                                                                                                   | Java Version |
|:-----------------------------------------------------------:|:------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:------------:|
| ![version](https://img.shields.io/badge/version-3.2.x-blue) | 3.2.1             | [![Maven Central](https://img.shields.io/maven-central/v/com.spring.data.gremlin/spring-data-gremlin/2.3.svg)](https://search.maven.org/search?q=g:com.spring.data.gremlin%20AND%20a:spring-data-gremlin%20AND%20v:2.3.*) | 17+          |
| ![version](https://img.shields.io/badge/version-2.3.x-blue) | 2.3.0             | [![Maven Central](https://img.shields.io/maven-central/v/com.spring.data.gremlin/spring-data-gremlin/2.3.svg)](https://search.maven.org/search?q=g:com.spring.data.gremlin%20AND%20a:spring-data-gremlin%20AND%20v:2.3.*) | 8+           |
| ![version](https://img.shields.io/badge/version-2.2.x-blue) | 2.2.0             | [![Maven Central](https://img.shields.io/maven-central/v/com.spring.data.gremlin/spring-data-gremlin/2.2.svg)](https://search.maven.org/search?q=g:com.spring.data.gremlin%20AND%20a:spring-data-gremlin%20AND%20v:2.2.*) | 8+           |
| ![version](https://img.shields.io/badge/version-2.1.x-blue) | 2.1.0             | [![Maven Central](https://img.shields.io/maven-central/v/com.spring.data.gremlin/spring-data-gremlin/2.1.svg)](https://search.maven.org/search?q=g:com.spring.data.gremlin%20AND%20a:spring-data-gremlin%20AND%20v:2.1.*) | 8+           |
| ![version](https://img.shields.io/badge/version-2.0.x-blue) | 2.0.0             | [![Maven Central](https://img.shields.io/maven-central/v/com.spring.data.gremlin/spring-data-gremlin/2.0.svg)](https://search.maven.org/search?q=g:com.spring.data.gremlin%20AND%20a:spring-data-gremlin%20AND%20v:2.0.*) | 8+           |

## TOC

* [Welcome to Contribute](#welcome-to-contribute)
* [Sample Code](#sample-code)
* [What’s New](#whats-new)
* [Spring data version support](#spring-data-version-support)
* [Feature List](#feature-list)
* [Quick Start](#quick-start)
* [Filing Issues](#filing-issues)
* [Code of Conduct](#code-of-conduct)

## Welcome To Contribute

Contribution is welcome. Please follow [this instruction](./CONTRIBUTING.md) to contribute code.

## Sample Code
Please refer to [sample project here](./examples/example/).

## Spring Data Version Support
This repository now supports Spring Data 3.x (Spring Boot 3.x) with Java 17+ as the primary target, while maintaining backward compatibility with Spring Data 2.x for legacy applications. 

## Feature List
- Spring Data CRUDRepository basic CRUD functionality
    - save
    - findAll
    - findById
    - deleteAll
    - deleteById
- Spring Data [@Id](https://github.com/spring-projects/spring-data-commons/blob/db62390de90c93a78743c97cc2cc9ccd964994a5/src/main/java/org/springframework/data/annotation/Id.java) annotation.
  There're 2 ways to map a field in domain class to `id` field of a database entity.
  - annotate a field in domain class with `@Id` 
  - set name of this field to `id`
- Default annotation
  - ```@Vertex``` maps an ```Object``` to a ```Vertex```
  - ```@VertexSet``` maps a set of ```Vertex```
  - ```@Edge``` maps an ```Object``` to an ```Edge```
  - ```@EdgeSet``` maps to a set of ```Edge```
  - ```@EdgeFrom``` maps to the head ```Vertex``` of an ```Edge```
  - ```@EdgeTo``` maps to the tail ```Vertex``` of an ```Edge```
  - ```@Graph``` maps to an ```Object``` to a ```Graph```
- Supports advanced operations 
  - ```<T> T findVertexById(Object id, Class<T> domainClass);```
  - ```<T> T findEdgeById(Object id, Class<T> domainClass);```
  - ```<T> boolean isEmptyGraph(T object)```
  - ```long vertexCount()```
  - ```long edgeCount()```
- **Enhanced Query Support**:
  - Supports [Spring Data custom query](https://docs.spring.io/spring-data/commons/docs/current/reference/html/#repositories.query-methods.details) find operation, e.g.,  `findByAFieldAndBField`
  - Supports string-based queries on repository methods via `@GremlinQuery("g.V()...")` when you need full control over the Gremlin traversal
  - **Raw Gremlin Query Execution**:
    - `queryForObject(String query)` - Execute raw Gremlin queries returning single results
    - `queryForList(String query, Class<T> type)` - Execute raw queries with type conversion
    - `queryForPage(String query, Pageable pageable)` - Execute paginated raw queries
    - `queryForList(String query, int offset, int limit, Class<T> type)` - Execute paginated queries with offset/limit
- **Performance Improvements**:
  - Parallel query execution for better performance
  - Enhanced error handling and logging
  - Automatic query optimization
- Supports any class type in domain class including collection and nested type
- **Java 17+ Support**: Full compatibility with modern Java features and Spring Boot 3.x

  

## Quick Start

### Add the dependency
`spring-data-gremlin` is published on Maven Central Repository.  

**Requirements:**
- Java 17+ (for Spring Boot 3.x compatibility)
- Spring Boot 3.2.x or Spring Framework 6.1.2+

If you are using Gradle, add the following dependency to your `build.gradle` file.  

```gradle
dependencies {
    implementation 'com.spring.data.gremlin:spring-data-gremlin:2.3.1-SNAPSHOT'
}
```

If you are using Maven, add the following dependency to your `pom.xml` file.  

```xml
<dependency>
    <groupId>com.spring.data.gremlin</groupId>
    <artifactId>spring-data-gremlin</artifactId>
    <version>2.3.1-SNAPSHOT</version>
</dependency>
```

**Note:** This version requires Java 17+ and Spring Boot 3.x. For Java 8+ compatibility, use version 2.1.7.

### Setup Configuration
Setup ```application.yml``` file.(Use Azure Cosmos DB Graph as an example.)

```
gremlin:
  endpoint: url-of-endpoint 
  port: 443
  username: /dbs/your-db-name/colls/your-collection-name
  password: your-password
  telemetryAllowed: true # set false to disable telemetry

```

### Define an entity
Define a simple Vertex entity with ```@Vertex```.

```
@Vertex
public class Person {

    @Id
    private String id;

    private String name;

    private String age;

    ...
}

```

Define a simple Edge entity with ```@Edge```.

```
@Edge
public class Relation {

    @Id
    private String id;

    private String name;

    @EdgeFrom
    private Person personFrom;

    @EdgeTo
    private Person personTo;

    ...
}
```
Define a simple Graph entity with ```@Graph```.

```
@Graph
public class Network {

    @Id
    private String id;

    public Network() {
        this.edges = new ArrayList<Object>();
        this.vertexes = new ArrayList<Object>();
    }

    @EdgeSet
    private List<Object> edges;

    @VertexSet
    private List<Object> vertexes;
    
    ...
}
```

### Create repositories
Extends DocumentDbRepository interface, which provides Spring Data repository support.

```
import GremlinRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends GremlinRepository<Person, String> {
        List<Person> findByName(String name); 
}
```

`findByName` method is custom query method, it will find the person with the ```name``` property.

### Raw Gremlin Query Support
You can now execute raw Gremlin queries directly using the enhanced query support:

```java
@Repository
public interface PersonRepository extends GremlinRepository<Person, String> {
    List<Person> findByName(String name);
    
    // Raw Gremlin query with @GremlinQuery annotation
    @GremlinQuery("g.V().has('name', :name).valueMap(true)")
    List<Map<String, Object>> findPersonPropertiesByName(@Param("name") String name);
}
```

Using GremlinTemplate for advanced queries:

```java
@Service
public class PersonService {
    
    @Autowired
    private GremlinTemplate gremlinTemplate;
    
    public List<Person> findPersonsWithComplexQuery() {
        // Execute raw Gremlin query with type conversion
        return gremlinTemplate.queryForList(
            "g.V().hasLabel('person').has('age', gte(25)).valueMap(true)", 
            Person.class
        );
    }
    
    public Page<Person> findPersonsPaginated(Pageable pageable) {
        // Execute paginated raw Gremlin query
        return gremlinTemplate.queryForPage(
            "g.V().hasLabel('person').valueMap(true)", 
            pageable
        );
    }
    
    public Object executeCustomQuery(String query) {
        // Execute any raw Gremlin query
        return gremlinTemplate.queryForObject(query);
    }
}
```

### Create an Application class
Here create an application class with all the components

```
@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

    @Autowired
    private PersonRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    public void run(String... var1) throws Exception {

        private final Person testUser = new Person("PERSON_ID", "PERSON_NAME", "PERSON_AGE");

        repository.deleteAll();
        repository.save(testUser);

        ... 
    }
}
```
Autowired UserRepository interface, then can do save, delete and find operations. Spring Data Azure Cosmos DB uses the DocumentTemplate to execute the queries behind *find*, *save* methods. You can use the template yourself for more complex queries.

## Migration Guide

### Upgrading from Spring Boot 2.x to 3.x

If you're upgrading from Spring Boot 2.x to 3.x, follow these steps:

1. **Update Java Version**: Ensure you're using Java 17 or higher
2. **Update Dependencies**: Change your dependency version to `2.3.1-SNAPSHOT`
3. **Update Build Configuration**: 
   - For Gradle: Update to Gradle 7.0+ and use the new dependency syntax
   - For Maven: Ensure you're using Maven 3.6.3+
4. **Code Changes**: 
   - Replace `javax.annotation` imports with `jakarta.annotation` if needed
   - Update any deprecated Spring Data methods
5. **Configuration**: Update your `application.yml` or `application.properties` for Spring Boot 3.x compatibility

### New Features Available

- **Raw Query Execution**: Use `GremlinTemplate.queryForObject()`, `queryForList()`, and `queryForPage()` for direct Gremlin query execution
- **Enhanced Type Safety**: All query methods now support generic type parameters for better type safety
- **Improved Performance**: Parallel query execution and better error handling
- **Pagination Support**: Built-in pagination support for raw queries with automatic count queries

## Filing Issues

If you encounter any bug, please file an issue [here](https://github.com/Microsoft/spring-data-gremlin/issues/new?template=custom.md).

To suggest a new feature or changes that could be made, file an issue the same way you would for a bug.


## Code of Conduct

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/). For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

## Data/Telemetry

This project collects usage data and sends it to Microsoft to help improve our products and services. Read our [privacy](https://privacy.microsoft.com/en-us/privacystatement) statement to learn more.

