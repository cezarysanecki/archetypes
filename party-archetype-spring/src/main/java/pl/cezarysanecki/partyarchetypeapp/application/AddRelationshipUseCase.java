package pl.cezarysanecki.partyarchetypeapp.application;

import org.springframework.stereotype.Service;
import pl.cezarysanecki.partyarchetypeapp.common.Result;
import pl.cezarysanecki.partyarchetypeapp.model.*;
import pl.cezarysanecki.partyarchetypeapp.model.events.PartyRelatedFailureEvent;

@Service
public class AddRelationshipUseCase {

    private final PartyRelationshipsFacade partyRelationshipsFacade;

    public AddRelationshipUseCase(PartyRelationshipsFacade partyRelationshipsFacade) {
        this.partyRelationshipsFacade = partyRelationshipsFacade;
    }

    public Result<PartyRelatedFailureEvent, PartyRelationship> execute(
            PartyId fromId,
            Role fromRole,
            PartyId toId,
            Role toRole,
            RelationshipName name
    ) {
        return partyRelationshipsFacade.assign(fromId, fromRole, toId, toRole, name);
    }
}

