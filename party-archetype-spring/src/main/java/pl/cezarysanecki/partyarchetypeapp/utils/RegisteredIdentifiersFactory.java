package pl.cezarysanecki.partyarchetypeapp.utils;

import org.reflections.Reflections;
import pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

public class RegisteredIdentifiersFactory {

    static final String BASE_PACKAGE = "pl.cezarysanecki.partyarchetypeapp";
    static final Reflections REFLECTIONS = new Reflections(BASE_PACKAGE);

    public RegisteredIdentifier create(String type, String number) {
        Class<? extends RegisteredIdentifier> clazz = findSubclassBy(type);
        if (clazz == null) {
            throw new IllegalArgumentException("No RegisteredIdentifier subclass found for type: " + type);
        }

        try {
            Constructor<? extends RegisteredIdentifier> constructor = clazz.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(number);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create RegisteredIdentifier instance " + clazz.getSimpleName(), e);
        }
    }

    private Class<? extends RegisteredIdentifier> findSubclassBy(String type) {
        Set<Class<? extends RegisteredIdentifier>> subclasses = REFLECTIONS.getSubTypesOf(RegisteredIdentifier.class);

        for (Class<?> subclass : subclasses) {
            try {
                Field typeField = subclass.getDeclaredField("TYPE");
                typeField.setAccessible(true);
                Object value = typeField.get(null);
                if (type.equals(value)) {
                    return subclass.asSubclass(RegisteredIdentifier.class);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
        return null;
    }

}
