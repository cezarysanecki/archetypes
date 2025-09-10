package pl.cezarysanecki.partyarchetypeapp.api;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cezarysanecki.partyarchetypeapp.application.AddRelationshipUseCase;
import pl.cezarysanecki.partyarchetypeapp.model.PartyId;
import pl.cezarysanecki.partyarchetypeapp.model.RelationshipName;
import pl.cezarysanecki.partyarchetypeapp.model.Role;

@RestController
@RequestMapping("/api/relationships")
public class AddRelationshipController {

    private final AddRelationshipUseCase useCase;

    public AddRelationshipController(AddRelationshipUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<?> addRelationship(@RequestBody AddRelationshipCommand command) {
        var result = useCase.execute(
                PartyId.of(UUID.fromString(command.fromId())),
                new Role(command.fromRole()),
                PartyId.of(UUID.fromString(command.toId())),
                new Role(command.toRole()),
                new RelationshipName(command.name())
        );
        return result.fold(
                failure -> ResponseEntity.badRequest().body(failure),
                ResponseEntity::ok
        );
    }

    public record AddRelationshipCommand(String fromId, String fromRole, String toId, String toRole, String name) {}
}
