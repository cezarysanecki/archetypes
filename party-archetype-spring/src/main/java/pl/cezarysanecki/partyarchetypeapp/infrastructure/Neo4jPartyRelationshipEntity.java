package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
class Neo4jPartyRelationshipEntity {

    @RelationshipId
    private String id;
    private String relationshipName;

    @TargetNode
    private Neo4jPartyEntity from;
    private String fromRole;

    @TargetNode
    private Neo4jPartyEntity to;
    private String toRole;

    public Neo4jPartyRelationshipEntity() {
    }

    Neo4jPartyRelationshipEntity(String id, String relationshipName, Neo4jPartyEntity from, String fromRole, Neo4jPartyEntity to, String toRole) {
        this.id = id;
        this.relationshipName = relationshipName;
        this.from = from;
        this.fromRole = fromRole;
        this.to = to;
        this.toRole = toRole;
    }

    public String getId() {
        return id;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public Neo4jPartyEntity getFrom() {
        return from;
    }

    public String getFromRole() {
        return fromRole;
    }

    public Neo4jPartyEntity getTo() {
        return to;
    }

    public String getToRole() {
        return toRole;
    }
}