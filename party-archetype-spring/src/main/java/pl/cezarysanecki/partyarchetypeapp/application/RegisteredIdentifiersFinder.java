package pl.cezarysanecki.partyarchetypeapp.application;

import org.reflections.Reflections;
import pl.cezarysanecki.partyarchetypeapp.model.RegisteredIdentifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

class RegisteredIdentifiersFinder {

    RegisteredIdentifier findBy(String type, String number) {
        Class<? extends RegisteredIdentifier> clazz = findRegisteredIdentifierSubclassByType(type);
        if (clazz != null) {
            try {
                Constructor<? extends RegisteredIdentifier> constructor = clazz.getDeclaredConstructor(String.class);
                constructor.setAccessible(true);
                return constructor.newInstance(number);
            } catch (Exception e) {
                throw new RuntimeException("Cannot create RegisteredIdentifier instance " + clazz.getSimpleName(), e);
            }
        }
        return null;
    }

    private Class<? extends RegisteredIdentifier> findRegisteredIdentifierSubclassByType(String type) {
        Reflections reflections = new Reflections("pl.cezarysanecki.partyarchetypeapp.model");
        Set<Class<? extends RegisteredIdentifier>> subclasses = reflections.getSubTypesOf(RegisteredIdentifier.class);

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
