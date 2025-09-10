package pl.cezarysanecki.partyarchetypeapp.infrastructure;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
class Neo4jPartyRelationshipEntity {

    @RelationshipId
    private Long id;
    private String relationshipName;

    @TargetNode
    private Neo4jPartyEntity from;
    @TargetNode
    private Neo4jPartyEntity to;

    public Neo4jPartyRelationshipEntity() {}

    Neo4jPartyRelationshipEntity(String relationshipName, Neo4jPartyEntity from, Neo4jPartyEntity to) {
        this.relationshipName = relationshipName;
        this.from = from;
        this.to = to;
    }

    public Long getId() {
        return id;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public Neo4jPartyEntity getFrom() {
        return from;
    }

    public Neo4jPartyEntity getTo() {
        return to;
    }
}

