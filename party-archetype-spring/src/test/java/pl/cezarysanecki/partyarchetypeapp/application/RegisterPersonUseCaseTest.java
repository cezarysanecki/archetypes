package pl.cezarysanecki.partyarchetypeapp.application;

import org.junit.jupiter.api.Test;

import java.util.Set;

class RegisterPersonUseCaseTest {

    @Test
    void shouldRegisterPerson() {
        //given
        RegisterPersonUseCase.RegisterPersonCommand command = new RegisterPersonUseCase.RegisterPersonCommand(
                "John",
                "Doe",
                Set.of("USER"),
                Set.of(new RegisterPersonUseCase.RegisterPersonCommand.RegisteredIdentifier("PERSONAL_IDENTIFICATION_NUMBER", "12345678901"))
        );

        //when
        RegisterPersonUseCase sut = new RegisterPersonUseCase(null);
        sut.registerPerson(command);
    }

}