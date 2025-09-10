package pl.cezarysanecki.partyarchetypeapp.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.cezarysanecki.partyarchetypeapp.application.RegisterPersonUseCase;

@RestController
@RequestMapping("/api/persons")
public class RegisterPersonController {
    private final RegisterPersonUseCase useCase;

    public RegisterPersonController(RegisterPersonUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerPerson(@RequestBody RegisterPersonUseCase.RegisterPersonCommand command) {
        useCase.registerPerson(command);
        return ResponseEntity.ok().build();
    }
}