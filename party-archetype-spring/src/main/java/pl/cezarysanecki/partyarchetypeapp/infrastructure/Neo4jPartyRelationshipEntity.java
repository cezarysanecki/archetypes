package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
class Neo4jPartyRelationshipEntity {

    @RelationshipId
    private Long id;
    private String relationshipId;
    private String relationshipName;

    private String from;
    private String fromRole;
    private String to;
    private String toRole;

    @TargetNode
    private Neo4jPartyEntity toEntity;

    public Neo4jPartyRelationshipEntity() {
    }

    Neo4jPartyRelationshipEntity(String relationshipId, String relationshipName, String from, String fromRole, String to, String toRole, Neo4jPartyEntity toEntity) {
        this.relationshipId = relationshipId;
        this.relationshipName = relationshipName;
        this.fromRole = fromRole;
        this.to = to;
        this.toRole = toRole;
        this.toEntity = toEntity;
    }

    public Long getId() {
        return id;
    }

    public String getRelationShipId() {
        return relationshipId;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public String getFrom() {
        return from;
    }

    public String getFromRole() {
        return fromRole;
    }

    public String getTo() {
        return to;
    }

    public String getToRole() {
        return toRole;
    }

    public Neo4jPartyEntity getToEntity() {
        return toEntity;
    }
}