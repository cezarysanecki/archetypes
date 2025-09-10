package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Node("Party")
class Neo4jPartyEntity {

    @Id
    String id;
    String type;
    Set<String> roles;
    @CompositeProperty
    Map<String, String> registeredIdentifiers;
    String firstName;
    String lastName;
    String organizationName;
    Long version;

    @Relationship(type = "Relationship", direction = Relationship.Direction.OUTGOING)
    private Set<Neo4jPartyRelationshipEntity> relationships = new HashSet<>();

    Neo4jPartyEntity(String id, String type, Set<String> roles, Map<String, String> registeredIdentifiers, Long version, String firstName, String lastName, String organizationName) {
        this.id = id;
        this.type = type;
        this.roles = roles;
        this.registeredIdentifiers = registeredIdentifiers;
        this.version = version;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organizationName = organizationName;
    }

    public void addRelationship(Neo4jPartyRelationshipEntity relationship) {
        this.relationships.add(relationship);
    }

}
