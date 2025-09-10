package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Node("Party")
class Neo4jPartyEntity {

    @Id
    private String id;
    private String type;
    private Set<String> roles;
    @Relationship(type = "HAS_IDENTIFIER")
    private Set<Neo4jRegisteredIdentifierEntity> registeredIdentifiers;
    private String version;

    protected Neo4jPartyEntity() {}

    Neo4jPartyEntity(String id, String type, Set<String> roles, Set<Neo4jRegisteredIdentifierEntity> registeredIdentifiers, String version) {
        this.id = id;
        this.type = type;
        this.roles = roles;
        this.registeredIdentifiers = registeredIdentifiers;
        this.version = version;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
    public Set<Neo4jRegisteredIdentifierEntity> getRegisteredIdentifiers() { return registeredIdentifiers; }
    public void setRegisteredIdentifiers(Set<Neo4jRegisteredIdentifierEntity> registeredIdentifiers) { this.registeredIdentifiers = registeredIdentifiers; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    @Node("RegisteredIdentifier")
    public static class Neo4jRegisteredIdentifierEntity {
        private String type;
        private String value;

        public Neo4jRegisteredIdentifierEntity() {}

        public Neo4jRegisteredIdentifierEntity(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
}
