package pl.cezarysanecki.partyarchetypeapp.application;

import org.springframework.stereotype.Component;
import pl.cezarysanecki.partyarchetypeapp.model.PartiesFacade;
import pl.cezarysanecki.partyarchetypeapp.model.PersonalData;
import pl.cezarysanecki.partyarchetypeapp.model.Role;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RegisterPersonUseCase {

    private static final RegisteredIdentifiersFinder REGISTERED_IDENTIFIERS_FINDER = new RegisteredIdentifiersFinder();

    private final PartiesFacade partiesFacade;

    public RegisterPersonUseCase(PartiesFacade partiesFacade) {
        this.partiesFacade = partiesFacade;
    }

    public void registerPerson(RegisterPersonCommand command) {
        partiesFacade.registerPersonFor(
                new PersonalData(command.firstName, command.lastName),
                command.roles.stream().map(Role::new).collect(Collectors.toSet()),
                command.registeredIdentifiers.stream()
                        .map(identifier -> REGISTERED_IDENTIFIERS_FINDER.findBy(identifier.type(), identifier.number()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet())

        );
    }

    public record RegisterPersonCommand(String firstName,
                                        String lastName,
                                        Set<String> roles,
                                        Set<RegisteredIdentifier> registeredIdentifiers) {
        public record RegisteredIdentifier(
                String type,
                String number) {
        }
    }

}
