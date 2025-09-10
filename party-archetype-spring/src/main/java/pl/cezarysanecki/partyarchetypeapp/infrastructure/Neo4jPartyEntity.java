package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Map;
import java.util.Set;

@Node("Party")
class Neo4jPartyEntity {

    @Id
    private String id;
    private String type;
    private Set<String> roles;
    @CompositeProperty
    private Map<String, String> registeredIdentifiers;
    private String firstName;
    private String lastName;
    private String organizationName;
    private String version;

    public Neo4jPartyEntity() {
    }

    Neo4jPartyEntity(String id, String type, Set<String> roles, Map<String, String> registeredIdentifiers, String version, String firstName, String lastName, String organizationName) {
        this.id = id;
        this.type = type;
        this.roles = roles;
        this.registeredIdentifiers = registeredIdentifiers;
        this.version = version;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organizationName = organizationName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Map<String, String> getRegisteredIdentifiers() {
        return registeredIdentifiers;
    }

    public void setRegisteredIdentifiers(Map<String, String> registeredIdentifiers) {
        this.registeredIdentifiers = registeredIdentifiers;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
