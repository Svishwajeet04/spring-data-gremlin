# Spring Data Gremlin Design

### Orientation

Gremlin is a functional, data-flow language that enables users to succinctly express complex 
traversals on (or queries of) their application's property graph. It hides the details of backend
database implementation across a variety of Gremlin-compatible graph databases and services.

Apache TinkerPop's Gremlin Java driver allows you to execute Gremlin queries directly, but the
syntax can be non-trivial and you typically have to build queries yourself, for example:

```java
static final String[] gremlinQueries = new String[] {
    "g.V().drop()",
    "g.addV('person').property('id', '1').property('name', 'pli').property('age', 31)",
    "g.addV('person').property('id', '4').property('name', 'incarnation').property('age', 27)",
    "g.addV('software').property('id', '2').property('name', 'spring-boot-sample').property('lang', 'java')",
    "g.V('1').addE('created').to(g.V('2')).property('weight', 0.8)",
    "g.V('1').addE('contributed').to(g.V('2')).property('weight', 0.1)",
    "g.V('4').addE('contributed').to(g.V('2')).property('weight', 0.4)"
};
```

Spring Data Gremlin aims to simplify this by mapping Java domain objects to graph entities and
by generating Gremlin traversals for common operations, leveraging Spring Data infrastructure.

### What's New

- Spring and Spring Data updates: The project has been updated to recent Spring Boot 2.x and
  Spring Data 2.x baselines used in this repository. Refer to the Spring Data Version Support
  table in the README for exact versions.
- Query support: In addition to derived query methods (e.g., findByNameAndAge), string-based
  queries are supported via the @GremlinQuery annotation on repository methods, allowing you to
  supply raw Gremlin when needed.

### From Users' View
Users can always use the Gremlin driver directly, but a more idiomatic Spring approach is to use
Spring Data repositories and annotations to map Java objects to graph entities.

### From Graph Database View
In graph databases there are concepts like Vertex, Edge, and Graph. Domain objects map to exactly
one of these elements via annotations:

```java
@Vertex    // maps an Object to a Vertex
@VertexSet // maps a set of Vertex in Graph
@Edge      // maps an Object to an Edge
@EdgeSet   // maps to a set of Edge in Graph
@EdgeFrom  // maps to the head Vertex of an Edge
@EdgeTo    // maps to the tail Vertex of an Edge
@Graph     // maps an Object to a Graph
```

### CRUD-based queries and repositories
`GremlinRepository` extends Spring Data's `CrudRepository`, providing basic operations like
insert/save, find, delete, and count.

#### Some constraints
- Gremlin represents Vertex and Edge in a flat layout, with fixed properties `id`, `label`,
  and other key-value properties.
- Gremlin property names must be `String`; values can be `Number`, `Boolean`, or `String`.
- No nested structure inside a single Vertex or Edge property map.
- `Edge` is directed.

#### GremlinSource
Before inserting a Vertex instance, we convert a domain object into an intermediate representation
called `GremlinSource` that mirrors the flat structure of graph entities and carries `id`, `label`,
and a `Map<String, Object>` for other properties. This WRITE operation takes care of handling the
`id`/`@Id` field as well.

The `GremlinSourceWriter` converts Java instances to `GremlinSource`.

#### GremlinResult
For READ operations, results from the driver are first converted to `GremlinSource`, and then to
Java domain objects, mirroring the WRITE process.

The `GremlinResultReader` converts Result to `GremlinSource`.
The `GremlinSourceReader` converts `GremlinSource` to Java instances.

#### GremlinScript
`GremlinScript` generates Gremlin traversals based on `GremlinSource`. `String`, `Number`, and
`Boolean` properties are stored as primitives; other types are serialized to JSON-like strings
(except `Date`, which is stored as epoch milliseconds). The Gremlin client then executes the
resulting traversal against the database.

The `GremlinScriptLiteral` generates literal queries based on `GremlinSource`.

### Query options

- Derived queries: Define repository methods such as `findByAFieldAndBField` and Spring Data
  will create the traversal at runtime.
- String-based queries: Annotate repository methods with `@GremlinQuery("g.V()...")` to supply
  custom Gremlin when derived queries are insufficient.
