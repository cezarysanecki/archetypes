# Archetypes

The Party archetype from [softwarearchetypes.com](https://www.softwarearchetypes.com/archetypes/party/) connected to real infrastructure: Spring Boot and Neo4j.

The domain model comes from the archetype by Jakub Pilimon and Bartłomiej Słota. It describes parties (a person, a company, an organization unit), their roles, registered identifiers such as a tax number, and the relationships between them. My part is the adapter layer: how to store that model in a graph database and expose it through a REST API without changing the model itself.

I described the implementation in the blog post [Archetyp Party w Spring i Neo4j](https://cezarysanecki.pl/2025/09/26/party-w-spring-i-node4j/) 🇵🇱 PL.

## Modules

| Module | What it contains |
| --- | --- |
| [`party-archetype-model`](party-archetype-model) | the archetype: `Party`, `Person`, `Company`, `OrganizationUnit`, roles, relationships, policies that decide which roles and relationships are allowed; plain Java with no framework |
| [`party-archetype-spring`](party-archetype-spring) | Spring Boot application: REST controllers, use cases and Neo4j repositories for parties and relationships |

## Running

Java 24, Podman (or Docker) with Compose.

```bash
./run.sh
```

The script builds the application, builds the image and starts it together with Neo4j. The API listens on `localhost:8080`, the Neo4j browser on `localhost:7474` (user `neo4j`, password `test1234`).

Example requests are in [`http/`](http):

1. `register-person.http`, `register-company.http`, `register-organization-unit.http` create parties,
2. `add-relationship.http` connects two of them, for example with an `EMPLOYMENT` relationship. Replace the ids with the ones returned by the first step.
