package pl.cezarysanecki.partyarchetypeapp.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cezarysanecki.partyarchetypeapp.application.RegisterCompanyUseCase;

@RestController
@RequestMapping("/api/companies")
public class RegisterCompanyController {

    private final RegisterCompanyUseCase useCase;

    public RegisterCompanyController(RegisterCompanyUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerCompany(@RequestBody RegisterCompanyUseCase.RegisterCompanyCommand command) {
        useCase.registerCompany(command);
        return ResponseEntity.ok().build();
    }
}

