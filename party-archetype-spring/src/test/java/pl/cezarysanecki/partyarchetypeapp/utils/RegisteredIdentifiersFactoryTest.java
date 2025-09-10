package pl.cezarysanecki.partyarchetypeapp.utils;

import org.junit.jupiter.api.Test;
import pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisteredIdentifiersFactoryTest {

    RegisteredIdentifiersFactory sut = new RegisteredIdentifiersFactory();

    @Test
    void registeredIdentifiersShouldHaveTypeField() {
        //when
        var registeredIdentifiers = RegisteredIdentifiersFactory.REFLECTIONS.getSubTypesOf(RegisteredIdentifier.class);

        //then
        assertTrue(
                registeredIdentifiers.stream()
                        .allMatch(clazz -> hasField(clazz, "TYPE")));
    }

    @Test
    void createSampleRegisteredIdentifier() {
        //when
        RegisteredIdentifier result = sut.create("PERSONAL_IDENTIFICATION_NUMBER", "12345678901");

        //then
        assertEquals("PERSONAL_IDENTIFICATION_NUMBER", result.type());
        assertEquals("12345678901", result.value());
    }

    private static boolean hasField(Class<? extends RegisteredIdentifier> clazz, String fieldName) {
        try {
            clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

}