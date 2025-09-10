package pl.cezarysanecki.partyarchetypeapp.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cezarysanecki.partyarchetypeapp.application.RegisterOrganizationUnitUseCase;

@RestController
@RequestMapping("/api/organization-units")
public class RegisterOrganizationUnitController {
    private final RegisterOrganizationUnitUseCase useCase;

    public RegisterOrganizationUnitController(RegisterOrganizationUnitUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerOrganizationUnit(@RequestBody RegisterOrganizationUnitUseCase.RegisterOrganizationUnitCommand command) {
        useCase.registerOrganizationUnit(command);
        return ResponseEntity.ok().build();
    }
}