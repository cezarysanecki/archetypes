package pl.cezarysanecki.partyarchetypeapp.application;

import org.springframework.stereotype.Component;
import pl.cezarysanecki.partyarchetypeapp.model.OrganizationName;
import pl.cezarysanecki.partyarchetypeapp.model.PartiesFacade;
import pl.cezarysanecki.partyarchetypeapp.model.Role;
import pl.cezarysanecki.partyarchetypeapp.utils.RegisteredIdentifiersFactory;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RegisterCompanyUseCase {

    private static final RegisteredIdentifiersFactory REGISTERED_IDENTIFIERS_FACTORY = new RegisteredIdentifiersFactory();

    private final PartiesFacade partiesFacade;

    public RegisterCompanyUseCase(PartiesFacade partiesFacade) {
        this.partiesFacade = partiesFacade;
    }

    public void registerCompany(RegisterCompanyCommand command) {
        partiesFacade.registerCompanyFor(
                new OrganizationName(command.name),
                command.roles.stream().map(Role::new).collect(Collectors.toSet()),
                command.registeredIdentifiers.stream()
                        .map(identifier -> REGISTERED_IDENTIFIERS_FACTORY.create(identifier.type(), identifier.number()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet())
        );
    }

    public record RegisterCompanyCommand(String name,
                                         Set<String> roles,
                                         Set<RegisteredIdentifier> registeredIdentifiers) {
        public record RegisteredIdentifier(
                String type,
                String number) {
        }
    }
}

